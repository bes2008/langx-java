package com.jn.langx.repository;

import java.util.List;

public interface BatchRepository<E, ID> extends Repository<E, ID> {
    List<E> getByIds(Iterable<ID> ids);

    void add(Iterable<E> entities);

    void update(Iterable<E> entities);

    void removeByIds(Iterable<ID> id);
}
