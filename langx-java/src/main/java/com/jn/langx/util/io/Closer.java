package com.jn.langx.util.io;

import java.util.List;

public interface Closer<I> {
    void close(I i);
    List<Class> applyTo();
}
