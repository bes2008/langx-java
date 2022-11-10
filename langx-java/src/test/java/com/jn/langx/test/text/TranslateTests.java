package com.jn.langx.test.text;

import com.jn.langx.text.translate.StringEscapes;
import com.jn.langx.text.xml.XmlEscapers;
import org.junit.Assert;
import org.junit.Test;

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
    }


}
