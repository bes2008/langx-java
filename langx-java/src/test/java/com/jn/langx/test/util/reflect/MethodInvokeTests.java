package com.jn.langx.test.util.reflect;

import com.jn.langx.text.stringtemplate.StringTemplate;
import com.jn.langx.util.Strings;
import com.jn.langx.util.Throwables;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Consumer2;
import com.jn.langx.util.reflect.Reflects;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

public class MethodInvokeTests {
    @Test
    public void test() {
        Map<String, String> map = Collects.propertiesToStringMap(System.getProperties(), true);
        final String templateString = "{0} : {1}";
        Collects.forEach(map, new Consumer2<String, String>() {
            @Override
            public void accept(String key, String value) {
                Class templateClass = StringTemplate.class;
                Object template = Reflects.newInstance(templateClass, null, null);
                try {
                    //Reflects.invokeAnyMethod(template, "using", new Class[]{String.class}, new Object[]{templateString}, true, true);
                    Reflects.setAnyFieldValue(template, "template", templateString, true, true);
                    String formated = Reflects.invokeAnyMethod(template, "format", new Class[]{Object[].class}, new Object[]{new Object[]{key, value}}, true, true);
                    System.out.println(formated);
                } catch (Throwable ex) {
                    Throwables.log(ex);
                }
            }
        });
    }

    @Test
    public void testStaticMethodInvoke() throws Throwable {
        Assert.assertTrue(Reflects.<Boolean>invokeAnyStaticMethod(Strings.class, "isEmpty", new Class[]{String.class}, new Object[]{""}, true, false));
    }
}
