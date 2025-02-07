package com.jn.langx.util.function;

/**
 * @since 4.4.3
 */
public interface Consumer3<ARG1, ARG2, ARG3> {
    /**
     * 执行操作的接口方法，接受三个参数。
     *
     * @param arg1 第一个参数，其类型为ARG1。
     * @param arg2 第二个参数，其类型为ARG2。
     * @param arg3 第三个参数，其类型为ARG3。
     */
    void accept(ARG1 arg1, ARG2 arg2, ARG3 arg3);
}
