package com.jn.langx.text.xml;

import com.jn.langx.util.BasedStringAccessor;
import com.jn.langx.util.Throwables;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;

public class XmlNodeContentAccessor extends BasedStringAccessor<XPathExpression, Document> {
    @Override
    public Object get(XPathExpression exp) {
        Node node = null;
        Throwable exception = null;
        try {
            node = (Node) exp.evaluate(getTarget(), XPathConstants.NODE);
        } catch (XPathExpressionException ex) {
            exception = ex;
        }
        if (node != null) {
            return node.getTextContent();
        }
        if (exception != null) {
            throw Throwables.wrapAsRuntimeException(exception);
        }
        return null;
    }

    @Override
    public boolean has(XPathExpression key) {
        return get(key) != null;
    }

    @Override
    public String getString(XPathExpression exp, String defaultValue) {
        Object content = get(exp);
        if (content == null) {
            return defaultValue;
        }
        return content.toString();
    }

    @Override
    public void set(XPathExpression exp, Object value) {
        String content = value == null ? "" : value.toString();
        Node node = null;
        Throwable exception = null;
        try {
            node = (Node) exp.evaluate(getTarget(), XPathConstants.NODE);
        } catch (XPathExpressionException ex) {
            exception = ex;
        }
        if (node != null) {
            node.setTextContent(content);
        }
        if (exception != null) {
            throw Throwables.wrapAsRuntimeException(exception);
        }
    }
}
