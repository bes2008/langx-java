package com.jn.langx.management.service;

import com.jn.langx.management.BaseService;
import com.jn.langx.management.MBeanException;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.Strings;
import com.jn.langx.util.reflect.Reflects;
import com.jn.langx.util.struct.Entry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.*;
import java.lang.reflect.Field;
import java.util.*;

public abstract class AbstractSpecifiedOptionService extends BaseService {
    private static final Logger logger;

    protected Map<String, Map<String, Object>> queryMBeanAttrs(final String specifiedOption, final Hashtable<String, String> options, final Collection<String> attributeNames) {
        Preconditions.checkNotNull(specifiedOption, "specialOption is null. ");
        final ObjectName queryOname = this.createObjectName(options);
        final Set<ObjectInstance> instances = (Set<ObjectInstance>) this.conn.queryMBeans(queryOname, (QueryExp) null);
        final List<ObjectName> onames = new ArrayList<ObjectName>();
        if (instances != null && !instances.isEmpty()) {
            for (final ObjectInstance ins : instances) {
                onames.add(ins.getObjectName());
            }
        }
        Map<String, Map<String, Object>> map = (Map<String, Map<String, Object>>) Collections.EMPTY_MAP;
        if (!onames.isEmpty()) {
            map = new HashMap<String, Map<String, Object>>();
            for (final ObjectName oname : onames) {
                try {
                    final List<Attribute> attrList = this.conn.getAttributes(oname, (String[]) attributeNames.toArray(new String[0])).asList();
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
                    AbstractSpecifiedOptionService.logger.error("Get thread pool infoes error", (Throwable) ex);
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
            final Set<ObjectName> allValue = (Set<ObjectName>) this.conn.queryNames(oname, (QueryExp) null);
            for (final ObjectName optionObjectName : allValue) {
                objectNames.add(optionObjectName);
            }
        } else {
            for (final String optionValue : optionValues) {
                final Hashtable<String, String> options = new Hashtable<String, String>();
                options.put(specifiedOption, optionValue);
                final ObjectName oname2 = this.createObjectName(options);
                objectNames.add(oname2);
            }
        }
        try {
            for (final ObjectName oname3 : objectNames) {
                AbstractSpecifiedOptionService.logger.debug("query attributes : " + attributeNames);
                final List<Attribute> attrList = this.conn.getAttributes(oname3, (String[]) attributeNames.toArray(new String[0])).asList();
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

    protected Map<String, List<Entry<String, Object>>> getMBeans(final String specifiedOption, final List<String> optionValues, final Class<? extends Unserializable> unerialClazz) throws MBeanException {
        if (!Strings.isBlank(specifiedOption)) {
            throw new MBeanException("specialOption is null. ");
        }
        final List<ObjectName> objectNames = new LinkedList<ObjectName>();
        if (optionValues == null || optionValues.isEmpty()) {
            final ObjectName oname = this.createObjectName(null);
            final Set<ObjectName> allValue = (Set<ObjectName>) this.conn.queryNames(oname, (QueryExp) null);
            for (final ObjectName optionObjectName : allValue) {
                objectNames.add(optionObjectName);
            }
        } else {
            for (final String optionValue : optionValues) {
                final Hashtable<String, String> options = new Hashtable<String, String>();
                options.put(specifiedOption, optionValue);
                final ObjectName oname2 = this.createObjectName(options);
                objectNames.add(oname2);
            }
        }
        final List<String> notSerialAttrs = new ArrayList<String>();
        if (unerialClazz != null && Reflects.isSubClassOrEquals(Unserializable.class, unerialClazz)) {
            final Field[] arr$;
            final Field[] fields = arr$ = unerialClazz.getDeclaredFields();
            for (final Field field : arr$) {
                notSerialAttrs.add(field.getName());
            }
        }
        final Map<String, List<Entry<String, Object>>> result = new HashMap<String, List<Entry<String, Object>>>();
        try {
            for (final ObjectName oname2 : objectNames) {
                final MBeanInfo mbeanInfo = this.conn.getMBeanInfo(oname2);
                final MBeanAttributeInfo[] attrInfos = mbeanInfo.getAttributes();
                final List<String> attributeNames = new LinkedList<String>();
                for (int i = 0; i < attrInfos.length; ++i) {
                    final String attrName = attrInfos[i].getName();
                    if (!notSerialAttrs.contains(attrName)) {
                        attributeNames.add(attrName);
                    }
                }
                if (attributeNames.isEmpty()) {
                    continue;
                }
                AbstractSpecifiedOptionService.logger.debug("query attributes : " + attributeNames);
                final List<Attribute> attrList = this.conn.getAttributes(oname2, (String[]) attributeNames.toArray(new String[0])).asList();
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

    static {
        logger = LoggerFactory.getLogger((Class) AbstractSpecifiedOptionService.class);
    }
}
