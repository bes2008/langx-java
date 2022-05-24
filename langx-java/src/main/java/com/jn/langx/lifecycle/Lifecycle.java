package com.jn.langx.lifecycle;


import com.jn.langx.Named;

public interface Lifecycle extends Initializable, Named {
    void startup();

    void shutdown();

}
