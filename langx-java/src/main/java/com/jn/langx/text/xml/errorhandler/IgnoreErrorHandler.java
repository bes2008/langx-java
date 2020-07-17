package com.jn.langx.text.xml.errorhandler;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class IgnoreErrorHandler implements org.xml.sax.ErrorHandler {
    @Override
    public void warning(final SAXParseException exception) throws SAXException {
        // ignore it
    }

    @Override
    public void error(final SAXParseException exception) throws SAXException {
        // ignore it
    }

    @Override
    public void fatalError(final SAXParseException exception) throws SAXException {
        // ignore it
    }
}