package com.jn.langx.text.xml.errorhandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class LogErrorHandler implements org.xml.sax.ErrorHandler {
    private static final Logger logger = LoggerFactory.getLogger(LogErrorHandler.class);

    @Override
    public void warning(final SAXParseException exception) throws SAXException {
        logger.warn("parse fail", exception);
    }

    @Override
    public void error(final SAXParseException exception) throws SAXException {
        logger.error("parse fail", exception);
    }

    @Override
    public void fatalError(final SAXParseException exception) throws SAXException {
        logger.error("parse fail", exception);
    }
}