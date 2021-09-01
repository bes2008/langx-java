package com.jn.langx.session;

import com.jn.langx.IdGenerator;

public interface SessionIdGenerator<E> extends IdGenerator<E> {
    @Override
    String get(E e);
}
