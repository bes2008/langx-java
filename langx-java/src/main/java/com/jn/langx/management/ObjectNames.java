package com.jn.langx.management;

import javax.management.ObjectName;
import java.util.Set;

public class ObjectNames {
    private ObjectNames(){

    }
    public static Set<ObjectName> queryObjectNames(JMXConnection connection, String objectName) throws MBeanException {
        ObjectName oname = null;
        try {
            oname = ObjectName.getInstance(objectName);
        } catch (Throwable e) {
            throw new MBeanException(e);
        }
        return queryObjectNames(connection, oname);
    }

    public static Set<ObjectName> queryObjectNames(JMXConnection connection, ObjectName objectName) {
        return connection.queryNames(objectName, null);
    }
}
