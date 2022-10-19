package com.jn.langx.security.owasp.xml.xpath;

import com.jn.langx.text.xml.XPathHandler;
import org.owasp.esapi.ESAPI;

public class OwaspXPathEncoder implements XPathHandler {
    @Override
    public String getName() {
        return "owasp-xpath-encoder";
    }

    @Override
    public String apply(String xpath) {
        return ESAPI.encoder().encodeForXPath(xpath);
    }
}
