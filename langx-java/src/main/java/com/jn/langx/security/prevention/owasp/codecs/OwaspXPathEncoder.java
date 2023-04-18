package com.jn.langx.security.prevention.owasp.codecs;

import com.jn.langx.text.xml.XPathHandler;

public class OwaspXPathEncoder implements XPathHandler {
    @Override
    public String getName() {
        return "owasp-xpath-encoder";
    }

    private static final char[] IMMUNE_XPATH = {',', '.', '-', '_', ' '};

    private HTMLEntityCodec htmlCodec = new HTMLEntityCodec();

    /**
     *
     * @param xpathParameter 要在 xpath中拼接的参数
     *
     * @see com.jn.langx.text.xml.XPaths
     */
    @Override
    public String transform(String xpathParameter) {
        if (xpathParameter == null) {
            return null;
        }
        return htmlCodec.encode(IMMUNE_XPATH, xpathParameter);
    }
}
