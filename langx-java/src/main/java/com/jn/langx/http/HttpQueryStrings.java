package com.jn.langx.http;

import com.jn.langx.Converter;
import com.jn.langx.codec.CodecException;
import com.jn.langx.text.StringTemplates;
import com.jn.langx.util.StringJoiner;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.StringMap;
import com.jn.langx.util.collection.multivalue.LinkedMultiValueMap;
import com.jn.langx.util.collection.multivalue.MultiValueMap;
import com.jn.langx.util.converter.ConverterService;
import com.jn.langx.util.function.Consumer;
import com.jn.langx.util.function.Consumer2;
import com.jn.langx.util.function.Function;
import com.jn.langx.util.io.Charsets;

import java.net.URLEncoder;
import java.util.Collection;
import java.util.Map;

public class HttpQueryStrings {
    public static StringMap getQueryStringStringMap(String url) {
        if (url == null) {
            return StringMap.EMPTY;
        }
        int paramPartStartIndex = url.indexOf("?") + 1;
        if (paramPartStartIndex == 0 || paramPartStartIndex == url.length()) {
            return StringMap.EMPTY;
        }
        int paramPartEndIndex = url.indexOf("#");
        String queryString = paramPartEndIndex == -1 ? url.substring(paramPartStartIndex) : url.substring(paramPartStartIndex, paramPartEndIndex);
        return new StringMap(queryString, "=", "&");
    }

    public static MultiValueMap<String, String> getQueryStringMultiValueMap(String url) {
        if (url == null) {
            return LinkedMultiValueMap.EMPTY;
        }
        int paramPartStartIndex = url.indexOf("?") + 1;
        if (paramPartStartIndex == 0 || paramPartStartIndex == url.length()) {
            return LinkedMultiValueMap.EMPTY;
        }
        int paramPartEndIndex = url.indexOf("#");
        String queryString = paramPartEndIndex == -1 ? url.substring(paramPartStartIndex) : url.substring(paramPartStartIndex, paramPartEndIndex);
        return com.jn.langx.util.struct.Entry.getMultiValueMap(queryString, "=", "&");
    }

    /**
     * 不会对URL中的特殊字符做处理
     *
     * @param map
     * @return
     */
    public static String toQueryString(Map<String, Object> map, final Map<Class, Function<Object, String>> converterMap) {
        MultiValueMap<String, String> multiValueMap = toMultiValueMap(map, converterMap);
        return toQueryString(multiValueMap);
    }

    public static String toQueryString(Map<String, String> map) {
        return toQueryString(map, false);
    }

    /**
     * 不会对URL中的特殊字符做处理，如需处理，请调用 UrlEncoder 类
     *
     * @param map
     * @return
     */
    public static String toQueryString(Map<String, String> map, final boolean encode) {
        final StringJoiner joiner = new StringJoiner("&", "", "");

        Collects.forEach(map, new Consumer2<String, String>() {
            @Override
            public void accept(final String key, String value) {
                if (encode) {
                    try {
                        value = URLEncoder.encode(value, Charsets.UTF_8.name());
                    } catch (Throwable ex) {
                        throw new CodecException(ex);
                    }
                }
                joiner.add(StringTemplates.formatWithPlaceholder("{}={}", key, value));
            }
        });

        return joiner.toString();
    }

    public static String toQueryString(MultiValueMap<String, String> map) {
        return toQueryString(map, false);
    }

    public static String toQueryString(MultiValueMap<String, String> map, final boolean encode) {
        final StringJoiner joiner = new StringJoiner("&", "", "");

        Collects.forEach(map, new Consumer2<String, Collection<String>>() {
            @Override
            public void accept(final String key, Collection<String> values) {
                Collects.forEach(values, new Consumer<String>() {
                    @Override
                    public void accept(String value) {
                        if (encode) {
                            try {
                                value = URLEncoder.encode(value, Charsets.UTF_8.name());
                            } catch (Throwable ex) {
                                throw new CodecException(ex);
                            }
                        }
                        joiner.add(StringTemplates.formatWithPlaceholder("{}={}", key, value));
                    }
                });
            }
        });

        return joiner.toString();
    }

    private static MultiValueMap<String, String> toMultiValueMap(Map<String, Object> map, final Map<Class, Function<Object, String>> converterMap) {
        final MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<String, String>();
        Collects.forEach(map, new Consumer2<String, Object>() {
            @Override
            public void accept(final String key, final Object value) {
                if (value == null) {
                    return;
                }
                Class valueClass = value.getClass();
                Function<Object, String> mapper = converterMap != null ? converterMap.get(valueClass) : null;
                if (mapper != null) {
                    String v = mapper.apply(value);
                    if (v != null) {
                        multiValueMap.add(key, v);
                    }
                    return;
                } else {
                    Converter<Object, String> converter = new ConverterService().findConverter(value, String.class);
                    if (converter != null) {
                        String v = converter.apply(value);
                        if (v != null) {
                            multiValueMap.add(key, v);
                            return;
                        }
                    }
                }
                final Consumer2<String, Object> self = this;
                Collects.forEach(Collects.asIterable(value), new Consumer<Object>() {
                    @Override
                    public void accept(Object item) {
                        self.accept(key, item);
                    }
                });
            }
        });
        return multiValueMap;
    }

}
