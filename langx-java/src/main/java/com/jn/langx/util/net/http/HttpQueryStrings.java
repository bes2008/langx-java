package com.jn.langx.util.net.http;

import com.jn.langx.codec.CodecException;
import com.jn.langx.codec.base64.Base64;
import com.jn.langx.text.StringTemplates;
import com.jn.langx.text.StringJoiner;
import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.Arrs;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.StringMap;
import com.jn.langx.util.collection.multivalue.LinkedMultiValueMap;
import com.jn.langx.util.collection.multivalue.MultiValueMap;
import com.jn.langx.util.collection.stack.SimpleStack;
import com.jn.langx.util.collection.stack.Stack;
import com.jn.langx.util.function.Consumer;
import com.jn.langx.util.function.Consumer2;
import com.jn.langx.util.function.Function;
import com.jn.langx.util.function.Function2;
import com.jn.langx.util.io.Charsets;
import com.jn.langx.util.reflect.type.Primitives;

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
     */
    public static String toQueryString(Map<String, Object> map, final Map<Class, Function<Object, String>> converterMap) {
        return toQueryString(map, true, null, converterMap);
    }

    public static String toQueryString(Map<String, Object> map, boolean encode, Function2<String, String, String> keyMapper, final Map<Class, Function<Object, String>> converterMap) {
        MultiValueMap<String, String> multiValueMap = toMultiValueMap(map, keyMapper, converterMap);
        return toQueryString(multiValueMap, encode);
    }

    public static String toQueryString(Map<String, String> map) {
        return toQueryString(map, false);
    }

    /**
     * 不会对URL中的特殊字符做处理，如需处理，请调用 UrlEncoder 类
     *
     * @param map
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

    private static MultiValueMap<String, String> toMultiValueMap(Map<String, Object> map, Function2<String, String, String> keyMapper, final Map<Class, Function<Object, String>> converterMap) {
        // 当值为 Object, Array 时，会把值放到 keyPrefixStack 中
        final Stack<String> keyPrefixStack = new SimpleStack<String>();
        keyPrefixStack.push("");
        final MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<String, String>();
        final Function2<String, String, String> keyBuilder = keyMapper == null ? new Function2<String, String, String>() {
            @Override
            public String apply(String keyPrefix, String key) {
                if (Strings.isEmpty(keyPrefix)) {
                    return key;
                }
                if (Strings.isEmpty(key)) {
                    return keyPrefix;
                }
                return keyPrefix + "." + key;
            }
        } : keyMapper;
        Consumer2<String, Object> consumer = new Consumer2<String, Object>() {
            @Override
            public void accept(final String key, Object value) {
                String prefix = keyPrefixStack.peek();
                String handledValue = null;
                if (value != null) {

                    String handledKey = keyBuilder.apply(prefix, key);
                    keyPrefixStack.push(handledKey);

                    Class valueClass = value.getClass();
                    if (converterMap != null && converterMap.containsKey(valueClass)) {
                        Function<Object, String> converter = converterMap.get(valueClass);
                        handledValue = converter.apply(value);
                    } else {
                        if (Primitives.isPrimitiveOrPrimitiveWrapperType(valueClass)) {
                            if (Primitives.isPrimitive(valueClass)) {
                                handledValue = "" + value;
                            } else {
                                handledValue = value.toString();
                            }
                        } else if (byte[].class == valueClass) {
                            handledValue = Base64.encodeBase64String((byte[]) value);
                        } else if (Arrs.isArray(value)) {
                            final Consumer2<String, Object> consumer = this;
                            Collects.forEach(value, new Consumer2<Integer, Object>() {
                                @Override
                                public void accept(Integer index, Object element) {
                                    consumer.accept("", element);
                                }
                            });
                        } else if (value instanceof Map) {
                            Collects.forEach((Map) value, this);
                        } else {
                            handledValue = value.toString();
                        }
                    }
                    if (handledValue != null) {
                        multiValueMap.add(handledKey, handledValue);
                    }
                    keyPrefixStack.pop();
                }
            }
        };
        Collects.forEach(map, consumer);
        return multiValueMap;
    }

}
