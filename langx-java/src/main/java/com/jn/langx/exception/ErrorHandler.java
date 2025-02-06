package com.jn.langx.exception;

import com.jn.langx.util.function.Handler;

/**
 * ErrorHandler 接口继承自 Handler<Throwable>，用于定义处理异常的策略和行为
 * 它提供了一种机制来处理代码执行过程中可能发生的错误或异常情况
 */
public interface ErrorHandler extends Handler<Throwable> {

    /**
     * 处理给定的异常
     *
     * @param throwable 要处理的异常或错误对象，它是 Throwable 类型，代表了任何可以被抛出的异常或错误
     *                  这个参数允许处理函数访问异常信息，从而可以进行适当的处理或记录
     */
    void handle(Throwable throwable);
}
