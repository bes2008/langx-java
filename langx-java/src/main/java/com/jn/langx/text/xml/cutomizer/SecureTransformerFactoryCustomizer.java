package com.jn.langx.text.xml.cutomizer;

import com.jn.langx.Customizer;
import com.jn.langx.text.xml.Xmls;

import javax.xml.transform.TransformerFactory;

/**
 * @since 5.2.9
 */
public class SecureTransformerFactoryCustomizer implements Customizer<TransformerFactory> {

    @Override
    public void customize(TransformerFactory factory) {
        Xmls.setAttribute(factory, "http://javax.xml.XMLConstants/property/accessExternalDTD", false);
        Xmls.setAttribute(factory, "http://javax.xml.XMLConstants/property/accessExternalStylesheet", false);
        Xmls.setAttribute(factory, "http://javax.xml.XMLConstants/feature/secure-processing", true);
    }
}
