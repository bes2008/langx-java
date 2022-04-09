package com.jn.langx.util.regexp;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;
import com.jn.langx.registry.GenericRegistry;
import com.jn.langx.text.StringTemplates;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Consumer;
import com.jn.langx.util.os.Platform;
import com.jn.langx.util.regexp.jdk.JdkRegexpFactory;
import com.jn.langx.util.regexp.named.Jdk6NamedRegexpFactory;

import java.util.ServiceLoader;

/**
 * @since 4.5.0
 */
public class Regexps {


    private static final GenericRegistry<RegexpFactory> registry = new GenericRegistry<RegexpFactory>();

    static {
        if (Platform.JAVA_VERSION_INT < 7) {
            registry.register(new Jdk6NamedRegexpFactory());
        } else {
            registry.register(new JdkRegexpFactory());
        }
        // SPI for joni or others
        Collects.forEach(ServiceLoader.load(RegexpFactory.class), new Consumer<RegexpFactory>() {
            @Override
            public void accept(RegexpFactory regexpFactory) {
                registry.register(regexpFactory);
            }
        });
        registry.init();
    }

    public static Regexp createRegexp(String pattern) {
        return createRegexp(null, pattern, null);
    }

    public static Regexp createRegexp(String pattern, Option option) {
        return createRegexp(null, pattern, option);
    }

    public static Regexp createRegexp(@Nullable String engine, @NonNull String pattern, @Nullable Option option) {
        Preconditions.checkNotNull(pattern);
        if (Strings.isBlank(engine)) {
            engine = "jdk";
        }
        if (option == null) {
            option = Option.DEFAULT;
        }
        RegexpFactory factory = registry.get(engine);
        if (factory == null) {
            throw new RuntimeException(StringTemplates.formatWithPlaceholder("not found regexp engine {}", engine));
        }
        return factory.get(pattern, option);
    }
}
