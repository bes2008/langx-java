package com.jn.langx.util.net.uri;

import com.jn.langx.annotation.Nullable;
import com.jn.langx.util.Objs;
import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.multivalue.MultiValueMap;
import com.jn.langx.util.net.uri.component.UriComponents;
import com.jn.langx.util.net.uri.component.UriComponentsBuilder;
import com.jn.langx.util.struct.Holder;

import java.net.URI;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DefaultUriBuilder implements UriBuilder {
    private boolean parsePath;
    private EncodingMode encodingMode;
    @Nullable
    private UriComponentsBuilder baseUri;
    private final Map<String, ?> defaultUriVariables;

    private final UriComponentsBuilder uriComponentsBuilder;

    public DefaultUriBuilder(String uriTemplate) {
        this(null, uriTemplate, null, true, null);
    }

    public DefaultUriBuilder(@Nullable UriComponentsBuilder baseUri, String uriTemplate) {
        this(baseUri, uriTemplate, null, true, null);
    }

    public DefaultUriBuilder(@Nullable UriComponentsBuilder baseUri, String uriTemplate, EncodingMode encodingMode) {
        this(baseUri, uriTemplate, encodingMode, true, null);
    }

    public DefaultUriBuilder(@Nullable UriComponentsBuilder baseUri, String uriTemplate, EncodingMode encodingMode, boolean parsePath) {
        this(baseUri, uriTemplate, encodingMode, parsePath, null);
    }

    public DefaultUriBuilder(@Nullable UriComponentsBuilder baseUri, String uriTemplate, EncodingMode encodingMode, boolean parsePath, Map<String, ?> defaultUriVariables) {
        this.baseUri = baseUri;
        this.encodingMode = encodingMode == null ? EncodingMode.TEMPLATE_AND_VALUES : encodingMode;
        this.parsePath = parsePath;
        if (defaultUriVariables == null) {
            defaultUriVariables = new HashMap<String, Object>();
        }
        this.defaultUriVariables = defaultUriVariables;
        this.uriComponentsBuilder = initUriComponentsBuilder(uriTemplate);
    }

    private UriComponentsBuilder initUriComponentsBuilder(String uriTemplate) {
        UriComponentsBuilder result;
        if (Strings.isEmpty(uriTemplate)) {
            result = (baseUri != null ? baseUri.cloneBuilder() : UriComponentsBuilder.newInstance());
        } else if (baseUri != null) {
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(uriTemplate);
            UriComponents uri = builder.build();
            result = (uri.getHost() == null ? baseUri.cloneBuilder().uriComponents(uri) : builder);
        } else {
            result = UriComponentsBuilder.fromUriString(uriTemplate);
        }
        if (encodingMode.equals(EncodingMode.TEMPLATE_AND_VALUES)) {
            result.encode();
        }
        parsePathIfNecessary(result);
        return result;
    }

    private void parsePathIfNecessary(UriComponentsBuilder result) {
        if (parsePath && encodingMode.equals(EncodingMode.URI_COMPONENT)) {
            UriComponents uric = result.build();
            String path = uric.getPath();
            result.replacePath(null);
            for (String segment : uric.getPathSegments()) {
                result.pathSegment(segment);
            }
            if (path != null && path.endsWith("/")) {
                result.path("/");
            }
        }
    }


    @Override
    public DefaultUriBuilder scheme(@Nullable String scheme) {
        this.uriComponentsBuilder.scheme(scheme);
        return this;
    }

    @Override
    public DefaultUriBuilder userInfo(@Nullable String userInfo) {
        this.uriComponentsBuilder.userInfo(userInfo);
        return this;
    }

    @Override
    public DefaultUriBuilder host(@Nullable String host) {
        this.uriComponentsBuilder.host(host);
        return this;
    }

    @Override
    public DefaultUriBuilder port(int port) {
        this.uriComponentsBuilder.port(port);
        return this;
    }

    @Override
    public DefaultUriBuilder port(@Nullable String port) {
        this.uriComponentsBuilder.port(port);
        return this;
    }

    @Override
    public DefaultUriBuilder path(String path) {
        this.uriComponentsBuilder.path(path);
        return this;
    }

    @Override
    public DefaultUriBuilder replacePath(@Nullable String path) {
        this.uriComponentsBuilder.replacePath(path);
        return this;
    }

    @Override
    public DefaultUriBuilder pathSegment(String... pathSegments) {
        this.uriComponentsBuilder.pathSegment(pathSegments);
        return this;
    }

    @Override
    public DefaultUriBuilder query(String query) {
        this.uriComponentsBuilder.query(query);
        return this;
    }

    @Override
    public DefaultUriBuilder replaceQuery(@Nullable String query) {
        this.uriComponentsBuilder.replaceQuery(query);
        return this;
    }

    @Override
    public DefaultUriBuilder queryParam(String name, Object... values) {
        this.uriComponentsBuilder.queryParam(name, values);
        return this;
    }

    @Override
    public DefaultUriBuilder queryParam(String name, @Nullable Collection<?> values) {
        this.uriComponentsBuilder.queryParam(name, values);
        return this;
    }

    @Override
    public DefaultUriBuilder queryParamIfPresent(String name, Holder<?> value) {
        this.uriComponentsBuilder.queryParamIfPresent(name, value);
        return this;
    }

    @Override
    public DefaultUriBuilder queryParams(MultiValueMap<String, String> params) {
        this.uriComponentsBuilder.queryParams(params);
        return this;
    }

    @Override
    public DefaultUriBuilder replaceQueryParam(String name, Object... values) {
        this.uriComponentsBuilder.replaceQueryParam(name, values);
        return this;
    }

    @Override
    public DefaultUriBuilder replaceQueryParam(String name, @Nullable Collection<?> values) {
        this.uriComponentsBuilder.replaceQueryParam(name, values);
        return this;
    }

    @Override
    public DefaultUriBuilder replaceQueryParams(MultiValueMap<String, String> params) {
        this.uriComponentsBuilder.replaceQueryParams(params);
        return this;
    }

    @Override
    public DefaultUriBuilder fragment(@Nullable String fragment) {
        this.uriComponentsBuilder.fragment(fragment);
        return this;
    }

    @Override
    public URI build(Map<String, ?> uriVars) {
        if (!defaultUriVariables.isEmpty()) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.putAll(defaultUriVariables);
            map.putAll(uriVars);
            uriVars = map;
        }
        if (encodingMode.equals(EncodingMode.VALUES_ONLY)) {
            uriVars = URIs.encodeUriVariables(uriVars);
        }
        UriComponents uric = this.uriComponentsBuilder.build().replaceVariables(uriVars);
        return createUri(uric);
    }

    @Override
    public URI build(Object... uriVars) {
        if (Objs.isEmpty(uriVars) && !defaultUriVariables.isEmpty()) {
            return build(Collections.emptyMap());
        }
        if (encodingMode.equals(EncodingMode.VALUES_ONLY)) {
            uriVars = URIs.encodeUriVariables(uriVars);
        }
        UriComponents uric = this.uriComponentsBuilder.build().replaceVariables(uriVars);
        return createUri(uric);
    }

    private URI createUri(UriComponents uric) {
        if (encodingMode.equals(EncodingMode.URI_COMPONENT)) {
            uric = uric.encode();
        }
        return URI.create(uric.toString());
    }
}