package com.jn.langx.text.xml;

import com.jn.langx.util.Emptys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.xpath.*;
import java.util.Map;

public class XmlAccessor {
    private static final Logger logger = LoggerFactory.getLogger((Class) XmlAccessor.class);

    public void setElementAttribute(final Document doc, final XPathFactory factory, final String elementXpath, final String attributeName, final String attributeValue) throws Exception {
        if (Emptys.isEmpty(attributeName)) {
            return;
        }
        try {
            final Element element = this.getElement(doc, factory, elementXpath);
            if (Emptys.isEmpty(element)) {
                return;
            }
            element.setAttribute(attributeName, attributeValue);
            if (logger.isDebugEnabled()) {
                logger.debug("set attribute {} = {} for element ", attributeName, attributeValue, elementXpath);
            }
        } catch (Exception ex) {
            logger.error("Error occur when set attribute {} for element {}", attributeName, elementXpath);
            throw ex;
        }
    }

    protected Element getElement(final Document doc, final XPathFactory factory, final String elementXpath) throws XPathExpressionException {
        final XPath xpath = factory.newXPath();
        final XPathExpression exp = xpath.compile(elementXpath);
        return (Element) exp.evaluate(doc, XPathConstants.NODE);
    }

    public void setElementAttributes(final Document doc, final XPathFactory factory, final String elementXpath, final Map<String, String> attrs) throws Exception {
        if (Emptys.isEmpty(attrs)) {
            return;
        }
        try {
            final Element element = this.getElement(doc, factory, elementXpath);
            if (Emptys.isEmpty(element)) {
                return;
            }
            for (final String attributeName : attrs.keySet()) {
                if (logger.isDebugEnabled()) {
                    logger.debug("set attribute {} = {} for element {}", attributeName, attrs.get(attributeName), elementXpath);
                }
                element.setAttribute(attributeName, attrs.get(attributeName));
            }
        } catch (Exception ex) {
            logger.error("Error occur when set attribute for element {}", elementXpath);
            throw ex;
        }
    }

    public void setElementsAttributes(final Document doc, final XPathFactory factory, final String elementXpath, final Map<String, String> attrs) throws Exception {
        if (Emptys.isEmpty(attrs)) {
            return;
        }
        try {
            final NodeList elements = this.getNodeList(doc, factory, elementXpath);
            if (Emptys.isEmpty(elements)) {
                return;
            }
            for (int i = 0; i < elements.getLength(); ++i) {
                final Element element = (Element) elements.item(i);
                for (final String attributeName : attrs.keySet()) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("set attribute {} = {} for element {}", attributeName, attrs.get(attributeName), elementXpath);
                    }
                    element.setAttribute(attributeName, attrs.get(attributeName));
                }
            }
        } catch (Exception ex) {
            logger.error("Error occur when set attribute for element {}", elementXpath);
            throw ex;
        }
    }

    public String getElementAttribute(final String xmlFilePath, final String elementXpath, final String attributeName) throws Exception {
        return Xmls.handleXml(xmlFilePath, new XmlDocumentHandler<String>() {
            @Override
            public String handle(final Document doc) throws Exception {
                final XPathFactory factory = XPathFactory.newInstance();
                return getElementAttribute(doc, factory, elementXpath, attributeName);
            }
        });
    }

    public String getElementAttribute(final Document doc, final XPathFactory factory, final String elementXpath, final String attributeName) throws Exception {
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

    public NodeList getNodeList(final Document doc, final XPathFactory factory, final String elementXpath) throws XPathExpressionException {
        final XPath xpath = factory.newXPath();
        final XPathExpression exp = xpath.compile(elementXpath);
        return (NodeList) exp.evaluate(doc, XPathConstants.NODESET);
    }

}
