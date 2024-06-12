package com.jn.langx.text.xml;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.text.StringTemplates;
import com.jn.langx.text.xml.cutomizer.DocumentBuilderFactoryCustomizer;
import com.jn.langx.text.xml.cutomizer.TransformerFactoryCustomizer;
import com.jn.langx.text.xml.errorhandler.RaiseErrorHandler;
import com.jn.langx.text.xml.resolver.DTDEntityResolver;
import com.jn.langx.text.xml.resolver.NullEntityResolver;
import com.jn.langx.util.Throwables;
import com.jn.langx.util.io.Charsets;
import com.jn.langx.util.io.IOs;
import com.jn.langx.util.io.file.Files;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.stream.XMLInputFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.validation.SchemaFactory;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathFactoryConfigurationException;
import java.io.*;

public class Xmls {
    public static final String NULL_XML_STR = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

    /**
     * @since 5.3.12
     *
     * @see <a href="https://blog.spoock.com/2018/10/23/java-xxe/" >java-xxe</a>
     */
    public static class SecuredPropertyNames{
        private SecuredPropertyNames(){

        }

        public static final String XML_PROPERTY_ACCESS_EXTERNAL_DTD = "http://javax.xml.XMLConstants/property/accessExternalDTD";

        public static final String XML_PROPERTY_ACCESS_EXTERNAL_SCHEMA = "http://javax.xml.XMLConstants/property/accessExternalSchema";

        public static final String XML_PROPERTY_ACCESS_EXTERNAL_STYLESHEET = "http://javax.xml.XMLConstants/property/accessExternalStylesheet";

        public static final String XML_FEATURE_SECURE_PROCESSING="http://javax.xml.XMLConstants/feature/secure-processing";
        public static final String SAX_FEATURE_EXTERNAL_GENERAL_ENTITIES="http://xml.org/sax/features/external-general-entities";
        public static final String SAX_FEATURE_EXTERNAL_PARAMETER_ENTITIES="http://xml.org/sax/features/external-parameter-entities";
        public static final String APACHE_XML_FEATURE_NO_VALIDATING_LOAD_EXTERNAL_DTD="http://apache.org/xml/features/nonvalidating/load-external-dtd";
        public static final String APACHE_XML_FEATURE_DISALLOW_DOCTYPE_DECL= "http://apache.org/xml/features/disallow-doctype-decl";
        public static final String APACHE_XML_FEATURE_VALIDATION="http://apache.org/xml/features/validation";
        public static final String APACHE_XML_FEATURE_VALIDATION_SCHEMA="http://apache.org/xml/features/validation/schema";
    }

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


    public static Document getXmlDoc(
            EntityResolver entityResolver,
            ErrorHandler errorHandler,
            final InputStream xml,
            boolean ignoreComments,
            boolean ignoringElementContentWhitespace,
            boolean namespaceAware
    ) throws Exception {
        return Xmls.getXmlDoc(entityResolver, errorHandler, xml, ignoreComments, ignoringElementContentWhitespace, namespaceAware, null);
    }

    /**
     * @since 5.2.9
     */
    public static Document getXmlDoc(
            EntityResolver entityResolver,
            ErrorHandler errorHandler,
            final InputStream xml,
            boolean ignoreComments,
            boolean ignoringElementContentWhitespace,
            boolean namespaceAware,
            DocumentBuilderFactoryCustomizer customizer
    ) throws Exception {
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setIgnoringComments(ignoreComments);
        factory.setIgnoringElementContentWhitespace(ignoringElementContentWhitespace);
        factory.setNamespaceAware(namespaceAware);
        boolean validation=entityResolver != null;
        factory.setValidating(validation);

        securedDocumentBuilderFactory(factory,validation);
        if(customizer!=null) {
            customizer.customize(factory);
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
            throw new FileNotFoundException(StringTemplates.formatWithPlaceholder("File '{}' does not exist .", xmlfilepathOrURI));
        }
        FileInputStream fis = null;
        try {
            fis = Files.openInputStream(file);
            return getXmlDoc(new DTDEntityResolver(dtdInputStream), fis);
        } finally {
            IOs.close(fis);
        }
    }

    public static void writeDocToOutputStream(final Document doc, final OutputStream out) throws Exception {
        final Transformer transformer = newTransformer();
        transformer.transform(new DOMSource(doc), new StreamResult(out));
    }


    public static void writeDocToFile(final Document doc, final File file) throws TransformerFactoryConfigurationError, TransformerException {
        Transformer transformer = newTransformer();
        transformer.transform(new DOMSource(doc), new StreamResult(file));
    }

    /**
     * @since 5.2.9
     */
    public static Transformer newTransformer(@NonNull TransformerFactoryCustomizer customizer) throws TransformerConfigurationException {
        TransformerFactory factory = TransformerFactory.newInstance();
        securedTransformerFactory(factory);
        if(customizer!=null) {
            customizer.customize(factory);
        }
        return factory.newTransformer();
    }

