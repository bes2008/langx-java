package com.jn.langx.text.xml.cutomizer;

import com.jn.langx.text.xml.Xmls;

import javax.xml.parsers.SAXParserFactory;

/**
 * @since 5.2.9
 */
public class SecureSaxParserFactoryCustomizer implements SaxParserFactoryCustomizer {

    @Override
    public void customize(SAXParserFactory factory) {
        Xmls.securedSAXParserFactory(factory);
    }
}
