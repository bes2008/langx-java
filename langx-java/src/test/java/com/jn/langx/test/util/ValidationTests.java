package com.jn.langx.test.util;

import com.jn.langx.util.Validations;
import org.junit.Test;

public class ValidationTests {

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
