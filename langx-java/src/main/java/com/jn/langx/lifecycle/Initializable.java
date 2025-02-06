package com.jn.langx.lifecycle;

/**
 * 初始化接口，用于标记和执行需要初始化的类的初始化逻辑
 * 这个接口主要用于那些在使用之前需要进行一些预处理或设置的类
 * 通过实现这个接口，类可以明确地定义自己的初始化过程
 */
public interface Initializable {
    /**
     * 执行初始化逻辑的方法
     * 这个方法可能会执行一些资源密集型操作，比如建立数据库连接，读取文件等
     * 因此，它允许抛出InitializationException类型的异常，以便于调用者可以处理初始化失败的情况
     *
     * @throws InitializationException 如果初始化过程失败
     */
    void init() throws InitializationException;
}
