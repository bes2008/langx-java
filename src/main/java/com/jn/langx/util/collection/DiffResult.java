package com.jn.langx.util.collection;

public interface DiffResult<C> {
    C getAdds();
    C getRemoves();
    C getUpdates();
    C getEquals();
}
