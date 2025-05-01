package com.jn.langx.util.concurrent.promise;

import com.jn.langx.util.function.Handler;

public interface Task {
    Object run(Handler resolve, Handler reject);
}
