package com.jn.langx.test.text;

import com.jn.langx.test.bean.Person;
import com.jn.langx.text.stringtemplate.StringTemplateFormatterChain;
import com.jn.langx.text.StringTemplates;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Function2;
import org.junit.Test;

import java.util.Map;

public class StringTemplateTests {

    private void testFormatBasedIndex(Object[] args) {
        System.out.println("====testFormatBasedIndex start ====");
        System.out.println(StringTemplates.formatWithIndex("", args));
        System.out.println(StringTemplates.formatWithIndex(" \t ", args));
        System.out.println(StringTemplates.formatWithIndex(" a, b, c ", args));
        System.out.println(StringTemplates.formatWithIndex(" a{}, b{1}, c{2} ", args));
        System.out.println(StringTemplates.formatWithIndex(" a{1}, b{2}, c{3} ", args));
        System.out.println(StringTemplates.formatWithIndex(" a{0}, b{1}, c{2} ", args));
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
        Person p = newBean();
        String template = "ID: ${id}, name: ${name}, age: ${age}, desc: ${desc}, xxxx: ${xxxx}";
        System.out.println(StringTemplates.formatWithBean(template, p));
    }

    Person newBean() {
        Person p = new Person();
        p.setId("id-1231231231");
        p.setAge(12);
        p.setName("zhangsan");
        p.setDesc("Hello, I'm from china");
        return p;
    }

    Map<String, Object> newMap() {
        return newMap("");
    }

    Map<String, Object> newMap(String keyPrefix) {
        Map<String, Object> map = Collects.emptyHashMap();
        map.put(keyPrefix + "id", "id-1231231231");
        map.put(keyPrefix + "age", "12");
        map.put(keyPrefix + "name", "zhangsan");
        map.put(keyPrefix + "desc", "Hello, I'm from china");
        return map;
    }

    @Test
    public void testMapTemplate() {
        Map<String, Object> map = newMap();
        String template = "ID: ${id}, name: ${name}, age: ${age}, xxxx: ${xxxx}, desc: ${desc}";
        System.out.println(StringTemplates.formatWithMap(template, map));
    }

    @Test
    public void testTemplateChain() {
        Person p = newBean();
        Map<String, Object> map = newMap("x");
        StringBuilder template = new StringBuilder()
                .append("1.\tid: {0}, name: {1} \n")
                .append("2.\tID: ${id}, name: ${name}, age: ${age}, xxxx: ${xxxx}, desc: ${desc}\n")
                .append("3.\tid: {}, name: {}\n")
                .append("4.\tID: ${xid}, name: ${xname}, age: ${xage}, xxxx: ${xxxx}, desc: ${xdesc}");
        StringTemplateFormatterChain chain = new StringTemplateFormatterChain()
                .addIndexedFormatterAndParameters(p.getId(), p.getName())
                .addPlaceHolderFormatterAndParameters(p.getId(), p.getName())
                .addMapBasedFormatterAndParameters(map)
                .addBeanBasedFormatterAndParameters(p);
        System.out.println(chain.format(template.toString()));

    }

    @Test
    public void testTemplateFluent() {
        Person p = newBean();
        Map<String, Object> map = newMap("x");
        StringBuilder template = new StringBuilder()
                .append("1.\tid: {0}, name: {1} \n")
                .append("2.\tID: ${id}, name: ${name}, age: ${age}, xxxx: ${xxxx}, desc: ${desc}\n")
                .append("3.\tid: {}, name: {}\n")
                .append("4.\tID: ${xid}, name: ${xname}, age: ${xage}, xxxx: ${xxxx}, desc: ${xdesc}");
        System.out.println(StringTemplates.fluenter(template.toString())
                .formatWithIndex(p.getId(), p.getName())
                .formatWithPlaceHolder(p.getId(), p.getName())
                .formatWithMap(map)
                .formatWithBean(p).get());

    }

    @Test
    public void testTemplateExtractVariable() {
        final Person p = newBean();
        String template = "ID: ${id}, name: ${name}, age: ${age}, desc: ${desc}, xxxx: ${xxxx}";
        System.out.println(StringTemplates.format(template, "${", "}",
                new Function2<String, Object[], String>() {
                    @Override
                    public String apply(String variable, Object[] args) {
                        if ("id".equals(variable)) {
                            return p.getId();
                        }
                        if ("name".equals(variable)) {
                            return p.getName();
                        }
                        if ("age".equals(variable)) {
                            return "" + p.getAge();
                        }
                        if ("desc".equals(variable)) {
                            return p.getDesc();
                        }
                        return "${" + variable + "}";
                    }
                }));
    }

}
