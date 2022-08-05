package com.jn.langx.util.regexp;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;
import com.jn.langx.registry.GenericRegistry;
import com.jn.langx.text.StringTemplates;
import com.jn.langx.util.Objs;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Consumer;
import com.jn.langx.util.os.Platform;
import com.jn.langx.util.regexp.jdk.JdkRegexpEngine;
import com.jn.langx.util.regexp.named.Jdk6NamedRegexpEngine;

import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.regex.Pattern;

/**
 * @since 4.5.0
 */
public class Regexps {


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

    public static Regexp createRegexp(@Nullable String engineName, @NonNull String pattern, @Nullable Option option) {
        RegexpEngine engine = engineName == null ? null : registry.get(engineName);
        if (engine == null && engineName != null) {
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
            engine = registry.get("joni");
        }
        if (engine == null) {
            engine = registry.get("jdk");
        }
        return engine.get(pattern, option);
    }

    public static boolean match(Regexp regexp, String text) {
        RegexpMatcher matcher = regexp.matcher(text);
        return matcher.matches();
    }

    public static boolean match(String regexp, int flags, String text) {
        return match(createRegexp(regexp, flags), text);
    }

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
        return null;
    }
}
