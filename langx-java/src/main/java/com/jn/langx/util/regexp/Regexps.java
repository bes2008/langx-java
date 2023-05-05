package com.jn.langx.util.regexp;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;
import com.jn.langx.registry.GenericRegistry;
import com.jn.langx.text.StringTemplates;
import com.jn.langx.util.Objs;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.Maps;
import com.jn.langx.util.collection.MultiKeyMap;
import com.jn.langx.util.function.Consumer;
import com.jn.langx.util.os.Platform;
import com.jn.langx.util.regexp.jdk.JdkRegexpEngine;
import com.jn.langx.util.regexp.named.Jdk6NamedRegexpEngine;

import java.util.*;
import java.util.regex.Pattern;

/**
 * @since 4.5.0
 */
public class Regexps {

    /**
     * 完全等价于 == Pattern.quote(s)
     */
    public static String quote(String s) {
        return Pattern.quote(s);
    }

    /**
     * key1: pattern string
     * key2: Option
     * key3: engine
     *
     * @since 4.7.5
     */
    private static MultiKeyMap<Regexp> cache = new MultiKeyMap<Regexp>();

    private static final GenericRegistry<RegexpEngine> registry = new GenericRegistry<RegexpEngine>();

    static {
        if (Platform.JAVA_VERSION_INT < 7) {
            registry.register(new Jdk6NamedRegexpEngine());
        } else {
            registry.register(new JdkRegexpEngine());
        }
        // SPI for joni or others
        Collects.forEach(ServiceLoader.load(RegexpEngine.class), new Consumer<RegexpEngine>() {
            @Override
            public void accept(RegexpEngine regexpFactory) {
                registry.register(regexpFactory);
            }
        });
        registry.init();
    }

    private Regexps() {

    }

    /**
     * @since 4.7.5
     */
    public static Regexp compile(@NonNull String pattern) {
        return createRegexp(pattern);
    }

    public static Regexp createRegexp(@NonNull String pattern) {
        return createRegexp(pattern, (Option) null);
    }

    public static Regexp createRegexp(@NonNull Pattern pattern) {
        return createRegexp((String) null, pattern.pattern(), Option.buildOption(pattern.flags()));
    }

    public static Regexp createRegexp(@NonNull String pattern, String flags) {
        return createRegexp(pattern, Option.fromJavaScriptFlags(flags));
    }

    public static Regexp createRegexp(@NonNull String pattern, int flags) {
        return createRegexp(pattern, Option.buildOption(flags));
    }

    public static Regexp createRegexp(@NonNull String pattern, @Nullable Option option) {
        return createRegexp((String) null, pattern, option);
    }

    public static Regexp createRegexpWithEngine(@Nullable String engineName, @NonNull String pattern) {
        return createRegexp(engineName, pattern, null);
    }

    public static Regexp createRegexp(@Nullable String engineName, @NonNull String pattern, @Nullable Option option) {
        RegexpEngine engine = Strings.isEmpty(engineName) ? null : registry.get(engineName);
        if (engine == null && Strings.isNotEmpty(engineName)) {
            throw new RuntimeException(StringTemplates.formatWithPlaceholder("not found regexp engine {}", engineName));
        }
        return createRegexp(engine, pattern, option);
    }

    public static Regexp createRegexp(@Nullable RegexpEngine engine, @NonNull String pattern, @Nullable Option option) {
        Preconditions.checkNotNull(pattern);
        if (option == null) {
            option = Option.DEFAULT;
        }
        if (engine == null) {
            engine = getRegexpEngine("joni");
        }

        Regexp regexp = cache.get(pattern, option, engine.getName());
        if (regexp == null) {
            regexp = engine.get(pattern, option);
            cache.put(pattern, option, engine.getName(), regexp);
        }
        return regexp;
    }

    public static RegexpEngine findRegexpEngine(String engine){
        return getRegexpEngine(engine,"UNKNOWN");
    }

    public static RegexpEngine getRegexpEngine(String engine){
        return getRegexpEngine(engine,"jdk");
    }

    public static RegexpEngine getRegexpEngine(String engineName, String defaultEngine){
        RegexpEngine engine = null;
        engine = registry.get(engineName);
        if (engine == null) {
            engine = registry.get(defaultEngine);
        }
        return engine;
    }

    /**
     * 判断 文本中 是否包含指定的 正则可匹配的内容
     *
     * @since 4.7.0
     */
    public static boolean contains(String text, Regexp regexp) {
        RegexpMatcher matcher = regexp.matcher(text);
        return matcher.find();
    }

    /**
     * 判断文本与正则是否匹配
     */
    public static boolean match(Regexp regexp, String text) {
        RegexpMatcher matcher = regexp.matcher(text);
        return matcher.matches();
    }

    /**
     * 判断文本与正则是否匹配
     */
    public static boolean match(String regexp, int flags, String text) {
        return match(createRegexp(regexp, flags), text);
    }

    /**
     * 判断文本与正则是否匹配
     */
    public static boolean match(String regexp, String text) {
        return match(createRegexp(regexp), text);
    }

    /**
     * @since 4.6.14
     */
    public static List<Map<String, String>> findNamedGroups(Regexp regexp, String text) {
        RegexpMatcher matcher = regexp.matcher(text);
        return matcher.namedGroups();
    }

    /**
     * @since 4.6.14
     */
    public static Map<String, String> findNamedGroup(Regexp regexp, String text) {
        List<Map<String, String>> groupsList = findNamedGroups(regexp, text);
        if (Objs.isNotEmpty(groupsList)) {
            return groupsList.get(0);
        }
        return Maps.newHashMap();
    }

    public static Map<String, String> namedGroups(RegexpMatcher matcher, Set<String> groupNames) {
        Map<String, String> namedGroups = new LinkedHashMap<String, String>();
        for (String groupName : groupNames) {
            String groupValue = matcher.group(groupName);
            namedGroups.put(groupName, groupValue);
        }
        return namedGroups;
    }
}
