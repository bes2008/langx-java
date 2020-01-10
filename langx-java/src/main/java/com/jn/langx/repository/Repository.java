package com.jn.langx.repository;

public interface Repository<E, ID> {
    E getById(ID id);

    void add(E entity);

    void update(E entity);

    void removeById(ID id);

}
