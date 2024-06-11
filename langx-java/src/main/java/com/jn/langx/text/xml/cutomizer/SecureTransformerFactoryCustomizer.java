package com.jn.langx.text.xml.cutomizer;

import com.jn.langx.text.xml.Xmls;

import javax.xml.transform.TransformerFactory;

public class SecureTransformerFactoryCustomizer implements TransformerFactoryCustomizer{
    @Override
    public void customize(TransformerFactory factory) {
        Xmls.securedTransformerFactory(factory);
    }
}
