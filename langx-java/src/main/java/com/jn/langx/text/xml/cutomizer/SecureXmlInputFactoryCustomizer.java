package com.jn.langx.text.xml.cutomizer;

import com.jn.langx.text.xml.Xmls;

import javax.xml.stream.XMLInputFactory;

/**
 * @since 5.2.9
 */
public class SecureXmlInputFactoryCustomizer implements XmlInputFactoryCustomizer {

    public void customize(XMLInputFactory factory) {
        Xmls.securedXmlInputFactory(factory);
    }
}
