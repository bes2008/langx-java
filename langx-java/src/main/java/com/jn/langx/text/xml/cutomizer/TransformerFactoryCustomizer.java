package com.jn.langx.text.xml.cutomizer;

import com.jn.langx.Customizer;

import javax.xml.transform.TransformerFactory;

/**
 * @since 5.2.9
 */
public interface TransformerFactoryCustomizer extends Customizer<TransformerFactory> {
    @Override
    void customize(TransformerFactory factory);
}
