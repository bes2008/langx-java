package com.jn.langx.repository;

import java.util.List;

public interface Repository2<E, ID> extends Repository<E, ID> {
    boolean has(ID id);

    long count();

    List<E> getByIds(Iterable<ID> ids);

    void add(Iterable<E> entities);

    void update(Iterable<E> entities);

    void removeByIds(Iterable<ID> id);
}
