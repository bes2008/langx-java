package com.jn.langx.test.util.net;

import com.jn.langx.util.net.ClusterAddressParser;
import com.jn.langx.util.net.NetworkAddress;
import com.jn.langx.util.regexp.RegexpPatterns;
import com.jn.langx.util.regexp.Regexps;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

public class ClusterAddressParserTests {
    private static ClusterAddressParser parser = new ClusterAddressParser();

    @BeforeClass
    public static void init(){
        parser.setDefaultPort(80);
    }

    @Test
    public void testIpv4Ip(){
        System.out.println(Regexps.match(RegexpPatterns.PATTERN_IP,"192.168.19.16"));
        System.out.println(Regexps.match(RegexpPatterns.PATTERN_IP,"192.168.19.16/23"));
    }

    @Test
    public void testIpv4Port() {
        System.out.println("=======================Test IP V4 start====================");
        List<NetworkAddress> addresses = null;
        addresses = parser.parse("host1:9092,host2:2181,host3:8080");
        System.out.println(addresses);

        addresses = parser.parse("host1,host2,host3:8080");
        System.out.println(addresses);

        addresses = parser.parse("host1,host2,host3");
        System.out.println(addresses);

        addresses = parser.parse("host1");
        System.out.println(addresses);

        addresses = parser.parse("192.168.19.16:2100");
        System.out.println(addresses);

        addresses = parser.parse("192.168.1.1:2100,192.168.1.2:2100,192.168.1.3:2100");
        System.out.println(addresses);

        addresses = parser.parse("192.168.1.1:2100,192.168.1.2:2100,192.168.1.3");
        System.out.println(addresses);


        System.out.println("=======================Test IP V4 end====================");
    }

    @Test
    public void testIpv6Port() {
        System.out.println("=======================Test IP V6 start====================");
        List<NetworkAddress> addresses = null;
        addresses = parser.parse("2031:0000:1F1F:0000:0000:0100:11A0:ADDF:9092,host2:2181,host3:8080");
        System.out.println(addresses);

        addresses = parser.parse("host3:8080,2031:0000:1F1F:0000:0000:0100:11A0:ADDF:9092,host2:2181");
        System.out.println(addresses);

        addresses = parser.parse("host3:8080,[2031:0000:1F1F:0000:0000:0100:11A0:ADDF]:9092,host2:2181");
        System.out.println(addresses);

        addresses = parser.parse("2000::1,2000::1:8080,2031:0000:1F1F:0000:0000:0100:11A0:ADDF:9092,host2:2181,host4");
        System.out.println(addresses);

        addresses = parser.parse("host1:9092,host2:2181,host3:8080");
        System.out.println(addresses);

        addresses = parser.parse("host1,host2,host3:8080");
        System.out.println(addresses);

        addresses = parser.parse("host1,host2,host3");
        System.out.println(addresses);

        addresses = parser.parse("host1:9092,192.168.1.2:2181,host3:8080");
        System.out.println(addresses);

        addresses = parser.parse("host1");
        System.out.println(addresses);

        addresses = parser.parse("host3:8080,::/12,host2:2181, ::0:9092");
        System.out.println(addresses);
        System.out.println("=======================Test IP V6 end====================");

    }
}
