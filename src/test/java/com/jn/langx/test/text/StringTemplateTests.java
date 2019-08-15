package com.jn.langx.test.text;

import com.jn.langx.text.StringTemplates;
import org.junit.Test;

public class StringTemplateTests {

    private void testFormatBasedIndex(Object[] args) {
        System.out.println(StringTemplates.format("", args));
        System.out.println(StringTemplates.format(" \t ", args));
        System.out.println(StringTemplates.format(" a, b, c ", args));
        System.out.println(StringTemplates.format(" a{}, b{1}, c{2} ", args));
        System.out.println(StringTemplates.format(" a{1}, b{2}, c{3} ", args));
        System.out.println(StringTemplates.format(" a{0}, b{1}, c{2} ", args));
    }

    private void testFormatBasedOrder(Object[] args) {
        System.out.println(StringTemplates.formatWithoutIndex("", args));
        System.out.println(StringTemplates.formatWithoutIndex(" \t ", args));
        System.out.println(StringTemplates.formatWithoutIndex(" a, b, c ", args));
        System.out.println(StringTemplates.formatWithoutIndex(" a{}, b{1}, c{2} ", args));
        System.out.println(StringTemplates.formatWithoutIndex(" a{1}, b{}, c{3} ", args));
        System.out.println(StringTemplates.formatWithoutIndex(" a{0}, b{}, c{} ", args));
    }

    @Test
    public void test2(){
        testFormatBasedIndex(new Object[]{1,"hello","world","build"});
        testFormatBasedOrder(new Object[]{1,"hello","world","build"});
    }

}
