package com.jn.langx.classpath;

import com.jn.langx.io.resource.AbstractLocatableResource;
/**
 * 抽象类 ClassFile 继承自 AbstractLocatableResource。
 * 提供获取类名的方法 getClassName()。
 */
public abstract class ClassFile extends AbstractLocatableResource {

    /**
     * 获取类名。
     *
     * @return 类名字符串，不能为空。
     * @throws IllegalStateException 如果无法获取类名。
     */
    public abstract String getClassName();
}
