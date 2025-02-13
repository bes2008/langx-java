package com.jn.langx.util.collection;


/**
 * Attributable接口定义了一套标准的操作属性的方法
 * 它允许对象以键值对的形式存储属性，其中键是字符串，值是任意对象
 * 这个接口的主要目的是提供一个灵活的方式来关联和管理对象的动态属性
 */
public interface Attributable {

    /**
     * 设置属性值
     * 如果给定名称的属性已经存在，该方法将覆盖现有的值
     *
     * @param name 属性名称，不能为空
     * @param value 属性值，可以是任意对象，包括null
     */
    void setAttribute(String name, Object value);

    /**
     * 获取指定名称的属性值
     *
     * @param name 属性名称，不能为空
     * @return 属性值，如果指定名称的属性不存在，则返回null
     */
    Object getAttribute(String name);

    /**
     * 检查是否存在指定名称的属性
     *
     * @param name 属性名称，不能为空
     * @return 如果存在指定名称的属性，则返回true；否则返回false
     */
    boolean hasAttribute(String name);

    /**
     * 移除指定名称的属性
     * 如果属性不存在，该方法不做任何操作
     *
     * @param name 属性名称，不能为空
     */
    void removeAttribute(String name);

    /**
     * 获取所有属性名称的集合
     * 这个方法返回一个Iterable对象，允许遍历所有属性名称
     *
     * @return 所有属性名称的Iterable集合，如果没有任何属性，返回空集合
     */
    Iterable<String> getAttributeNames();
}
