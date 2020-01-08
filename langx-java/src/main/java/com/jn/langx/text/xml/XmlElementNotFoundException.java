package com.jn.langx.text.xml;

public class XmlElementNotFoundException extends RuntimeException {
    public XmlElementNotFoundException() {
        super();
    }

    public XmlElementNotFoundException(String message) {
        super(message);
    }

    public XmlElementNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public XmlElementNotFoundException(Throwable cause) {
        super(cause);
    }
}
