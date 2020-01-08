package com.jn.langx.text.xml;

import org.w3c.dom.Document;

public interface XmlDocumentHandler<T> {
    T handle(final Document doc) throws Exception;
}
