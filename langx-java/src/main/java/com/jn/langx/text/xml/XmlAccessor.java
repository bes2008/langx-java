package com.jn.langx.text.xml;

import com.jn.langx.util.Emptys;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Consumer;
import com.jn.langx.util.function.Consumer2;
import com.jn.langx.util.logging.Loggers;
import org.slf4j.Logger;
import org.w3c.dom.*;

import javax.xml.xpath.*;
import java.util.Map;

public class XmlAccessor {
    private String defaultNamespacePrefix = null;

    /**
     * XML中没有指定命名空间，或者只有W3C标准命名空间时，用这个
     */
    public XmlAccessor() {

    }

    /**
     * XML中指定了非标准的命名空间时，用这个
     */
    public XmlAccessor(String defaultNamespacePrefix) {
        setDefaultNamespacePrefix(defaultNamespacePrefix);
    }

    public String getDefaultNamespacePrefix() {
        return defaultNamespacePrefix;
    }

    public void setDefaultNamespacePrefix(String defaultNamespacePrefix) {
        this.defaultNamespacePrefix = defaultNamespacePrefix;
    }

    public void setElementAttribute(final Document doc, final XPathFactory factory, final String elementXpath, final String attributeName, final String attributeValue) throws Exception {
        if (Emptys.isEmpty(attributeName)) {
            return;
        }
        Logger logger = Loggers.getLogger(getClass());
        try {
            final Element element = this.getElement(doc, factory, elementXpath);
            if (Emptys.isEmpty(element)) {
                return;
            }
            element.setAttribute(attributeName, attributeValue);
            if (logger.isDebugEnabled()) {
                logger.debug("set attribute {} = {} for element {}", attributeName, attributeValue, elementXpath);
            }
        } catch (Exception ex) {
            logger.error("Error occur when set attribute {} for element {}", attributeName, elementXpath);
            throw ex;
        }
    }


