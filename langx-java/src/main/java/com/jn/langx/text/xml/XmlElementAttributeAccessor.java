package com.jn.langx.text.xml;

import com.jn.langx.util.BasedStringAccessor;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;

public class XmlElementAttributeAccessor extends BasedStringAccessor<String, Element> {
    @Override
    public Object get(String attributeName) {
        return getString(attributeName, (String) null);
    }

    @Override
    public String getString(String attributeName, String defaultValue) {
        Attr attr = getTarget().getAttributeNode(attributeName);
        return attr == null ? null : attr.getValue();
    }

    @Override
    public void set(String attributeName, Object value) {
        String attributeValue = value == null ? "" : value.toString();
        Attr attr = getTarget().getAttributeNode(attributeName);
        if (attr != null) {
            attr.setValue(attributeValue);
        }
    }

    @Override
    public boolean has(String attributeName) {
        return getTarget().getAttributeNode(attributeName) != null;
    }
}
