package com.jn.langx.text.xml.cutomizer;

import com.jn.langx.Customizer;
import com.jn.langx.text.xml.Xmls;

import javax.xml.parsers.DocumentBuilderFactory;

/**
 * @since 5.2.9
 */
public class SecureDocumentBuilderFactoryCustomizer implements Customizer<DocumentBuilderFactory> {
    @Override
    public void customize(DocumentBuilderFactory factory) {

        Xmls.setFeature(factory, "http://xml.org/sax/features/external-general-entities", false); // 不包括外部一般实体。
        Xmls.setFeature(factory, "http://xml.org/sax/features/external-parameter-entities", false); // 不包含外部参数实体或外部DTD子集。
        Xmls.setFeature(factory, "http://apache.org/xml/features/nonvalidating/load-external-dtd", false); // 忽略外部DTD
        Xmls.setFeature(factory, "http://javax.xml.XMLConstants/property/accessExternalDTD", false); // 不访问外部 dtd
        Xmls.setFeature(factory, "http://javax.xml.XMLConstants/property/accessExternalSchema", false); // 不访问外部 schema
        Xmls.setFeature(factory, "http://javax.xml.XMLConstants/feature/secure-processing", true);


        // 设置 XInclude 处理的状态为false,禁止实体扩展引用
        factory.setXIncludeAware(false);
        factory.setExpandEntityReferences(false);
    }
}