    public static void securedDocumentBuilderFactory(DocumentBuilderFactory factory, boolean validation){
        Xmls.setFeature(factory,SecuredPropertyNames.XML_FEATURE_SECURE_PROCESSING, true);
        Xmls.setFeature(factory,SecuredPropertyNames.APACHE_XML_FEATURE_DISALLOW_DOCTYPE_DECL,true);
        Xmls.setFeature(factory,SecuredPropertyNames.SAX_FEATURE_EXTERNAL_GENERAL_ENTITIES, false); // 不包括外部一般实体。
        Xmls.setFeature(factory,SecuredPropertyNames.SAX_FEATURE_EXTERNAL_PARAMETER_ENTITIES, false); // 不包含外部参数实体或外部DTD子集。
        Xmls.setFeature(factory,SecuredPropertyNames.APACHE_XML_FEATURE_NO_VALIDATING_LOAD_EXTERNAL_DTD , false); // 忽略外部DTD


        Xmls.setFeature(factory, SecuredPropertyNames.APACHE_XML_FEATURE_VALIDATION, validation);
        Xmls.setFeature(factory,SecuredPropertyNames.APACHE_XML_FEATURE_VALIDATION_SCHEMA, validation);

        Xmls.setFeature(factory,SecuredPropertyNames.XML_PROPERTY_ACCESS_EXTERNAL_DTD, false); // 不访问外部 dtd
        Xmls.setFeature(factory,SecuredPropertyNames.XML_PROPERTY_ACCESS_EXTERNAL_SCHEMA, false); // 不访问外部 schema

        // 设置 XInclude 处理的状态为false,禁止实体扩展引用
        factory.setXIncludeAware(false);
        factory.setExpandEntityReferences(false);
    }

    public static void securedTransformerFactory(TransformerFactory factory){
        factory.setAttribute(SecuredPropertyNames.XML_PROPERTY_ACCESS_EXTERNAL_DTD, "");
        factory.setAttribute(SecuredPropertyNames.XML_PROPERTY_ACCESS_EXTERNAL_STYLESHEET, "");
        factory.setAttribute(SecuredPropertyNames.XML_FEATURE_SECURE_PROCESSING, true);
    }

    public static void securedXmlInputFactory(XMLInputFactory factory){
        factory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
        factory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, Boolean.FALSE);
        factory.setProperty(SecuredPropertyNames.XML_PROPERTY_ACCESS_EXTERNAL_DTD, "");
        factory.setProperty(SecuredPropertyNames.XML_PROPERTY_ACCESS_EXTERNAL_SCHEMA, "");
    }

    public static void securedSchemaFactory(SchemaFactory factory) {
        Xmls.setFeature(factory,SecuredPropertyNames.APACHE_XML_FEATURE_DISALLOW_DOCTYPE_DECL, true);
        Xmls.setFeature(factory,SecuredPropertyNames.XML_PROPERTY_ACCESS_EXTERNAL_DTD, false);
        Xmls.setFeature(factory,SecuredPropertyNames.XML_PROPERTY_ACCESS_EXTERNAL_SCHEMA, false);
    }

    public static void securedSAXParserFactory(SAXParserFactory factory){
        Xmls.setFeature(factory, SecuredPropertyNames.APACHE_XML_FEATURE_DISALLOW_DOCTYPE_DECL, true);

        Xmls.setFeature(factory, SecuredPropertyNames.SAX_FEATURE_EXTERNAL_GENERAL_ENTITIES, false);
        Xmls.setFeature(factory, SecuredPropertyNames.SAX_FEATURE_EXTERNAL_PARAMETER_ENTITIES, false);
        Xmls.setFeature(factory, SecuredPropertyNames.APACHE_XML_FEATURE_NO_VALIDATING_LOAD_EXTERNAL_DTD, false);

    }

    public static Transformer newTransformer() throws TransformerConfigurationException {
        return newTransformer(null);
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

    /**
     * @since 5.2.9
     */
    public static NodeList findNodeList(Document doc, String xpath) throws XPathExpressionException {
        NodeList nodes = new XmlAccessor(Namespaces.hasCustomNamespace(doc) ? "x" : null).getNodeList(doc, XPathFactory.newInstance(), xpath);
        return nodes;
    }

    /**
     * @since 5.2.9
     */
    public static Element findElement(Document doc, String xpath) throws XPathExpressionException {
        Element element = new XmlAccessor(Namespaces.hasCustomNamespace(doc) ? "x" : null).getElement(doc, XPathFactory.newInstance(), xpath);
        return element;
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

    /**
     * @since 5.2.9
     */
    public static void setAttribute(DocumentBuilderFactory factory, String attribute, Object value) {
        try {
            factory.setAttribute(attribute, value);
        } catch (IllegalArgumentException e) {
            // ignore it
        }
    }


    /**
     * @since 5.2.9
     */
    public static void setFeature(SAXParserFactory factory, String feature, boolean enabled) {
        try {
            factory.setFeature(feature, enabled);
        } catch (Exception e) {
            // ignore it
        }
    }

    /**
     * @since 5.2.9
     */
    public static void setProperty(XMLInputFactory factory, String feature, Object value) {
        try {
            factory.setProperty(feature, value);
        } catch (IllegalArgumentException e) {
            // ignore it
        }
    }

    /**
     * @since 5.2.9
     */
    public static void setAttribute(TransformerFactory factory, String feature, Object value) {
        try {
            factory.setAttribute(feature, value);
        } catch (IllegalArgumentException e) {
            // ignore it
        }
    }

    /**
     * @since 5.2.9
     */
    public static void setFeature(TransformerFactory factory, String feature, boolean enabled) {
        try {
            factory.setFeature(feature, enabled);
        } catch (TransformerConfigurationException e) {
            // ignore it
        }
    }

    /**
     * @since 5.2.9
     */
    public static void setProperty(SchemaFactory factory, String feature, Object value) {
        try {
            factory.setProperty(feature, value);
        } catch (Exception e) {
            // ignore it
        }
    }

    /**
     * @since 5.2.9
     */
    public static void setFeature(SchemaFactory factory, String feature, boolean enabled) {
        try {
            factory.setFeature(feature, enabled);
        } catch (Exception e) {
            // ignore it
        }
    }

    /**
     * @since 5.2.9
     */
    public static void setFeature(XPathFactory xpathFactory, String feature, boolean enabled) {
        try {
            xpathFactory.setFeature(feature, enabled);
        } catch (XPathFactoryConfigurationException e) {
            // ignore it
        }
    }

}
