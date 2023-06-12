package com.jn.langx.test.util.logging.masking;

import com.jn.langx.security.masking.Maskings;
import org.junit.Test;

public class MaskTests {
    @Test
    public void test() {
        System.out.println(Maskings.masking("phone", "+86-13711112792"));
        System.out.println(Maskings.masking("phone", "13711112792323"));
        System.out.println(Maskings.masking("phone", "1371142792"));
        System.out.println(Maskings.masking("phone", "137142792"));
        System.out.println(Maskings.masking("phone", "13742792"));
        System.out.println(Maskings.masking("phone", "1742792"));
    }
}
