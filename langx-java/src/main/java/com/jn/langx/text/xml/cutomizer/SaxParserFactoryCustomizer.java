package com.jn.langx.text.xml.cutomizer;

import com.jn.langx.Customizer;

import javax.xml.parsers.SAXParserFactory;

/**
 * @since 5.2.9
 */
public interface SaxParserFactoryCustomizer extends Customizer<SAXParserFactory> {
    @Override
    void customize(SAXParserFactory factory);
}
