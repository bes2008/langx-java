package com.jn.langx.session.impl;

import com.jn.langx.session.Session;
import com.jn.langx.session.SessionRepository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
