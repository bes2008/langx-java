package com.jn.langx.session;

import com.jn.langx.repository.Repository;

public interface SessionRepository extends Repository<Session,String> {
    @Override
    Session getById(String id);

    @Override
    void add(Session entity);

    @Override
    void update(Session entity);

    @Override
    void removeById(String id);
}
