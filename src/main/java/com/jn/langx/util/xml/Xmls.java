package com.jn.langx.util.xml;

import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;

@SuppressWarnings({"unchecked","unused"})
public class Xmls {
    private static final String NULL_XML_STR = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

    public static Document getXmlDoc(EntityResolver entityResolver, final InputStream xml) throws Exception {
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setIgnoringComments(false);
        factory.setIgnoringElementContentWhitespace(false);
        factory.setNamespaceAware(true);
        if (entityResolver != null) {
            factory.setValidating(true);
        }
        final DocumentBuilder builder = factory.newDocumentBuilder();
        entityResolver = ((entityResolver == null) ? new NullEntityResolver() : entityResolver);
        builder.setEntityResolver(entityResolver);
        builder.setErrorHandler(new ErrorHandler());
        return builder.parse(xml);
    }

    public static Document getXmlDoc(final InputStream dtdInputStream, final String src, final boolean srcIsPath) throws Exception {
        if (srcIsPath) {
            return getXmlDoc(dtdInputStream, src);
        }
        return getXmlDoc(new DTDEntityResolver(dtdInputStream), new ByteArrayInputStream(src.getBytes()));
    }

    public static Document getXmlDoc(final InputStream dtdInputStream, final String xmlfilepathOrURI) throws Exception {
        final File file = new File(xmlfilepathOrURI);
        if (!file.exists()) {
            throw new FileNotFoundException("File '" + xmlfilepathOrURI + "' does not exist .");
        }
        return getXmlDoc(new DTDEntityResolver(dtdInputStream), new FileInputStream(file));
    }

    public static void writeDocToOutputStream(final Document doc, final OutputStream out) throws Exception {
        final Transformer trans = TransformerFactory.newInstance().newTransformer();
        trans.transform(new DOMSource(doc), new StreamResult(out));
    }

    public static void writeDocToFile(final Document doc, final File file) throws TransformerFactoryConfigurationError, TransformerException {
        final Transformer trans = TransformerFactory.newInstance().newTransformer();
        trans.transform(new DOMSource(doc), new StreamResult(file));
    }

    public static <T> T handleXml(final String xmlpath, final XmlDocumentHandler<T> handler) throws Exception {
        InputStream input = null;
        Document doc = null;
        try {
            final File file = new File(xmlpath);
            if (!file.exists()) {
                throw new FileNotFoundException("File '" + xmlpath + "' does not exist .");
            }
            input = new FileInputStream(file);
            doc = getXmlDoc(null, input);
            return handler.handle(doc);
        } catch (Exception ex) {
            throw ex;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException ex2) {
                }
            }
        }
    }

    public static class DTDEntityResolver implements EntityResolver {
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

    public static class NullEntityResolver implements EntityResolver {
        @Override
        public InputSource resolveEntity(final String publicId, final String systemId) throws SAXException, IOException {
            return new InputSource(new ByteArrayInputStream("<?xml version=\"1.0\" encoding=\"UTF-8\"?>".getBytes()));
        }
    }

    public static class ErrorHandler implements org.xml.sax.ErrorHandler {
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
}
