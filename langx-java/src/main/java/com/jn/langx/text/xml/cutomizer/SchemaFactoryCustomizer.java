package com.jn.langx.text.xml.cutomizer;

import com.jn.langx.Customizer;

import javax.xml.validation.SchemaFactory;

/**
 * @since 5.2.9
 */
public interface SchemaFactoryCustomizer extends Customizer<SchemaFactory> {
    @Override
    void customize(SchemaFactory factory);
}
