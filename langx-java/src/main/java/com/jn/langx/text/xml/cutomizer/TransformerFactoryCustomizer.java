package com.jn.langx.text.xml.cutomizer;

import com.jn.langx.Customizer;

import javax.xml.transform.TransformerFactory;

public interface TransformerFactoryCustomizer extends Customizer<TransformerFactory> {
    @Override
    void customize(TransformerFactory target);
}
