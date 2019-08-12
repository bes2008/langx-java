package com.jn.langx.util.xml;

import org.w3c.dom.Document;

public interface XmlDocumentHandler<T> {
    T handle(final Document doc) throws Exception;
}
