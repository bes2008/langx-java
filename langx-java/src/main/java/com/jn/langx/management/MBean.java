package com.jn.langx.management;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.ObjectName;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class MBean {
    private String domain;
    private Hashtable<String, String> options = new Hashtable<String, String>();
    private String objectName;
    private Map<String, Object> attributeMap = new HashMap<String, Object>();


    public void putAttribute(Attribute attribute){
        this.attributeMap.put(attribute.getName(), attribute.getValue());
    }

    public void putAttributes(AttributeList attributes){
        putAttributes(attributes.asList());
    }

    public void putAttributes(List<Attribute> attributes){
        for (Attribute attribute : attributes) {
            putAttribute(attribute);
        }
    }

    public String getOption(String optionName){
        return this.options.get(optionName);
    }

    public Object getAttribute(String attributeName){
        return this.attributeMap.get(attributeName);
    }

    public static MBean of(ObjectName oname) throws IllegalArgumentException{
        if (oname ==null || oname.isPattern()){
            throw new IllegalArgumentException();
        }
        MBean mbean = new MBean();
        mbean.setDomain(oname.getDomain());
        mbean.setObjectName(oname.toString());
        mbean.options.putAll(oname.getKeyPropertyList());
        return mbean;
    }

    public Hashtable<String, String> getOptions() {
        return new Hashtable<String, String>(options);
    }

    public void setOptions(Hashtable<String, String> options) {
        this.options = new Hashtable<String, String>(options);
    }

    public Map<String, Object> getAttributeMap() {
        return attributeMap;
    }

    public void setAttributeMap(Map<String, Object> attributeMap) {
        this.attributeMap = attributeMap;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }
}
