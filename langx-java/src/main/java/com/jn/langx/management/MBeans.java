package com.jn.langx.management;

import com.jn.langx.util.collection.Collects;

import javax.management.ObjectName;
import java.util.*;

public class MBeans {
    private MBeans(){

    }
    public static Map<String, Object> getAttributes(JMXConnection connection, String objectName, List<String> attributeNames) {
        MBean mbean = getMBean(connection, objectName, attributeNames);
        return mbean.getAttributeMap();
    }

    public static Object getAttribute(JMXConnection connection, String objectName, String attributeName) {
        MBean mbean = getMBean(connection, objectName, Collects.asList(attributeName));
        return mbean.getAttribute(attributeName);
    }

    public static MBean getMBean(JMXConnection connection, String objectName, List<String> attributeNames) {
        Set<ObjectName> objectNames = ObjectNames.queryObjectNames(connection, objectName);
        ObjectName oname = null;
        if (objectNames.size() > 1) {
            oname = objectNames.toArray(new ObjectName[0])[0];
        }
        boolean getAttributes = attributeNames != null && !attributeNames.isEmpty();
        MBean mbean = MBean.of(oname);
        if (getAttributes) {
            mbean.putAttributes(connection.getAttributes(oname, attributeNames.toArray(new String[0])));
        }
        return mbean;
    }

    public static List<MBean> getMBeans(JMXConnection connection, String objectName, Collection<String> attributeNames) {
        Set<ObjectName> objectNames = ObjectNames.queryObjectNames(connection, objectName);
        List<MBean> mbeans = new ArrayList<MBean>();
        boolean getAttributes = attributeNames != null && !attributeNames.isEmpty();
        for (ObjectName oname : objectNames) {
            MBean mbean = MBean.of(oname);
            mbeans.add(mbean);
            if (getAttributes) {
                mbean.putAttributes(connection.getAttributes(oname, attributeNames.toArray(new String[0])));
            }
        }
        return mbeans;
    }
}
