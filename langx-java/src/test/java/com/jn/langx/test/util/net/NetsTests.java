package com.jn.langx.test.util.net;

import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Consumer;
import com.jn.langx.util.net.Nets;
import org.junit.Test;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class NetsTests {
    @Test
    public void testGetNetworkInterfaces() {
        Map<String, Set<InetAddress>> a = Nets.getNetworkInterfaceAddresses(null, null);
        System.out.println(a);
        Map<String, Set<InetAddress>> b = Nets.getNetworkInterfaceAddresses();
        System.out.println(b);

        List<NetworkInterface> interfaceList = Nets.getNetworkInterfaces();
        Collects.forEach(interfaceList, new Consumer<NetworkInterface>() {
            @Override
            public void accept(NetworkInterface networkInterface) {
                try {
                    System.out.println(Nets.getMac(networkInterface));
                } catch (SocketException ex) {

                }
            }
        });
    }
}
