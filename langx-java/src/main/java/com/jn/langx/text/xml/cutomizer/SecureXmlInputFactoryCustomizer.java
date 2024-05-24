package com.jn.langx.text.xml.cutomizer;

import com.jn.langx.Customizer;
import com.jn.langx.text.xml.Xmls;

import javax.xml.XMLConstants;
import javax.xml.stream.XMLInputFactory;

/**
 * @since 5.2.9
 */
public class SecureXmlInputFactoryCustomizer implements Customizer<XMLInputFactory> {

    public void customize(XMLInputFactory factory) {
        Xmls.setProperty(factory, XMLInputFactory.SUPPORT_DTD, false);
        Xmls.setProperty(factory, XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, Boolean.FALSE);
        Xmls.setProperty(factory, XMLConstants.ACCESS_EXTERNAL_DTD, false);
        Xmls.setProperty(factory, XMLConstants.ACCESS_EXTERNAL_SCHEMA, false);
    }
}
