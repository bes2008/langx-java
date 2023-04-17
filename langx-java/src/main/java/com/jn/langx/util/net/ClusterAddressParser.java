package com.jn.langx.util.net;

import com.jn.langx.Parser;
import com.jn.langx.util.*;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.Lists;
import com.jn.langx.util.function.Consumer;
import com.jn.langx.util.function.Consumer2;
import com.jn.langx.util.function.Predicate;
import com.jn.langx.util.function.Predicate2;
import com.jn.langx.util.logging.Loggers;
import com.jn.langx.util.regexp.Regexp;
import com.jn.langx.util.regexp.RegexpPatterns;
import com.jn.langx.util.regexp.Regexps;
import org.slf4j.Logger;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 * case 1:
 *  host1:port1,host2:port2,host3,host4:port4 => host1:port1, host2:port2,host3:defaultPort,host4:port4
 * case 2:
 *  host1,host2,host3:port  => host1:port,host2:port,host3:port
 * case 3:
 *  host1,host2,host3 => host1:defaultPort, host2:defaultPort, host3: defaultPort
 * </pre>
 */
public class ClusterAddressParser implements Parser<String, List<NetworkAddress>> {
    private static final Regexp IP_PORT_SEGMENT_PATTERN = Regexps.createRegexp("(?<ip>[^/]*)((/(?<prefixLength>\\d{1,6}))?(:(?<port>\\d{1,5}))?)?");
    private static final Regexp IPv4_PORT_SEGMENT_PATTERN = Regexps.createRegexp("(?<ip>[^:]*)(:(?<port>\\d{1,5}))?");
    private int defaultPort = -1;
    private boolean supportsPortAtEnd = false;

    public ClusterAddressParser() {
    }

    public ClusterAddressParser(int defaultPort) {
        this(defaultPort, false);
    }

    public ClusterAddressParser(int defaultPort, boolean supportsPortAtEnd) {
        setDefaultPort(defaultPort);
        setSupportsPortAtEnd(supportsPortAtEnd);
    }

    public void setSupportsPortAtEnd(boolean supportsPortAtEnd) {
        this.supportsPortAtEnd = supportsPortAtEnd;
    }

    /**
     * 转换为 host1:port1,host2:port2,host3:port3结构
     */
    public static String normalize(List<NetworkAddress> addresses, final ClusterAddressStyle style) {
        Preconditions.checkNotEmpty(addresses);
        final StringBuilder builder = new StringBuilder(256);
        Collects.forEach(addresses, new Consumer2<Integer, NetworkAddress>() {
            @Override
            public void accept(Integer index, NetworkAddress address) {
                if (index > 0) {
                    builder.append(",");
                }
                if (style == ClusterAddressStyle.HOST_PORT_PAIR) {
                    builder.append(address.getHost() + ":" + address.getPort());
                } else if (style == ClusterAddressStyle.PORT_AT_END) {
                    builder.append(address.getHost());
                }
            }
        });
        if (style == ClusterAddressStyle.PORT_AT_END) {
            builder.append(":" + addresses.get(addresses.size() - 1).getPort());
        }
        return builder.toString();
    }


