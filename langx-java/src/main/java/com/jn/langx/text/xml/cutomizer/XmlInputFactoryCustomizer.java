package com.jn.langx.text.xml.cutomizer;

import com.jn.langx.Customizer;

import javax.xml.stream.XMLInputFactory;

public interface XmlInputFactoryCustomizer extends Customizer<XMLInputFactory> {
    @Override
    void customize(XMLInputFactory target);
}
