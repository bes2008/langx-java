package com.jn.langx.text.xml.resolver.xpath;

import javax.xml.namespace.QName;
import javax.xml.xpath.XPathVariableResolver;
import java.util.HashMap;
import java.util.Map;

public class SimpleVariableResolver implements XPathVariableResolver {
    private final Map<QName, Object> variables = new HashMap<QName, Object>();

    /**
     * External methods to add parameter
     *
     * @param name  Parameter name
     * @param value Parameter value
     */
    public void addVariable(QName name, Object value) {
        variables.put(name, value);
    }

    /**
     * {@inheritDoc}
     *
     * @param variableName
     */
    @Override
    public Object resolveVariable(QName variableName) {
        return variables.get(variableName);
    }
}
