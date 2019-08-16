package com.jn.langx.parser;

import com.jn.langx.util.BasedStringAccessor;
import com.jn.langx.util.collection.StringMap;
import com.jn.langx.util.collection.StringMapAccessor;

/**
 * @author jinuo.fang
 */
public class HttpQueryStringAccessor extends BasedStringAccessor<String, String> implements Parser<String, HttpQueryStringAccessor> {
    private StringMapAccessor delegate;

    public HttpQueryStringAccessor() {
    }

    public HttpQueryStringAccessor(String url) {
        this();
        setTarget(url);
    }

    @Override
    public void setTarget(String url) {
        super.setTarget(url);
        delegate = parse0(url);
    }

    @Override
    public Object get(String key) {
        return delegate.get(key);
    }

    @Override
    public String getString(String key, String defaultValue) {
        return delegate.getString(key, defaultValue);
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

    @Override
    public void set(String key, Object value) {
        delegate.set(key, value);
    }

}
