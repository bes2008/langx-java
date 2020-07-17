package com.jn.langx.test.text.xml;

import com.jn.langx.io.resource.Resource;
import com.jn.langx.io.resource.Resources;
import com.jn.langx.text.xml.XmlAccessor;
import com.jn.langx.text.xml.Xmls;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Consumer;
import com.jn.langx.util.io.IOs;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.xpath.XPathFactory;
import java.io.InputStream;
import java.util.List;

public class XmlParserTests {
    public static void main(String[] args) throws Throwable {

        //, "/xmls/antlr-2.7.7.pom", "/xmls/servlet-api-2.5.pom"
        List<String> poms = Collects.newArrayList("/xmls/asm-parent-3.3.1.pom");
        Collects.forEach(poms, new Consumer<String>() {
            @Override
            public void accept(String s) {
                InputStream inputStream = null;
                try {
                    Resource resource = Resources.loadClassPathResource(s);
                    inputStream = resource.getInputStream();
                    Document document = Xmls.getXmlDoc(null, inputStream);
                    System.out.println(document.getDocumentElement().getNamespaceURI());

                    String xpathExpressionString = "/x:project/x:groupId";
                    Element element = new XmlAccessor("x").getElement(document, XPathFactory.newInstance(), xpathExpressionString);
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
