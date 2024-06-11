package com.jn.langx.text.xml.cutomizer;

import com.jn.langx.Customizer;

import javax.xml.parsers.DocumentBuilderFactory;

public interface DocumentBuilderFactoryCustomizer extends Customizer<DocumentBuilderFactory> {
    @Override
    void customize(DocumentBuilderFactory target);
}
