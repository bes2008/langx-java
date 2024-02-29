package com.jn.langx.test.util;

import com.jn.langx.util.Validations;
import org.junit.Test;

public class ValidationTests {

    @Test
    public void testHostname(){
        String[] hostnames=new String[]{
                "hello.world.213.com",
                "hello-world.213.com",
                "hello.world-.213.com",
                "hello.world.213.-com",
                "Linux17219",
                "linux17219"
        };
        for (int i = 0; i < hostnames.length; i++) {
            String hostname=hostnames[i];
            System.out.println(Validations.isValidRFC1123Hostname(hostname));
        }

    }

    @Test
    public void testPort(){
        System.out.println(Validations.isValidPort(-1));
        System.out.println(Validations.isValidPort(0));
        System.out.println(Validations.isValidPort(1));
        System.out.println(Validations.isValidPort(65535));
        System.out.println(Validations.isValidPort(65536));
        System.out.println(Validations.isValidPort(65537));
    }

}
