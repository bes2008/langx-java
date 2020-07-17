package com.jn.langx.text.xml.errorhandler;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class RaiseErrorHandler implements org.xml.sax.ErrorHandler {
    @Override
    public void warning(final SAXParseException exception) throws SAXException {
        throw exception;
    }

    @Override
    public void error(final SAXParseException exception) throws SAXException {
        throw exception;
    }

    @Override
    public void fatalError(final SAXParseException exception) throws SAXException {
        throw exception;
    }
}