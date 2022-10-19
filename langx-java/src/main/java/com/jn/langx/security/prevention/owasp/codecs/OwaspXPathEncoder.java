package com.jn.langx.security.prevention.owasp.codecs;

import com.jn.langx.text.xml.XPathHandler;

public class OwaspXPathEncoder implements XPathHandler {
    @Override
    public String getName() {
        return "owasp-xpath-encoder";
    }

    private final static char[] IMMUNE_XPATH = {',', '.', '-', '_', ' '};

    private HTMLEntityCodec htmlCodec = new HTMLEntityCodec();

    @Override
    public String apply(String xpath) {
        if (xpath == null) {
            return null;
        }
        return htmlCodec.encode(IMMUNE_XPATH, xpath);
    }
}
