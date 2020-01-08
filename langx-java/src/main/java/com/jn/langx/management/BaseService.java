package com.jn.langx.management;

import com.jn.langx.util.struct.Entry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.Attribute;
import javax.management.ObjectName;
import java.util.Collection;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

public abstract class BaseService implements MBeanService {
    private static final Logger logger;
    protected Hashtable<String, String> defaultOptions;
    protected String domain;
    protected JMXConnection conn;

    public BaseService() {
        this.defaultOptions = new Hashtable<String, String>();
        this.init();
    }

    protected abstract void init();

    protected ObjectName createObjectName(final Hashtable<String, String> table) {
        final Hashtable<String, String> options = new Hashtable<String, String>();
        options.putAll(this.defaultOptions);
        if (table != null) {
            options.putAll(table);
        }
        ObjectName oname = null;
        try {
            oname = new ObjectName(this.domain, options);
        } catch (Exception ex) {
        }
        BaseService.logger.debug("create an ObjectName : " + oname);
        return oname;
    }

    @Override
    public List<Entry<String, Object>> getMBeanAttrs(final Hashtable<String, String> options, final Collection<String> attributeNames) {
        final ObjectName oname = this.createObjectName(null);
        List<Attribute> attrList = null;
        if (attributeNames != null && !attributeNames.isEmpty()) {
            attrList = this.conn.getAttributes(oname, (String[]) attributeNames.toArray(new String[0])).asList();
        }
        if (attrList == null) {
            BaseService.logger.warn("The attributeNames is not specified");
            return null;
        }
        final List<Entry<String, Object>> mbean = new LinkedList<Entry<String, Object>>();
        for (final Attribute attr : attrList) {
            final String attrName = attr.getName();
            final Object attrValue = attr.getValue();
            mbean.add(new Entry<String, Object>(attrName, attrValue));
        }
        return mbean;
    }

    static {
        logger = LoggerFactory.getLogger(BaseService.class);
    }
}
