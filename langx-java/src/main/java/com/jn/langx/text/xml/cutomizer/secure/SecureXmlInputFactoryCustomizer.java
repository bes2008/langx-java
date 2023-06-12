package com.jn.langx.text.xml.cutomizer.secure;

import com.jn.langx.text.xml.Xmls;
import com.jn.langx.text.xml.cutomizer.XMLInputFactoryCustomizer;

import javax.xml.XMLConstants;
import javax.xml.stream.XMLInputFactory;

public class SecureXmlInputFactoryCustomizer implements XMLInputFactoryCustomizer {
    public static final SecureXmlInputFactoryCustomizer DEFAULT = new SecureXmlInputFactoryCustomizer();

    @Override
    public void customize(XMLInputFactory factory) {
        Xmls.setProperty(factory, XMLInputFactory.SUPPORT_DTD, false);
        Xmls.setProperty(factory, XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, Boolean.FALSE);
        Xmls.setProperty(factory, XMLConstants.ACCESS_EXTERNAL_DTD, false);
        Xmls.setProperty(factory, XMLConstants.ACCESS_EXTERNAL_SCHEMA, false);
    }
}
