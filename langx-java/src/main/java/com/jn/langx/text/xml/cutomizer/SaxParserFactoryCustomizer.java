package com.jn.langx.text.xml.cutomizer;

import com.jn.langx.Customizer;

import javax.xml.parsers.SAXParserFactory;

public interface SaxParserFactoryCustomizer extends Customizer<SAXParserFactory> {
    @Override
    void customize(SAXParserFactory factory);
}
