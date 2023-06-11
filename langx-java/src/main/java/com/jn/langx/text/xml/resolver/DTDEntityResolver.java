package com.jn.langx.text.xml.resolver;

import com.jn.langx.util.io.Charsets;
import com.jn.langx.util.io.IOs;
import com.jn.langx.util.logging.Loggers;
import org.slf4j.Logger;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.*;

public class DTDEntityResolver implements EntityResolver {
    private static final Logger logger = Loggers.getLogger(DTDEntityResolver.class);
    private StringReader dtdReader;

    public DTDEntityResolver(final InputStream dtdInputStream) {
        setDtdInputStream(dtdInputStream);
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
                FileInputStream fis = new FileInputStream(file);
                setDtdInputStream(fis);
            }
            if (this.dtdReader != null) {
                return new InputSource(this.dtdReader);
            }
            source = new InputSource(new ByteArrayInputStream("<?xml version=\"1.0\" encoding=\"UTF-8\"?>".getBytes(Charsets.UTF_8)));
        } catch (Exception ex) {
        }
        return source;
    }

    void setDtdInputStream(final InputStream dtdInputStream) {
        String content = null;
        try {
            content = IOs.readAsString(dtdInputStream);
            StringReader reader = new StringReader(content);
            this.dtdReader = reader;
        } catch (Throwable e) {
            logger.error(e.getMessage(), e);
        } finally {
            IOs.close(dtdInputStream);
        }

    }
}