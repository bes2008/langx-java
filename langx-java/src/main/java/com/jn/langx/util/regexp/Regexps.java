package com.jn.langx.util.regexp;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;
import com.jn.langx.registry.GenericRegistry;
import com.jn.langx.text.StringTemplates;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Consumer;
import com.jn.langx.util.os.Platform;
import com.jn.langx.util.regexp.jdk.JdkRegexpEngine;
import com.jn.langx.util.regexp.named.Jdk6NamedRegexpEngine;

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

    public static Regexp createRegexp(String pattern) {
        return createRegexp(pattern, null);
    }

    public static Regexp createRegexp(Pattern pattern) {
        return createRegexp((String) null, pattern.pattern(), Option.buildOption(pattern.flags()));
    }

    public static Regexp createRegexp(String pattern, Option option) {
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
}
