package com.jn.langx.text.xml.cutomizer;

import com.jn.langx.Customizer;

import javax.xml.stream.XMLInputFactory;

/**
 * @since 5.2.9
 */
public interface XMLInputFactoryCustomizer extends Customizer<XMLInputFactory> {
    @Override
    void customize(XMLInputFactory factory);
}