    public List<NetworkAddress> parse(String s) {
        if (Strings.isBlank(s)) {
            return Collections.emptyList();
        }

        final List<NetworkAddress> ret = Lists.newArrayList();

        String[] segments = Strings.split(s, ",");
        Logger logger = Loggers.getLogger(getClass());

        for (String segment : segments) {
            segment = Strings.strip(segment);
            // ip(/prefixLength)?(:port)?
            Map<String, String> stringMap = Regexps.findNamedGroup(IP_PORT_SEGMENT_PATTERN, segment);
            if (stringMap != null) {
                String ip = stringMap.get("ip");
                if (Strings.isBlank(ip)) {
                    logger.warn("invalid ip address: {}", segment);
                }

                String prefixLength = stringMap.get("prefixLength");
                if (Strings.isNotEmpty(prefixLength)) {
                    ip = ip + "/" + prefixLength;
                }

                int ipv6PortIndex = ip.startsWith("[") ? Strings.indexOf(ip, "]:") : -1;
                String portString = ipv6PortIndex > 0 ? Strings.substring(ip, ipv6PortIndex + 2) : null;
                boolean hasBrace = (ip.startsWith("[") && segment.endsWith("]")) || (ipv6PortIndex > 0);
                ip = hasBrace ? Strings.substring(ip, 1, ipv6PortIndex > 0 ? ipv6PortIndex : (ip.length() - 1)) : segment;

                portString = Strings.isEmpty(portString) ? stringMap.get("port") : portString;
                int port = Strings.isEmpty(portString) ? 0 : Numbers.createInteger(portString);
                if (port > 65535) {
                    logger.warn("invalid port : {}", segment);
                    continue;
                }
                if (!hasBrace && Regexps.match(RegexpPatterns.PATTERN_IPv4, ip)) {
                    // IP v4，但没有端口
                    ret.add(new NetworkAddress(ip, port, NetworkAddress.AddrMode.V4));
                } else if (!hasBrace && Regexps.match("\\w+(\\.\\w+)*(:\\d{1,5})?", ip)) {
                    // host:port, ipv4:port
                    Map<String, String> string2Map = Regexps.findNamedGroup(IPv4_PORT_SEGMENT_PATTERN, segment);
                    if (string2Map != null) {
                        ip = string2Map.get("ip");
                        if (Strings.isBlank(ip)) {
                            logger.warn("invalid ip address: {}", segment);
                        }
                        portString = string2Map.get("port");
                        port = Strings.isEmpty(portString) ? 0 : Numbers.createInteger(portString);
                        if (port > 65535) {
                            logger.warn("invalid port : {}", segment);
                            continue;
                        }

                        NetworkAddress.AddrMode v = NetworkAddress.AddrMode.HOST;
                        if(Regexps.match(RegexpPatterns.PATTERN_IPv4, ip)){
                            v = NetworkAddress.AddrMode.V4;
                        }
                        ret.add(new NetworkAddress(ip, port, v));
                    }
                } else if (Regexps.match(RegexpPatterns.PATTERN_IPv6, ip)) {
                    ret.add(new NetworkAddress("[" + ip + "]", port, NetworkAddress.AddrMode.V6));
                } else {
                    int portSeparator = Strings.lastIndexOf(segment, ":");
                    if (portSeparator >= 1 && portSeparator < segment.length() - 1) {
                        if (':' == segment.charAt(portSeparator - 1)) {
                            logger.warn("invalid ip address: {}", segment);
                            continue;
                        } else {
                            portString = Strings.substring(segment, portSeparator + 1);
                            try {
                                port = Strings.isEmpty(portString) ? 0 : Numbers.createInteger(portString);
                            } catch (Throwable e) {
                                logger.warn("invalid ip address: {}", segment);
                                continue;
                            }
                            ip = Strings.substring(segment, 0, portSeparator);
                            if (!hasBrace && Regexps.match(RegexpPatterns.PATTERN_IPv4, ip)) {
                                // IP v4
                                ret.add(new NetworkAddress(ip, port, NetworkAddress.AddrMode.V4));
                                continue;
                            } else if (Regexps.match(RegexpPatterns.PATTERN_IPv6, ip)) {
                                ret.add(new NetworkAddress("[" + ip + "]", port, NetworkAddress.AddrMode.V6));
                                continue;
                            }
                        }
                    }

                    logger.warn("invalid ip address: {}", segment);
                }
            } else {
                logger.warn("invalid ip address: {}", segment);
            }
        }
        if (Objs.isNotEmpty(ret)) {

            boolean portAtEnd = supportsPortAtEnd;
            if (supportsPortAtEnd) {
                // 只有最后一个地址有端口时，useUnifiedPortInAddressString 的值为true，即表示统一使用该端口
                int firstValidPortIndex = Collects.firstOccurrence(ret, new Predicate2<Integer, NetworkAddress>() {
                    @Override
                    public boolean test(Integer index, NetworkAddress address) {
                        return address.getPort() > 0 && address.getPort() < 65535;
                    }
                });

                portAtEnd = firstValidPortIndex == (ret.size() - 1);
            }
            final int unifiedPort = portAtEnd ? ret.get(ret.size() - 1).getPort() : defaultPort;
            if (unifiedPort > 0) {
                Collects.forEach(ret, new Predicate<NetworkAddress>() {
                    @Override
                    public boolean test(NetworkAddress address) {
                        return address.getPort() < 1;
                    }
                }, new Consumer<NetworkAddress>() {
                    @Override
                    public void accept(NetworkAddress address) {
                        address.setPort(unifiedPort);
                    }
                });
            }
        }
        return ret;
    }

    public String normalize(String s, ClusterAddressStyle style) {
        List<NetworkAddress> addresses = parse(s);
        if (Emptys.isEmpty(addresses)) {
            return "";
        }
        return normalize(addresses, style);
    }

    public String normalize(String s) {
        return normalize(s, ClusterAddressStyle.HOST_PORT_PAIR);
    }

    public static enum ClusterAddressStyle {
        HOST_PORT_PAIR,
        PORT_AT_END;
    }


    public int getDefaultPort() {
        return defaultPort;
    }

    public void setDefaultPort(int defaultPort) {
        this.defaultPort = defaultPort;
    }
}
