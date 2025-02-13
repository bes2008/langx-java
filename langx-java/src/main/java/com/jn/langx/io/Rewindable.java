package com.jn.langx.io;

/**
 * Rewindable接口定义了一个可以回放的对象所需具备的功能
 * 它允许对象回到其初始状态或先前的某个状态
 *
 * @param <X> 实现该接口的泛型类型，表示可以回放的对象类型
 */
public interface Rewindable<X> {
    /**
     * 将当前对象回放到其初始状态或先前的某个状态
     *
     * @return 回放后的对象实例，允许返回null如果回放操作不适用或未执行
     */
    X rewind();
}
