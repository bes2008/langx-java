package com.jn.langx.util.concurrent.promise;

import com.jn.langx.util.function.Handler;

/**
 * 代表Promise中要运行的任务。
 * <pre>
 * 如果在run方法中抛出异常，那么Promise会认为失败，并通知订阅者。
 * 如果在run方法中执行了 resolve或者reject，那么 resolve或者reject之后的 下列情况都会被忽略：
 * 1. resolve或者reject之后，再执行resolve或者reject，将会被忽略
 * 2. resolve或者reject之后，run 方法的返回值将会被忽略
 * 3. resolve或者reject之后，run 方法中的后续代码出现了异常，也将被忽略
 * </pre>
 */
public interface Task {
    /**
     * @param resolve 它是由Promise自动创建的，用来成功时传递结果给订阅者
     * @param reject 它是由Promise自动创建的，用来在失败时传递结果给订阅者
     * @return 任务的返回值，也是任务的运行结果
     */
    Object run(Handler resolve, Handler reject);

}
