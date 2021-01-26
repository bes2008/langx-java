package com.jn.langx.repository;

import java.util.List;

public interface Repository2<E, ID> extends Repository<E, ID> {
    boolean has(ID id);

    long count();

    List<E> getByIds(Iterable<ID> ids);

    void addAll(Iterable<E> entities);

    void update(Iterable<E> entities);

    void removeByIds(Iterable<ID> id);

    @Override
    E getById(ID id);

    @Override
    void add(E entity);

    @Override
    void update(E entity);

    @Override
    void removeById(ID id);
}
