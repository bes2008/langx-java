package com.jn.langx.text.xml.cutomizer;

import com.jn.langx.Customizer;

import javax.xml.parsers.DocumentBuilderFactory;

/**
 * @since 5.2.9
 */
public interface DocumentBuilderFactoryCustomizer extends Customizer<DocumentBuilderFactory> {
    @Override
    void customize(DocumentBuilderFactory factory);
}
