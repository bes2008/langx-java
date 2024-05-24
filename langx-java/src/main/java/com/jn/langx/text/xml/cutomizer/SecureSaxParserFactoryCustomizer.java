package com.jn.langx.text.xml.cutomizer;

import com.jn.langx.Customizer;
import com.jn.langx.text.xml.Xmls;

import javax.xml.parsers.SAXParserFactory;

/**
 * @since 5.2.9
 */
public class SecureSaxParserFactoryCustomizer implements Customizer<SAXParserFactory> {

    @Override
    public void customize(SAXParserFactory factory) {
        Xmls.setFeature(factory, "http://apache.org/xml/features/disallow-doctype-decl", true);

        Xmls.setFeature(factory, "http://xml.org/sax/features/external-general-entities", false);
        Xmls.setFeature(factory, "http://xml.org/sax/features/external-parameter-entities", false);
    }
}
