package com.jn.langx.text.xml.cutomizer.secure;

import com.jn.langx.text.xml.Xmls;
import com.jn.langx.text.xml.cutomizer.SaxParserFactoryCustomizer;

import javax.xml.parsers.SAXParserFactory;

public class SecureSaxParserFactoryCustomizer implements SaxParserFactoryCustomizer {
    public static final SecureSaxParserFactoryCustomizer DEFAULT = new SecureSaxParserFactoryCustomizer();

    @Override
    public void customize(SAXParserFactory factory) {
        Xmls.setFeature(factory, "http://apache.org/xml/features/disallow-doctype-decl", true);

        Xmls.setFeature(factory, "http://xml.org/sax/features/external-general-entities", false);
        Xmls.setFeature(factory, "http://xml.org/sax/features/external-parameter-entities", false);
    }
}
