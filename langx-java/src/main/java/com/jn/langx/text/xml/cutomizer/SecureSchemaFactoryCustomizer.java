package com.jn.langx.text.xml.cutomizer;

import com.jn.langx.text.xml.Xmls;

import javax.xml.validation.SchemaFactory;

/**
 * @since 5.2.9
 */
public class SecureSchemaFactoryCustomizer implements SchemaFactoryCustomizer {

    @Override
    public void customize(SchemaFactory factory) {
        Xmls.securedSchemaFactory(factory);
    }
}