    public void setElementAttributes(final Document doc, final XPathFactory factory, final String elementXpath, final Map<String, String> attrs) throws Exception {
        if (Emptys.isEmpty(attrs)) {
            return;
        }
        final Logger logger = Loggers.getLogger(getClass());
        try {
            final Element element = this.getElement(doc, factory, elementXpath);
            if (Emptys.isEmpty(element)) {
                return;
            }
            Collects.forEach(attrs, new Consumer2<String, String>() {
                @Override
                public void accept(String attributeName, String value) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("set attribute {} = {} for element {}", attributeName, value, elementXpath);
                    }
                    element.setAttribute(attributeName, value);
                }
            });
        } catch (Exception ex) {
            logger.error("Error occur when set attribute for element {}", elementXpath);
            throw ex;
        }
    }

    public void setElementsAttributes(final Document doc, final XPathFactory factory, final String elementXpath, final Map<String, String> attrs) throws Exception {
        if (Emptys.isEmpty(attrs)) {
            return;
        }
        final Logger logger = Loggers.getLogger(getClass());
        try {
            final NodeList elements = this.getNodeList(doc, factory, elementXpath);
            if (Emptys.isEmpty(elements)) {
                return;
            }
            Collects.forEach(Collects.<Element>asIterable(elements), new Consumer<Element>() {
                @Override
                public void accept(final Element element) {
                    Collects.forEach(attrs, new Consumer2<String, String>() {
                        @Override
                        public void accept(String attributeName, String value) {
                            if (logger.isDebugEnabled()) {
                                logger.debug("set attribute {} = {} for element {}", attributeName, attrs.get(attributeName), elementXpath);
                            }
                            element.setAttribute(attributeName, attrs.get(attributeName));
                        }
                    });
                }
            });
        } catch (Exception ex) {
            logger.error("Error occur when set attribute for element {}", elementXpath);
            throw ex;
        }
    }

    public String getElementAttribute(final String xmlFilePath, final String elementXpath, final String attributeName) {
        return Xmls.handleXml(xmlFilePath, new XmlDocumentHandler<String>() {
            @Override
            public String handle(final Document doc) throws Exception {
                final XPathFactory factory = XPathFactory.newInstance();
                return getElementAttribute(doc, factory, elementXpath, attributeName);
            }
        });
    }

    public String getElementAttribute(final Document doc, final XPathFactory factory, final String elementXpath, final String attributeName) throws Exception {
        Logger logger = Loggers.getLogger(getClass());
        try {
            if (Emptys.isEmpty(attributeName)) {
                throw new IllegalArgumentException("attributeName is empty .");
            }
            final Element element = this.getElement(doc, factory, elementXpath);
            final Attr attr = element.getAttributeNode(attributeName);
            String ret = Emptys.isEmpty(attr) ? "" : attr.getValue();
            if (logger.isDebugEnabled()) {
                logger.debug("get attribute {} from {} is {}", attributeName, elementXpath, ret);
            }
            return ret;
        } catch (Exception ex) {
            logger.error("Error occur when get attribute {} from element {}", attributeName, elementXpath);
            throw ex;
        }
    }

    public Node getNode(final Document doc, String elementXpath) throws XPathExpressionException {
        return getNode(doc, null, elementXpath);
    }

    public Node getNode(final Document doc, final XPathFactory factory, String elementXpath) throws XPathExpressionException {
        boolean usingCustomNamespace = Namespaces.hasCustomNamespace(doc);
        elementXpath = XPaths.wrapXpath(elementXpath, usingCustomNamespace, defaultNamespacePrefix);
        final XPath xpath = (factory == null ? XPathFactory.newInstance() : factory).newXPath();
        xpath.setNamespaceContext(new NodeNamespaceContext(doc, defaultNamespacePrefix));
        final XPathExpression exp = xpath.compile(elementXpath);
        return (Node) exp.evaluate(doc, XPathConstants.NODE);
    }

    public Element getElement(final Document doc, final String elementXpath) throws XPathExpressionException {
        return (Element) getNode(doc, null, elementXpath);
    }

    public Element getElement(final Document doc, final XPathFactory factory, final String elementXpath) throws XPathExpressionException {
        return (Element) getNode(doc, factory, elementXpath);
    }

    public Attr getAttr(final Document doc, final String elementXpath) throws XPathExpressionException {
        return (Attr) getNode(doc, null, elementXpath);
    }

    public Attr getAttr(final Document doc, final XPathFactory factory, final String elementXpath) throws XPathExpressionException {
        return (Attr) getNode(doc, factory, elementXpath);
    }

    public NodeList getNodeList(final Document doc, String elementXpath) throws XPathExpressionException {
        return getNodeList(doc, null, elementXpath);
    }

    public NodeList getNodeList(final Document doc, final XPathFactory factory, String elementXpath) throws XPathExpressionException {
        final XPath xpath =  (factory == null ? XPathFactory.newInstance() : factory).newXPath();
        xpath.setNamespaceContext(new NodeNamespaceContext(doc, defaultNamespacePrefix));
        boolean usingCustomNamespace = Namespaces.hasCustomNamespace(doc);
        elementXpath = XPaths.wrapXpath(elementXpath, usingCustomNamespace, defaultNamespacePrefix);
        final XPathExpression exp = xpath.compile(elementXpath);
        return (NodeList) exp.evaluate(doc, XPathConstants.NODESET);
    }

    public String getString(final Document doc, final XPathFactory factory, String elementXpath) throws XPathExpressionException {
        final XPath xpath = factory.newXPath();
        xpath.setNamespaceContext(new NodeNamespaceContext(doc, defaultNamespacePrefix));
        boolean usingCustomNamespace = Namespaces.hasCustomNamespace(doc);
        elementXpath = XPaths.wrapXpath(elementXpath, usingCustomNamespace, defaultNamespacePrefix);
        final XPathExpression exp = xpath.compile(elementXpath);
        return (String) exp.evaluate(doc, XPathConstants.STRING);
    }

    public Number getNumber(final Document doc, final XPathFactory factory, String elementXpath) throws XPathExpressionException {
        final XPath xpath = factory.newXPath();
        xpath.setNamespaceContext(new NodeNamespaceContext(doc, defaultNamespacePrefix));
        boolean usingCustomNamespace = Namespaces.hasCustomNamespace(doc);
        elementXpath = XPaths.wrapXpath(elementXpath, usingCustomNamespace, defaultNamespacePrefix);
        final XPathExpression exp = xpath.compile(elementXpath);
        return (Number) exp.evaluate(doc, XPathConstants.NUMBER);
    }

    public Boolean getBoolean(final Document doc, final XPathFactory factory, String elementXpath) throws XPathExpressionException {
        final XPath xpath = factory.newXPath();
        xpath.setNamespaceContext(new NodeNamespaceContext(doc, defaultNamespacePrefix));
        boolean usingCustomNamespace = Namespaces.hasCustomNamespace(doc);
        elementXpath = XPaths.wrapXpath(elementXpath, usingCustomNamespace, defaultNamespacePrefix);
        final XPathExpression exp = xpath.compile(elementXpath);
        return (Boolean) exp.evaluate(doc, XPathConstants.BOOLEAN);
    }
}
