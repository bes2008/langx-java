package com.jn.langx.parser;

import com.jn.langx.Accessor;
import com.jn.langx.util.collection.StringMap;
import com.jn.langx.util.collection.StringMapAccessor;

public class HttpQueryStringAccessor implements Parser<String, HttpQueryStringAccessor>, Accessor<String, String> {
    private String url;
    private StringMapAccessor delegate;

    public HttpQueryStringAccessor() {
    }

    public HttpQueryStringAccessor(String url) {
        this();
        setTarget(url);
    }

    @Override
    public void setTarget(String url) {
        this.url = url;
        delegate = parse0(url);
    }

    @Override
    public String getTarget() {
        return this.url;
    }

    @Override
    public Object get(String key) {
        return delegate.get(key);
    }

    @Override
    public String getString(String key) {
        return delegate.getString(key);
    }

    @Override
    public String getString(String key, String defaultValue) {
        return delegate.getString(key, defaultValue);
    }

    @Override
    public Integer getInteger(String key) {
        return delegate.getInteger(key);
    }

    @Override
    public Integer getInteger(String key, Integer defaultValue) {
        return delegate.getInteger(key, defaultValue);
    }

    @Override
    public Short getShort(String key) {
        return delegate.getShort(key);
    }

    @Override
    public Short getShort(String key, Short defaultValue) {
        return delegate.getShort(key, defaultValue);
    }

    @Override
    public Double getDouble(String key) {
        return delegate.getDouble(key);
    }

    @Override
    public Double getDouble(String key, Double defaultValue) {
        return delegate.getDouble(key, defaultValue);
    }

    @Override
    public Float getFloat(String key) {
        return delegate.getFloat(key);
    }

    @Override
    public Float getFloat(String key, Float defaultValue) {
        return delegate.getFloat(key, defaultValue);
    }

    @Override
    public Long getLong(String key) {
        return delegate.getLong(key);
    }

    @Override
    public Long getLong(String key, Long defaultValue) {
        return delegate.getLong(key, defaultValue);
    }

    @Override
    public Boolean getBoolean(String key) {
        return delegate.getBoolean(key);
    }

    @Override
    public Boolean getBoolean(String key, Boolean defaultValue) {
        return delegate.getBoolean(key, defaultValue);
    }

    private StringMapAccessor parse0(String url) {
        return new StringMapAccessor(StringMap.httpUrlParameters(url));
    }

    @Override
    public HttpQueryStringAccessor parse(String url) {
        setTarget(url);
        return this;
    }

    public static HttpQueryStringAccessor access(String url) {
        HttpQueryStringAccessor accessor = new HttpQueryStringAccessor();
        accessor.setTarget(url);
        return accessor;
    }

    public StringMap getStringMap() {
        if (this.delegate != null) {
            return this.delegate.getTarget();
        } else {
            return StringMap.EMPTY;
        }
    }
}
