package com.jn.langx.classpath.classloader;

import java.io.InputStream;

/**
 * 类加载器访问器接口
 * 用于加载类和资源文件
 */
public interface ClassLoaderAccessor {

    /**
     * 根据完全限定类名加载类
     *
     * @param fqcn 完全限定类名
     * @return 加载的类对象
     * @throws ClassNotFoundException 如果类未找到
     */
    Class loadClass(String fqcn) throws ClassNotFoundException;

    /**
     * 获取资源文件的输入流
     *
     * @param name 资源文件的名称
     * @return 资源文件的输入流
     */
    InputStream getResourceStream(String name);
}
