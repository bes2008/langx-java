package com.jn.langx.io.stream;

/**
 * IO流拦截器接口，用于在读写IO流操作前后进行拦截处理
 * 该接口允许实现类在进行IO读写操作前后执行自定义逻辑，以便于进行诸如日志记录、权限检查等操作
 *
 * @param <IOStream> 拦截器所处理的IO流类型，可以是任何实现了特定IO操作的类
 */
public interface IOStreamInterceptor<IOStream> {

    /**
     * 在读取数据之前调用的方法
     *
     * @param stream IO流实例，允许访问流的状态或执行相关操作
     * @param b 缓冲区数组，数据将被读取到这个数组中
     * @param off 数组中的起始位置，从该位置开始写入数据
     * @param len 本次操作计划读取的数据长度
     * @return boolean 表示是否应该继续执行读取操作；返回false将取消本次读取操作
     */
    boolean beforeRead(IOStream stream, final byte[] b, final int off, final int len);

    /**
     * 在数据读取之后调用的方法
     *
     * @param stream IO流实例，允许访问流的状态或执行相关操作
     * @param b 缓冲区数组，已读取的数据存储在该数组中
     * @param off 数组中的起始位置，从该位置开始写入数据
     * @param len 本次操作实际读取的数据长度
     * @return boolean 表示是否应该继续处理读取的数据；返回false将忽略本次读取的数据
     */
    boolean afterRead(IOStream stream, final byte[] b, final int off, final int len);

    /**
     * 在写入数据之前调用的方法
     *
     * @param stream IO流实例，允许访问流的状态或执行相关操作
     * @param b 缓冲区数组，待写入的数据存储在该数组中
     * @param off 数组中的起始位置，从该位置开始读取数据进行写入
     * @param len 本次操作计划写入的数据长度
     * @return boolean 表示是否应该继续执行写入操作；返回false将取消本次写入操作
     */
    boolean beforeWrite(IOStream stream, final byte[] b, final int off, final int len);

    /**
     * 在数据写入之后调用的方法
     *
     * @param stream IO流实例，允许访问流的状态或执行相关操作
     * @param b 缓冲区数组，已写入的数据存储在该数组中
     * @param off 数组中的起始位置，从该位置开始读取数据进行写入
     * @param len 本次操作实际写入的数据长度
     * @return boolean 表示是否应该继续处理写入操作的后续逻辑；返回false将忽略本次写入操作
     */
    boolean afterWrite(IOStream stream, final byte[] b, final int off, final int len);
}
