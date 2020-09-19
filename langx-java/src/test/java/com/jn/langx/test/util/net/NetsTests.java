package com.jn.langx.test.util.net;

import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.function.Consumer;
import com.jn.langx.util.function.Function;
import com.jn.langx.util.net.Nets;
import org.junit.Test;

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
}
