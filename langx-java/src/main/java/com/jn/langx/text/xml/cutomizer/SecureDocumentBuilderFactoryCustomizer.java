package com.jn.langx.text.xml.cutomizer;

import com.jn.langx.text.xml.Xmls;

import javax.xml.parsers.DocumentBuilderFactory;

/**
 * @since 5.2.9
 */
public class SecureDocumentBuilderFactoryCustomizer implements DocumentBuilderFactoryCustomizer {
    @Override
    public void customize(DocumentBuilderFactory factory) {
        Xmls.securedDocumentBuilderFactory(factory,factory.isValidating());
    }
}
