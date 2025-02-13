package com.jn.langx.util.enums;

import java.util.List;

/**
 * EnumGetter接口定义了一组用于获取枚举值的方法
 * 它提供通过不同的属性（如名称、代码、显示文本等）来获取枚举值的功能
 * 该接口旨在为枚举相关的操作提供一个统一的访问方式
 */
public interface EnumGetter {
    /**
     * 获取当前EnumGetter可以应用的枚举类列表
     *
     * @return 包含所有可应用枚举类的列表
     */
    List<Class> applyTo();

    /**
     * 通过枚举名称获取枚举实例
     *
     * @param enumClass 枚举类的Class对象
     * @param name 枚举实例的名称
     * @return 匹配名称的枚举实例，如果找不到则返回null
     */
    Enum getByName(Class enumClass, String name);

    /**
     * 通过枚举代码获取枚举实例
     *
     * @param enumClass 枚举类的Class对象
     * @param code 枚举实例的代码
     * @return 匹配代码的枚举实例，如果找不到则返回null
     */
    Enum getByCode(Class enumClass, int code);

    /**
     * 通过显示文本获取枚举实例
     *
     * @param enumClass 枚举类的Class对象
     * @param displayText 枚举实例的显示文本
     * @return 匹配显示文本的枚举实例，如果找不到则返回null
     */
    Enum getByDisplayText(Class enumClass, String displayText);

    /**
     * 通过toString方法的返回值获取枚举实例
     *
     * @param enumClass 枚举类的Class对象
     * @param toString 枚举实例的toString方法返回值
     * @return 匹配toString值的枚举实例，如果找不到则返回null
     */
    Enum getByToString(Class enumClass, String toString);

    /**
     * 获取枚举实例的名称
     *
     * @param e 枚举实例
     * @return 枚举实例的名称
     */
    String getName(Enum e);

    /**
     * 获取枚举实例的显示文本
     *
     * @param e 枚举实例
     * @return 枚举实例的显示文本
     */
    String getDisplayText(Enum e);

    /**
     * 获取枚举实例的代码
     *
     * @param e 枚举实例
     * @return 枚举实例的代码
     */
    int getCode(Enum e);
}
