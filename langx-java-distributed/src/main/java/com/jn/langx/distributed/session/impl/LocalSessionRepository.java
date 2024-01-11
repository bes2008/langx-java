package com.jn.langx.distributed.session.impl;

import com.jn.langx.distributed.session.Session;
import com.jn.langx.distributed.session.SessionRepository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @since 3.7.0
 */
public class LocalSessionRepository implements SessionRepository {
    private Map<String, Session> map = new ConcurrentHashMap<String, Session>();

    @Override
    public Session getById(String id) {
        return map.get(id);
    }

    @Override
    public void add(Session entity) {
        map.put(entity.getId(), entity);
    }

    @Override
    public void update(Session entity) {
        map.put(entity.getId(), entity);
    }

    @Override
    public void removeById(String id) {
        map.remove(id);
    }
}
