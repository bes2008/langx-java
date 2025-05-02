package com.jn.langx.util.concurrent.promise;

import com.jn.langx.util.function.Handler;

/**
 * 代表Promise中要运行的任务。
 *
 */
public interface Task {
    /**
     * @param resolve 它是由Promise自动创建的，用来成功时传递结果给订阅者
     * @param reject 它是由Promise自动创建的，用来在失败时传递结果给订阅者
     * @return 任务的返回值，也是任务的运行结果
     */
    Object run(Handler resolve, Handler reject);

}
