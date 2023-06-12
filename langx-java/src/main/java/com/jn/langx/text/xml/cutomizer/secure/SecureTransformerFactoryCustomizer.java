package com.jn.langx.text.xml.cutomizer.secure;

import com.jn.langx.text.xml.Xmls;
import com.jn.langx.text.xml.cutomizer.TransformerFactoryCustomizer;

import javax.xml.XMLConstants;
import javax.xml.transform.TransformerFactory;

public class SecureTransformerFactoryCustomizer implements TransformerFactoryCustomizer {
    public static SecureTransformerFactoryCustomizer DEFAULT = new SecureTransformerFactoryCustomizer();

    @Override
    public void customize(TransformerFactory factory) {
        Xmls.setAttribute(factory, XMLConstants.ACCESS_EXTERNAL_DTD, false);
    }
}
