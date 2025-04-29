package com.jn.langx.util.net.http;

import com.jn.langx.Parser;
import com.jn.langx.Transformer;
import com.jn.langx.annotation.NonNull;
import com.jn.langx.util.BasedStringAccessor;
import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.StringMap;
import com.jn.langx.util.collection.multivalue.LinkedMultiValueMap;
import com.jn.langx.util.collection.multivalue.MultiValueMap;
import com.jn.langx.util.collection.multivalue.MultiValueMapAccessor;
import com.jn.langx.util.io.Charsets;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * @author jinuo.fang
 */
public class HttpQueryStringAccessor extends BasedStringAccessor<String, String> implements Parser<String, HttpQueryStringAccessor> {
    private MultiValueMapAccessor<String> delegate;
    private boolean decodeQueryParams = false;

    public HttpQueryStringAccessor() {
    }

    public HttpQueryStringAccessor(boolean decodeQueryParams) {
        this.decodeQueryParams = decodeQueryParams;
    }

    public HttpQueryStringAccessor(@NonNull String url) {
        this(url, false);
    }

    public HttpQueryStringAccessor(@NonNull String url, boolean decodeQueryParams) {
        this(decodeQueryParams);
        setTarget(url);
    }

    public static HttpQueryStringAccessor access(String url) {
        HttpQueryStringAccessor accessor = new HttpQueryStringAccessor();
        accessor.setTarget(url);
        return accessor;
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
    public boolean has(String key) {
        return super.has(key);
    }

    @Override
    public String getString(String key, String defaultValue) {
        return delegate.getString(key, defaultValue);
    }

    private MultiValueMapAccessor parse0(String url) {
        MultiValueMapAccessor accessor = new MultiValueMapAccessor();

        Transformer<String, String> queryParamTransformer = decodeQueryParams ? new Transformer<String, String>() {
            @Override
            public String transform(String input) {
                if (Strings.isEmpty(input)) {
                    return input;
                }
                try {
                    return URLDecoder.decode(input, Charsets.UTF_8.name());
                } catch (UnsupportedEncodingException use) {
                    return input;
                }
            }
        } : null;
        accessor.setTarget(HttpQueryStrings.getQueryStringMultiValueMap(url, queryParamTransformer));
        return accessor;
    }

    @Override
    public HttpQueryStringAccessor parse(String url) {
        setTarget(url);
        return this;
    }

    public StringMap getStringMap() {
        MultiValueMap<String, String> multiValueMap = getMultiValueMap();
        final StringMap map = new StringMap();
        map.putAll(multiValueMap.toSingleValueMap());
        return map;
    }

    public MultiValueMap<String, String> getMultiValueMap() {
        if (this.delegate != null) {
            return (MultiValueMap) this.delegate.getTarget();
        } else {
            return LinkedMultiValueMap.EMPTY;
        }
    }

    @Override
    public void set(String key, Object value) {
        delegate.set(key, value);
    }

    @Override
    public void remove(String key) {
        delegate.remove(key);
    }
}
