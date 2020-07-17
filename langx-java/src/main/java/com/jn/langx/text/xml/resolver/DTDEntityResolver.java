package com.jn.langx.text.xml.resolver;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.*;

public class DTDEntityResolver implements EntityResolver {
    private InputStream dtdInputStream;

    public DTDEntityResolver(final InputStream dtdInputStream) {
        this.dtdInputStream = dtdInputStream;
    }

    @Override
    public InputSource resolveEntity(final String publicId, final String systemId) throws SAXException, IOException {
        InputSource source = null;
        try {
            File file = null;
            if (systemId != null && !systemId.isEmpty()) {
                file = new File(systemId);
            } else if (publicId != null && !publicId.isEmpty()) {
                file = new File(publicId);
            }
            if (file != null && file.exists()) {
                source = new InputSource(new FileInputStream(file));
            }
            if (source == null) {
                if (this.dtdInputStream != null) {
                    return new InputSource(this.dtdInputStream);
                }
                source = new InputSource(new ByteArrayInputStream("<?xml version=\"1.0\" encoding=\"UTF-8\"?>".getBytes()));
            }
        } catch (Exception ex) {
        }
        return source;
    }
}