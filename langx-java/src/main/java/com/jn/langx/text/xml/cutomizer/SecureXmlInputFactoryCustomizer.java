package com.jn.langx.text.xml.cutomizer;

import com.jn.langx.Customizer;
import com.jn.langx.text.xml.Xmls;

import javax.xml.stream.XMLInputFactory;

/**
 * @since 5.2.9
 */
public class SecureXmlInputFactoryCustomizer implements Customizer<XMLInputFactory> {

    public void customize(XMLInputFactory factory) {
        Xmls.setProperty(factory, XMLInputFactory.SUPPORT_DTD, false);
        Xmls.setProperty(factory, XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, Boolean.FALSE);
        Xmls.setProperty(factory, "http://javax.xml.XMLConstants/property/accessExternalDTD", false);
        Xmls.setProperty(factory, "http://javax.xml.XMLConstants/property/accessExternalSchema", false);
    }
}
