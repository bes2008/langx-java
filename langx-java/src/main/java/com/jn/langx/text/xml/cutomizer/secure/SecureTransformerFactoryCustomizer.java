package com.jn.langx.text.xml.cutomizer.secure;

import com.jn.langx.text.xml.Xmls;
import com.jn.langx.text.xml.cutomizer.TransformerFactoryCustomizer;

import javax.xml.transform.TransformerFactory;

/**
 * @since 5.2.9
 */
public class SecureTransformerFactoryCustomizer implements TransformerFactoryCustomizer {
    public static SecureTransformerFactoryCustomizer DEFAULT = new SecureTransformerFactoryCustomizer();

    @Override
    public void customize(TransformerFactory factory) {
        Xmls.setAttribute(factory, "http://javax.xml.XMLConstants/property/accessExternalDTD", false);
        Xmls.setAttribute(factory, "http://javax.xml.XMLConstants/property/accessExternalStylesheet", false);
        Xmls.setAttribute(factory, "http://javax.xml.XMLConstants/feature/secure-processing", true);
    }
}
