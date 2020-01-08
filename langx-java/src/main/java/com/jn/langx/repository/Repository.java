package com.jn.langx.repository;

public interface Repository<E, ID> {
    E getById(ID id);

    E add(E entity);

    E update(E entity);

    void removeById(ID id);

}
