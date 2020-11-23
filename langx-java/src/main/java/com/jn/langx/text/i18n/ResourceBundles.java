package com.jn.langx.text.i18n;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.text.StringTemplates;
import com.jn.langx.util.ClassLoaders;
import com.jn.langx.util.Emptys;
import com.jn.langx.util.function.Function2;

import java.util.Locale;
import java.util.ResourceBundle;

public class ResourceBundles {
    private ResourceBundles() {
    }

    public static String getString(@NonNull final String basename, @NonNull String key, Object... args) {
        return getString(basename, Locale.getDefault(), ClassLoaders.getDefaultClassLoader(), key, args);
    }

    public static String getString(@NonNull final String basename, @NonNull Locale locale, @NonNull String key, Object... args) {
        return getString(basename, locale, ClassLoaders.getDefaultClassLoader(), key, args);
    }

    public static String getString(@NonNull final String basename, @NonNull final Locale locale, @NonNull final ClassLoader classLoader, @NonNull final String key, Object... args) {
        ResourceBundle bundle = ResourceBundle.getBundle(basename, locale, classLoader);
        return getString(bundle, key, args);
    }

    public static String getString(final ResourceBundle bundle, String key, Object... args) {
        if (bundle == null) {
            return null;
        }
        String message = bundle.getString(key);
        if (args == null) {
            args = Emptys.EMPTY_OBJECTS;
        }

        message = StringTemplates.format(message, "${", "}", new Function2<String, Object[], String>() {
            @Override
            public String apply(String variable, Object[] args) {
                return ResourceBundles.getString(bundle, variable);
            }
        });

        // https://blog.csdn.net/new03/article/details/84826958
        // 使用 {0},{1},{2}... 来进行参数替换
        //MessageFormat formatter = new MessageFormat(message, locale);
        //message = formatter.format(args);
        message = StringTemplates.formatWithIndex(message, args);
        return message;
    }
}
