package com.jn.langx.util.concurrent.promise;

import com.jn.langx.util.function.Handler;

/**
 * 代表Promise中要运行的任务
 */
public interface Task {
    /**
     * @param resolve
     * @param reject
     * @return
     */
    Object run(Handler resolve, Handler reject);

}
