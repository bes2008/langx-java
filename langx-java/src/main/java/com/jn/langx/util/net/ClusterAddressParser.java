package com.jn.langx.util.net;

import com.jn.langx.Parser;
import com.jn.langx.util.Emptys;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Consumer;
import com.jn.langx.util.function.Consumer2;
import com.jn.langx.util.function.Predicate;
import com.jn.langx.util.function.Predicate2;
import com.jn.langx.util.logging.Loggers;
import org.slf4j.Logger;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

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
    private int defaultPort = -1;

    public ClusterAddressParser() {
    }

    public ClusterAddressParser(int defaultPort) {
        setDefaultPort(defaultPort);
    }

    /**
     * 转换为 host1:port1,host2:port2,host3:port3结构
     *
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

        final List<NetworkAddress> ret = new LinkedList<NetworkAddress>();

        String[] segments = Strings.split(s, ",");
        Logger logger = Loggers.getLogger(getClass());
        for (String segment : segments) {
            segment = Strings.strip(segment);
            String[] ipSegments = segment.split(":");
            if (ipSegments.length < 1) {
                // ignore
            } else if (ipSegments.length == 1) {
                // 只有 ip, 且是ipv4
                ret.add(new NetworkAddress(ipSegments[0], 0));
            } else if (ipSegments.length == 2) {
                // ip:port 结构，且是 ipv4
                try {
                    int port = Integer.parseInt(ipSegments[1]);
                    ret.add(new NetworkAddress(ipSegments[0], port));
                } catch (NumberFormatException ex) {
                    logger.error("not a illegal address string: {}", s);
                }
            } else if (ipSegments.length == 3 && segment.contains("::")) {
                // ip v6 缩写形式，无端口
                ret.add(new NetworkAddress(segment, 0));
            } else if (ipSegments.length == 4 && segment.contains("::")) {
                // ip v6 缩写形式，有端口
                ret.add(new NetworkAddress(segment, 0));
            } else if (ipSegments.length == 8) {
                // ip v6 缩写形式，无端口
                ret.add(new NetworkAddress(segment, 0));
            } else if (ipSegments.length == 9) {
                // ip v6 缩写形式，有端口
                try {
                    int port = Integer.parseInt(ipSegments[ipSegments.length - 1]);
                    ret.add(new NetworkAddress(segment.substring(0, segment.lastIndexOf(":")), port));
                } catch (NumberFormatException ex) {
                    logger.error("not a illegal address string: {}", s);
                }
            } else {
                logger.error("not a illegal address string: {}", s);
            }
        }

        // 只有最后一个地址又端口时，useUnifiedPortInAddressString 的值为true，即表示统一使用该端口
        int firstValidPortIndex = Collects.firstOccurrence(ret, new Predicate2<Integer, NetworkAddress>() {
            @Override
            public boolean test(Integer index, NetworkAddress address) {
                return address.getPort() > 0;
            }
        });

        boolean useUnifiedPortInAddressString = firstValidPortIndex == (ret.size() - 1);

        final int unifiedPort = useUnifiedPortInAddressString ? ret.get(ret.size() - 1).getPort() : defaultPort;
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
