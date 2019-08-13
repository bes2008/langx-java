package com.jn.langx.test.util;

import org.junit.Test;
import static com.jn.langx.util.Strings.*;

public class StringsTests {
    @Test
    public void isEmptyTest(){
        System.out.println(isEmpty(null));
        System.out.println(isEmpty(""));
        System.out.println(isEmpty(" "));
        System.out.println(isEmpty("\t"));
        System.out.println(isEmpty("\n"));
    }

    @Test
    public void isBlankTest(){
        System.out.println(isBlank(null));
        System.out.println(isBlank(""));
        System.out.println(isBlank(" "));
        System.out.println(isBlank("\t"));
        System.out.println(isBlank("\n"));
    }
}
