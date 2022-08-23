package com.jn.langx.plugin;

import com.jn.langx.Named;
import com.jn.langx.Ordered;
import com.jn.langx.lifecycle.Destroyable;
import com.jn.langx.lifecycle.Initializable;

public interface Plugin<E> extends Initializable, Destroyable, Named, Ordered {
    boolean availableFor(E e);
}
