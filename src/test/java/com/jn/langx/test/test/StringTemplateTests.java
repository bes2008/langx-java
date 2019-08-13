package com.jn.langx.test.test;

import com.jn.langx.text.StringTemplates;
import org.junit.Test;

public class StringTemplateTests {
    private void test(Object[] args) {
        System.out.println(StringTemplates.format("", args));
        System.out.println(StringTemplates.format(" \t ", args));
        System.out.println(StringTemplates.format(" a, b, c ", args));
        System.out.println(StringTemplates.format(" a{}, b{1}, c{2} ", args));
        System.out.println(StringTemplates.format(" a{1}, b{2}, c{3} ", args));
        System.out.println(StringTemplates.format(" a{0}, b{1}, c{2} ", args));
    }

    @Test
    public void test2(){
        test(new Object[]{1,"hello","world","build"});
    }

}
