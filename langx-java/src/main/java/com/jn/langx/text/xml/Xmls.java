package com.jn.langx.text.xml;

import com.jn.langx.text.xml.errorhandler.RaiseErrorHandler;
import com.jn.langx.text.xml.resolver.DTDEntityResolver;
import com.jn.langx.text.xml.resolver.NullEntityResolver;
import com.jn.langx.util.Throwables;
import com.jn.langx.util.io.Charsets;
import com.jn.langx.util.io.IOs;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.*;

public class Xmls {
    public static final String NULL_XML_STR = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

    private Xmls() {
    }

    public static Document getXmlDoc(InputStream xml) throws Exception {
        return getXmlDoc(null, xml);
    }

    public static Document getXmlDoc(EntityResolver entityResolver, final InputStream xml) throws Exception {
        return getXmlDoc(entityResolver, null, xml);
    }

    public static Document getXmlDoc(EntityResolver entityResolver, ErrorHandler errorHandler, final InputStream xml) throws Exception {
        return getXmlDoc(entityResolver, errorHandler, xml, true);
    }

    public static Document getXmlDoc(EntityResolver entityResolver, ErrorHandler errorHandler, final InputStream xml, boolean namespaceAware) throws Exception {
        return getXmlDoc(entityResolver, errorHandler, xml, false, false, namespaceAware);
    }

    /**
     * @since 5.2.7
     */
    public static void setFeature(DocumentBuilderFactory factory, String feature, boolean enabled) {
        try {
            factory.setFeature(feature, enabled);
        } catch (ParserConfigurationException e) {
            // ignore it
        }
    }


    public static Document getXmlDoc(
            EntityResolver entityResolver,
            ErrorHandler errorHandler,
            final InputStream xml,
            boolean ignoreComments,
            boolean ignoringElementContentWhitespace,
            boolean namespaceAware
    ) throws Exception {
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setIgnoringComments(ignoreComments);

        setFeature(factory, "http://xml.org/sax/features/external-general-entities", false); // 不包括外部一般实体。
        setFeature(factory, "http://xml.org/sax/features/external-parameter-entities", false); // 不包含外部参数实体或外部DTD子集。
        setFeature(factory, "http://apache.org/xml/features/nonvalidating/load-external-dtd", false); // 忽略外部DTD
        setFeature(factory, XMLConstants.ACCESS_EXTERNAL_DTD, false); // 不访问外部 dtd
        setFeature(factory, XMLConstants.ACCESS_EXTERNAL_SCHEMA, false); // 不访问外部 schema
        setFeature(factory,"http://javax.xml.XMLConstants/feature/secure-processing", true);

        // 设置 XInclude 处理的状态为false,禁止实体扩展引用
        factory.setXIncludeAware(false);
        factory.setExpandEntityReferences(false);
        factory.setIgnoringElementContentWhitespace(ignoringElementContentWhitespace);
        factory.setNamespaceAware(namespaceAware);
        if (entityResolver != null) {
            factory.setValidating(true);
        }
        final DocumentBuilder builder = factory.newDocumentBuilder();
        entityResolver = ((entityResolver == null) ? new NullEntityResolver() : entityResolver);
        builder.setEntityResolver(entityResolver);
        builder.setErrorHandler(errorHandler == null ? new RaiseErrorHandler() : errorHandler);
        return builder.parse(xml);
    }

    public static Document getXmlDoc(final InputStream dtdInputStream, final String src, final boolean srcIsPath) throws Exception {
        if (srcIsPath) {
            return getXmlDoc(dtdInputStream, src);
        }
        return getXmlDoc(new DTDEntityResolver(dtdInputStream), new ByteArrayInputStream(src.getBytes(Charsets.UTF_8)));
    }

    public static Document getXmlDoc(final InputStream dtdInputStream, final String xmlfilepathOrURI) throws Exception {
        final File file = new File(xmlfilepathOrURI);
        if (!file.exists()) {
            throw new FileNotFoundException("File '" + xmlfilepathOrURI + "' does not exist .");
        }
        return getXmlDoc(new DTDEntityResolver(dtdInputStream), new FileInputStream(file));
    }

    public static void writeDocToOutputStream(final Document doc, final OutputStream out) throws Exception {
        final Transformer transformer = newTransformer();
        transformer.transform(new DOMSource(doc), new StreamResult(out));
    }

    private static final String FEATURE_SECURE_PROCESSING = "http://javax.xml.XMLConstants/feature/secure-processing";

    public static void writeDocToFile(final Document doc, final File file) throws TransformerFactoryConfigurationError, TransformerException {
        Transformer transformer = newTransformer();
        transformer.transform(new DOMSource(doc), new StreamResult(file));
    }

    public static Transformer newTransformer() throws TransformerConfigurationException {
        TransformerFactory factory = TransformerFactory.newInstance();
        factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, factory);
        factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, false);
        factory.setAttribute(FEATURE_SECURE_PROCESSING, true);
        final Transformer trans = factory.newTransformer();
        return trans;
    }

    public static <T> T handleXml(final String xmlpath, final XmlDocumentHandler<T> handler) {
        InputStream input = null;
        Document doc;
        try {
            final File file = new File(xmlpath);
            if (!file.exists()) {
                throw new FileNotFoundException("File '" + xmlpath + "' does not exist .");
            }
            input = new FileInputStream(file);
            doc = getXmlDoc(null, input);
            return handler.handle(doc);
        } catch (Exception ex) {
            throw Throwables.wrapAsRuntimeException(ex);
        } finally {
            IOs.close(input);
        }
    }


    public static NodeList findNodeList(Document doc, String xpath) throws XPathExpressionException {
        NodeList nodes = new XmlAccessor(Namespaces.hasCustomNamespace(doc) ? "x" : null).getNodeList(doc, XPathFactory.newInstance(), xpath);
        return nodes;
    }

    public static Element findElement(Document doc, String xpath) throws XPathExpressionException {
        Element element = new XmlAccessor(Namespaces.hasCustomNamespace(doc) ? "x" : null).getElement(doc, XPathFactory.newInstance(), xpath);
        return element;
    }

}
