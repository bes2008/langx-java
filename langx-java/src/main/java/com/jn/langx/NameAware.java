package com.jn.langx;

/**
 * NameAware接口用于定义可以设置名称的对象。
 * 实现该接口的类将具有设置自身名称的能力
 */
public interface NameAware {
    /**
     * 设置对象的名称
     *
     * @param name 对象的新名称
     */
    void setName(String name);
}
