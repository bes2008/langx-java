package com.jn.langx.io.resource;

import java.io.IOException;
import java.net.URL;

/**
 * Urlable接口定义了获取URL对象的方法
 * 它可以用于任何需要提供URL访问的类
 */
public interface Urlable {
    /**
     * 获取当前对象的URL表示
     *
     * 此方法允许调用者获取一个URL对象，该对象表示当前对象的网络位置或资源位置
     * 实现此接口的类需要确保能够正确地形成一个URL
     *
     * @return URL对象，表示当前对象的位置或资源
     * @throws IOException 如果无法创建URL，则抛出IOException异常
     */
    URL getUrl() throws IOException;
}
