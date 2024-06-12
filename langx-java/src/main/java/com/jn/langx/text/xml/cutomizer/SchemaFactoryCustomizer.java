package com.jn.langx.text.xml.cutomizer;

import com.jn.langx.Customizer;

import javax.xml.validation.SchemaFactory;

public interface SchemaFactoryCustomizer extends Customizer<SchemaFactory> {
    @Override
    void customize(SchemaFactory target);
}
