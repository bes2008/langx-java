package com.jn.langx.io.resource;

/**
 * 可定位的接口，用于定义能够获取位置信息的对象所需的方法
 * location 可以相对的，也可以是绝对的，且是有前缀的
 */
public interface Locatable {
    /**
     * 获取位置的前缀
     *
     * @return 前缀字符串
     */
    String getPrefix();

    /**
     * 获取路径信息
     *
     * @return 路径字符串
     */
    String getPath();

    /**
     * 获取Location对象，Location对象封装了位置信息
     *
     * @return Location对象
     */
    Location getLocation();

    /**
     * 获取绝对路径信息
     *
     * @return 绝对路径字符串
     */
    String getAbsolutePath();
}
