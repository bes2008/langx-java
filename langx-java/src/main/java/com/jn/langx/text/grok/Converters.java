package com.jn.langx.text.grok;


import com.jn.langx.Converter;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.Maps;
import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.converter.*;
import com.jn.langx.util.function.*;
import com.jn.langx.util.regexp.Regexp;
import com.jn.langx.util.regexp.Regexps;
import com.jn.langx.util.struct.Pair;

import java.util.*;

/**
 * Convert String argument to the right type.
 *
 * 4.7.2
 */
public class Converters {

    public enum Type {
        BYTE(new ByteConverter()),
        BOOLEAN(new BooleanConverter()),
        SHORT(new ShortConverter()),
        INT(new IntegerConverter(), "integer"),
        LONG(new LongConverter()),
        FLOAT(new FloatConverter()),
        DOUBLE(new DoubleConverter()),
        DATETIME(new StringToDateConverter(), "date"),
        STRING(new NoopConverter(), "text");

        public final Converter<?, ?> converter;
        public final List<String> aliases;

        Type(Converter<?, ?> converter, String... aliases) {
            this.converter = converter;
            this.aliases = Arrays.asList(aliases);
        }
    }

    private static final Regexp SPLITTER = Regexps.createRegexp("[:;]");

    private static final Map<String, Converters.Type> TYPES = Pipeline.of(Converters.Type.values())
            .collect(Collects.toMap(new Supplier0<Map<String, Converters.Type>>() {
                @Override
                public Map<String, Converters.Type> get() {
                    return new HashMap<String, Type>();
                }

            }, new Function<Type, String>() {
                @Override
                public String apply(Type t) {
                    return t.name().toLowerCase();
                }
            }, new Function<Type, Type>() {
                @Override
                public Type apply(Type t) {
                    return t;
                }
            }));

    private static final Map<String, Converters.Type> TYPE_ALIASES = Pipeline.of(Type.values())
            .map(new Function<Type, Collection<Map.Entry<String, Type>>>() {
                @Override
                public Collection<Map.Entry<String, Type>> apply(final Type type) {
                    return Collects.map(type.aliases, new Function<String, Map.Entry<String, Type>>() {
                        @Override
                        public Map.Entry<String, Type> apply(String alias) {
                            return new Pair<String, Type>(alias, type);
                        }
                    });
                }
            }).flatMap(new Function<Map.Entry<String, Type>, Map.Entry<String, Type>>() {
                @Override
                public Map.Entry<String, Type> apply(Map.Entry<String, Type> input) {
                    return input;
                }
            }).collect(Collects.toMap(new Supplier0<Map<String, Converters.Type>>() {
                @Override
                public Map<String, Converters.Type> get() {
                    return new HashMap<String, Type>();
                }

            }, new Function<Map.Entry<String, Converters.Type>, String>() {
                @Override
                public String apply(Map.Entry<String, Converters.Type> t) {
                    return t.getKey();
                }
            }, new Function<Map.Entry<String, Converters.Type>, Type>() {
                @Override
                public Type apply(Map.Entry<String, Converters.Type> t) {
                    return t.getValue();
                }
            }));

    private static Converters.Type getType(String key) {
        key = key.toLowerCase();
        Converters.Type type = Maps.get(TYPES, key, new Supplier<String, Type>() {
            @Override
            public Type get(String key) {
                return TYPE_ALIASES.get(key);
            }
        });
        if (type == null) {
            throw new IllegalArgumentException("Invalid data type :" + key);
        }
        return type;
    }

    public static Map<String, Converter> getConverters(Collection<String> groupNames, final Object... params) {
        return Pipeline.of(groupNames)
                .filter(new Predicate<String>() {
                    @Override
                    public boolean test(String groupName) {
                        return containsDelimiter(groupName);
                    }
                })
                .collect(Collects.toHashMap(Functions.<String>noopFunction(), new Function<String, Converter>() {
                    @Override
                    public Converter apply(String key) {
                        String[] list = splitGrokPattern(key);
                        Converter converter = getType(list[1]).converter;
                        if (list.length == 3) {
                            // 先不支持
                            //    converter = converter.newConverter(list[2], params);
                        }
                        return converter;
                    }
                }, true));

    }

    public static Map<String, Converters.Type> getGroupTypes(Collection<String> groupNames) {
        return Pipeline.of(groupNames)
                .filter(new Predicate<String>() {
                    @Override
                    public boolean test(String groupName) {
                        return containsDelimiter(groupName);
                    }
                }).map(new Function<String, String[]>() {
                    @Override
                    public String[] apply(String input) {
                        return splitGrokPattern(input);
                    }
                }).collect(Collects.<String[], String, Converters.Type>toHashMap(new Function<String[], String>() {
                    @Override
                    public String apply(String[] input) {
                        return input[0];
                    }
                }, new Function<String[], Converters.Type>() {
                    @Override
                    public Type apply(String[] input) {
                        return getType(input[1]);
                    }
                }, true));
    }

    public static String extractKey(String key) {
        return splitGrokPattern(key)[0];
    }

    private static boolean containsDelimiter(String string) {
        return string.indexOf(':') >= 0 || string.indexOf(';') >= 0;
    }

    private static String[] splitGrokPattern(String string) {
        return SPLITTER.split(string, 3);
    }

}
