package com.jn.langx.test.util.net;

import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.function.Consumer;
import com.jn.langx.util.function.Function;
import com.jn.langx.util.net.Nets;
import com.jn.langx.util.regexp.Regexp;
import com.jn.langx.util.regexp.RegexpPatterns;
import com.jn.langx.util.regexp.Regexps;
import org.junit.Test;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class NetsTests {
    @Test
    public void testGetNetworkInterfaces() throws Throwable {
        Map<String, Set<InetAddress>> a = Nets.getNetworkInterfaceAddresses(null, null);
        System.out.println("net address mappings:");
        System.out.println(a);
        System.out.println("======================");

        Map<String, Set<InetAddress>> b = Nets.getNetworkInterfaceAddresses();
        System.out.println("net addresses:");
        System.out.println(b);
        System.out.println("======================");

        System.out.println("macs:");
        List<NetworkInterface> interfaceList = Nets.getNetworkInterfaces();
        Collects.forEach(interfaceList, new Consumer<NetworkInterface>() {
            @Override
            public void accept(NetworkInterface networkInterface) {
                System.out.println(networkInterface.getName() + ":::" + networkInterface.getDisplayName() + ":::" + Nets.getMac(networkInterface));
            }
        });
        System.out.println("=====================");
        System.out.println("first mac:");
        NetworkInterface firstValid = Nets.getFirstValidNetworkInterface();
        System.out.println(firstValid);

        System.out.println(Pipeline.<InetAddress>of(Nets.addressesFromNetworkInterface(firstValid)).map(new Function<InetAddress, String>() {
            @Override
            public String apply(InetAddress address) {
                return Nets.toAddressString(address);
            }
        }).asList());
        InetAddress currentAddress = Nets.getCurrentAddress();
        if (currentAddress != null) {
            System.out.println(currentAddress.getHostName());
            System.out.println(Nets.toAddressString(currentAddress));
        }
        System.out.println(Nets.getFirstValidMac());
    }

    @Test
    public void testIpv6Pattern() {
        // ip v6 地址

        String str = "2001:0000:3238:00E1:0063:0000:0000:FEFB";
        System.out.println(Regexps.match(RegexpPatterns.PATTERN_IPv6, str));
        str = "2001:0:3238:E1:0063::FEFB";
        System.out.println(Regexps.match(RegexpPatterns.PATTERN_IPv6, str));
        str = "::1";
        System.out.println(Regexps.match(RegexpPatterns.PATTERN_IPv6, str));
        str = "::";
        System.out.println(Regexps.match(RegexpPatterns.PATTERN_IPv6, str));
        str = "::/12";
        System.out.println(Regexps.match(RegexpPatterns.PATTERN_IPv6, str));

        Regexp IP_SEGMENT_PATTERNS = Regexps.createRegexp("(?<ip>[^/]*)(/(?<prefixLength>\\d{1,6})(:(?<port>\\d{1,6}))?)?");
        str = "::/12";
        Map<String, String> stringMap = Regexps.findNamedGroup(IP_SEGMENT_PATTERNS, str);
        System.out.println(stringMap);
        str = "::/12:123";
        stringMap = Regexps.findNamedGroup(IP_SEGMENT_PATTERNS, str);
        System.out.println(stringMap);
    }

    @Test
    public void testIpv4ToInt() throws Throwable{
        System.out.println(Nets.ipv4AddressToInt((Inet4Address) InetAddress.getByName("localhost")));
        System.out.println(Nets.ipv4AddressToInt("127.0.0.1"));
    }

    @Test
    public void testIpv6ToInt() throws Throwable{
        System.out.println(Nets.ipv6AddressToLong("fe80:0000:0000:0000:021b:77ff:fbd6:7860"));

        System.out.println(Nets.ipv4MappingToIpv6("127.0.0.1"));
        System.out.println(Nets.ipv4MappingToIpv6("127.0.0.1", true));
    }
}
