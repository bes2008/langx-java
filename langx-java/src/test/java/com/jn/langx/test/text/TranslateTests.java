package com.jn.langx.test.text;

import com.jn.langx.io.resource.Resource;
import com.jn.langx.io.resource.Resources;
import com.jn.langx.text.translate.StringEscapes;
import com.jn.langx.text.xml.XmlEscapers;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Consumer;
import com.jn.langx.util.io.IOs;
import org.junit.Assert;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;

public class TranslateTests {
    @Test
    public void xmlContentEscapeTest() {
        String content = "hello world :  5 > 3 ? true or false";
        String c1 = XmlEscapers.xmlContentEscaper().escape(content);
        System.out.println(c1);

        String c2 = StringEscapes.escapeXml(content);
        System.out.println(c2);

        String content2 = StringEscapes.unescapeXml(c2);
        System.out.println(content2);
        Assert.assertEquals(content, content2);

        List<String> poms = Collects.newArrayList(
                 "/xmls/asm-parent-3.3.1.pom",
                 "/xmls/antlr-2.7.7.pom",
                 "/xmls/servlet-api-2.5.pom",
                 "/xmls/beanshell-2.0b4.pom",
                "/xmls/langx-java.5.0.3.pom"
                ,
                 "/xmls/bsh-2.0b4.pom"
        );
        Collects.forEach(poms, new Consumer<String>() {
            @Override
            public void accept(String s) {
                InputStream inputStream = null;
                try {
                    Resource resource = Resources.loadClassPathResource(s);
                    inputStream = resource.getInputStream();
                    String xml = IOs.readAsString(inputStream);
                    System.out.println(xml);
                    System.out.println("\n\n\n==============escaped=================\n\n");
                    String x2 = StringEscapes.escapeXml(xml);
                    System.out.println(x2);
                    System.out.println("\n\n\n==============unescaped=================\n\n");
                    String xml2 =  StringEscapes.unescapeXml(x2);
                    System.out.println(xml2);

                    Assert.assertEquals(xml, xml2);
                } catch (Throwable ex) {
                    ex.printStackTrace();
                } finally {
                    IOs.close(inputStream);
                }
            }
        });

    }


}
