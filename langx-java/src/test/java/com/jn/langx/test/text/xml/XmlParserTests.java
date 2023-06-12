package com.jn.langx.test.text.xml;

import com.jn.langx.io.resource.Resource;
import com.jn.langx.io.resource.Resources;
import com.jn.langx.text.StringTemplates;
import com.jn.langx.text.xml.Namespaces;
import com.jn.langx.text.xml.XmlAccessor;
import com.jn.langx.text.xml.Xmls;
import com.jn.langx.text.xml.errorhandler.LogErrorHandler;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Consumer;
import com.jn.langx.util.io.IOs;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.xpath.XPathFactory;
import java.io.InputStream;
import java.util.List;

public class XmlParserTests {
    @Test
    public void test() throws Throwable {
        List<String> poms = Collects.newArrayList(
                // "/xmls/asm-parent-3.3.1.pom",
                // "/xmls/antlr-2.7.7.pom",
                // "/xmls/servlet-api-2.5.pom",
                // "/xmls/beanshell-2.0b4.pom",
                "/xmls/langx-java.5.0.3.pom"
                //,
                // "/xmls/bsh-2.0b4.pom"
        );
        Collects.forEach(poms, new Consumer<String>() {
            @Override
            public void accept(String s) {
                InputStream inputStream = null;
                try {
                    Resource resource = Resources.loadClassPathResource(s);
                    inputStream = resource.getInputStream();
                    Document document = Xmls.getXmlDoc(null, new LogErrorHandler(), inputStream);

                    String xpathExpressionString;
                    Element element;


                    boolean usingCustomNamespace = Namespaces.hasCustomNamespace(document, true);
                    if (usingCustomNamespace) {
                        String prefix = document.getDocumentElement().getPrefix();
                        if (prefix == null) {
                            prefix = "x";
                        }
                        xpathExpressionString = StringTemplates.formatWithPlaceholder("/{}:project/{}:groupId", prefix, prefix);
                        element = new XmlAccessor(prefix).getElement(document, XPathFactory.newInstance(), xpathExpressionString);
                    } else {
                        xpathExpressionString = "/project/groupId";
                        element = new XmlAccessor().getElement(document, XPathFactory.newInstance(), xpathExpressionString);
                    }
                    System.out.println(element.getTextContent());
                } catch (Throwable ex) {
                    ex.printStackTrace();
                } finally {
                    IOs.close(inputStream);
                }
            }
        });

    }

}
