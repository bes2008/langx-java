package com.jn.langx.management.service;

import com.jn.langx.management.BaseService;
import com.jn.langx.management.MBeanException;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.Maps;
import com.jn.langx.util.logging.Loggers;
import com.jn.langx.util.reflect.Reflects;
import com.jn.langx.util.struct.Entry;
import org.slf4j.Logger;

import javax.management.*;
import java.lang.reflect.Field;
import java.util.*;

public abstract class AbstractSpecifiedOptionService extends BaseService {

    protected Map<String, Map<String, Object>> queryMBeanAttrs(final String specifiedOption, final Map<String, String> options, final Collection<String> attributeNames) {
        Preconditions.checkNotNull(specifiedOption, "specialOption is null. ");
        final ObjectName queryOname = this.createObjectName(options);
        final Set<ObjectInstance> instances = this.conn.queryMBeans(queryOname, null);
        final List<ObjectName> onames = new ArrayList<ObjectName>();
        if (instances != null && !instances.isEmpty()) {
            for (final ObjectInstance ins : instances) {
                onames.add(ins.getObjectName());
            }
        }
        Logger logger = Loggers.getLogger(getClass());
        Map<String, Map<String, Object>> map = Maps.newLinkedHashMap();
        if (!onames.isEmpty()) {
            map = new HashMap<String, Map<String, Object>>();
            for (final ObjectName oname : onames) {
                try {
                    final List<Attribute> attrList = this.conn.getAttributes(oname, attributeNames.toArray(new String[0])).asList();
                    if (attrList == null || attrList.isEmpty()) {
                        continue;
                    }
                    final Map<String, Object> mbean = new HashMap<String, Object>();
                    for (final Attribute attr : attrList) {
                        final String attrName = attr.getName();
                        final Object attrValue = attr.getValue();
                        mbean.put(attrName, attrValue);
                    }
                    map.put(oname.getKeyProperty(specifiedOption), mbean);
                } catch (Exception ex) {
                    logger.error("Get thread pool infoes error", ex);
                }
            }
        }
        return map;
    }

    protected Map<String, List<Entry<String, Object>>> getMBeansAttrs(final String specifiedOption, final List<String> optionValues, final Collection<String> attributeNames) throws Exception {
        Preconditions.checkNotNull(specifiedOption, "specialOption is null. ");
        final Map<String, List<Entry<String, Object>>> result = new HashMap<String, List<Entry<String, Object>>>();
        if (attributeNames.isEmpty()) {
            return result;
        }
        final List<ObjectName> objectNames = new LinkedList<ObjectName>();
        if (optionValues == null || optionValues.isEmpty()) {
            final ObjectName oname = this.createObjectName(null);
            final Set<ObjectName> allValue = this.conn.queryNames(oname, null);
            objectNames.addAll(allValue);
        } else {
            for (final String optionValue : optionValues) {
                final Map<String, String> options = new HashMap<String, String>();
                options.put(specifiedOption, optionValue);
                final ObjectName oname2 = this.createObjectName(options);
                objectNames.add(oname2);
            }
        }
        try {
            Logger logger = Loggers.getLogger(getClass());
            for (final ObjectName oname3 : objectNames) {
                if(logger.isDebugEnabled()) {
                    logger.debug("query attributes : {}", attributeNames);
                }
                final List<Attribute> attrList = this.conn.getAttributes(oname3, attributeNames.toArray(new String[0])).asList();
                if (attrList == null) {
                    continue;
                }
                final List<Entry<String, Object>> mbean = new LinkedList<Entry<String, Object>>();
                final String optionName = oname3.getKeyProperty(specifiedOption);
                for (final Attribute attr : attrList) {
                    final String attrName = attr.getName();
                    final Object attrValue = attr.getValue();
                    mbean.add(new Entry<String, Object>(attrName, attrValue));
                }
                result.put(optionName, mbean);
            }
        } catch (Throwable ex) {
            throw new MBeanException(ex);
        }
        return result;
    }

    protected Map<String, List<Entry<String, Object>>> getMBeans(final String specifiedOption, final List<String> optionValues) throws Exception {
        return this.getMBeans(specifiedOption, optionValues, null);
    }

    protected Map<String, List<Entry<String, Object>>> getMBeans(final String specifiedOption, final List<String> optionValues, final Class<? extends Unserializable> unserialClazz) throws MBeanException {
        if (!Strings.isBlank(specifiedOption)) {
            throw new MBeanException("specialOption is null. ");
        }
        final List<ObjectName> objectNames = new LinkedList<ObjectName>();
        if (optionValues == null || optionValues.isEmpty()) {
            final ObjectName oname = this.createObjectName(null);
            final Set<ObjectName> allValue = this.conn.queryNames(oname, null);
            objectNames.addAll(allValue);
        } else {
            for (final String optionValue : optionValues) {
                final Hashtable<String, String> options = new Hashtable<String, String>();
                options.put(specifiedOption, optionValue);
                final ObjectName oname2 = this.createObjectName(options);
                objectNames.add(oname2);
            }
        }
        final List<String> notSerialAttrs = new ArrayList<String>();
        if (unserialClazz != null && Reflects.isSubClassOrEquals(Unserializable.class, unserialClazz)) {
            final Collection<Field> fields = Reflects.getAllDeclaredFields(unserialClazz,true);
            for (final Field field : fields) {
                notSerialAttrs.add(field.getName());
            }
        }
        final Map<String, List<Entry<String, Object>>> result = new HashMap<String, List<Entry<String, Object>>>();
        try {
            Logger logger = Loggers.getLogger(getClass());
            for (final ObjectName oname2 : objectNames) {
                final MBeanInfo mbeanInfo = this.conn.getMBeanInfo(oname2);
                final MBeanAttributeInfo[] attrInfos = mbeanInfo.getAttributes();
                final List<String> attributeNames = new LinkedList<String>();
                for (MBeanAttributeInfo attrInfo : attrInfos) {
                    final String attrName = attrInfo.getName();
                    if (!notSerialAttrs.contains(attrName)) {
                        attributeNames.add(attrName);
                    }
                }
                if (attributeNames.isEmpty()) {
                    continue;
                }
                if(logger.isDebugEnabled()) {
                    logger.debug("query attributes : {}", attributeNames);
                }
                final List<Attribute> attrList = this.conn.getAttributes(oname2,  attributeNames.toArray(new String[0])).asList();
                if (attrList == null) {
                    continue;
                }
                final List<Entry<String, Object>> mbean = new LinkedList<Entry<String, Object>>();
                final String optionName = oname2.getKeyProperty(specifiedOption);
                for (final Attribute attr : attrList) {
                    final String attrName2 = attr.getName();
                    final Object attrValue = attr.getValue();
                    mbean.add(new Entry<String, Object>(attrName2, attrValue));
                }
                result.put(optionName, mbean);
            }
        } catch (Exception ex) {
            throw new MBeanException(ex);
        }
        return result;
    }

}
