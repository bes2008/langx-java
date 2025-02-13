package com.jn.langx.management;

import com.jn.langx.util.struct.Entry;

import java.util.Collection;
import java.util.Hashtable;
import java.util.List;

/**
 * MBeanService接口定义了服务匹配和获取MBean属性的通用方法
 */
public interface MBeanService {
    /**
     * 检查服务是否匹配
     *
     * @return 如果服务匹配返回true，否则返回false
     */
    boolean isServiceMatch();

    /**
     * 获取MBean的指定属性
     *
     * @param p0 包含属性名称和属性值的哈希表，用于指定MBean的属性
     * @param p1 包含要获取的属性名称的集合
     * @return 返回一个包含属性名称和属性值的条目列表
     */
    List<Entry<String, Object>> getMBeanAttrs(final Hashtable<String, String> p0, final Collection<String> p1);
}
