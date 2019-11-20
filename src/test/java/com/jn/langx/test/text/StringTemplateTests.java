package com.jn.langx.test.text;

import com.jn.langx.test.bean.Person;
import com.jn.langx.text.StringTemplates;
import com.jn.langx.util.collection.Collects;
import org.junit.Test;

import java.util.Map;

public class StringTemplateTests {

    private void testFormatBasedIndex(Object[] args) {
        System.out.println("====testFormatBasedIndex start ====");
        System.out.println(StringTemplates.format("", args));
        System.out.println(StringTemplates.format(" \t ", args));
        System.out.println(StringTemplates.format(" a, b, c ", args));
        System.out.println(StringTemplates.format(" a{}, b{1}, c{2} ", args));
        System.out.println(StringTemplates.format(" a{1}, b{2}, c{3} ", args));
        System.out.println(StringTemplates.format(" a{0}, b{1}, c{2} ", args));
        System.out.println("====testFormatBasedIndex end ====");
    }

    private void testFormatBasedOrder(Object[] args) {
        System.out.println("====testFormatBasedOrder start ====");
        System.out.println(StringTemplates.formatWithPlaceholder("", args));
        System.out.println(StringTemplates.formatWithPlaceholder(" \t ", args));
        System.out.println(StringTemplates.formatWithPlaceholder(" a, b, c ", args));
        System.out.println(StringTemplates.formatWithPlaceholder(" a{}, b{1}, c{2} ", args));
        System.out.println(StringTemplates.formatWithPlaceholder(" a{1}, b{}, c{3} ", args));
        System.out.println(StringTemplates.formatWithPlaceholder(" a{0}, b{}, c{} ", args));
        System.out.println("====testFormatBasedOrder end ====");
    }

    @Test
    public void test2() {
        testFormatBasedIndex(new Object[]{1, "hello", "world", "build"});
        testFormatBasedOrder(new Object[]{1, "hello", "world", "build"});
    }

    @Test
    public void testBeanTemplate() {
        Person p = new Person();
        p.setId("id-1231231231");
        p.setAge(12);
        p.setName("zhangsan");
        p.setDesc("Hello, I'm from china");

        String template = "ID: ${id}, name: ${name}, age: ${age}, desc: ${desc}, xxxx: ${xxxx}";
        System.out.println(StringTemplates.formatWithBean(template, p));
    }


    @Test
    public void testMapTemplate() {
        Map<String, Object> map = Collects.emptyHashMap();
        map.put("id", "id-1231231231");
        map.put("age", "12");
        map.put("name", "zhangsan");
        map.put("desc", "Hello, I'm from china");


        String template = "ID: ${id}, name: ${name}, age: ${age}, desc: ${desc}, xxxx: ${xxxx}";
        System.out.println(StringTemplates.formatWithMap(template, map));
    }

}
