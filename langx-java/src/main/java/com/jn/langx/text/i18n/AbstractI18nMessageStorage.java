package com.jn.langx.text.i18n;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;
import com.jn.langx.util.ClassLoaders;

import java.util.Locale;

public abstract class AbstractI18nMessageStorage implements I18nMessageStorage {
    @Override
    public String getMessage(@NonNull String key, @Nullable Object[] args) {
        return getMessage((ClassLoader) null, key, args);
    }

    @Override
    public String getMessage(@Nullable ClassLoader classLoader, @NonNull String key, @Nullable Object... args) {
        return getMessage(getLocale(), classLoader, key, args);
    }

    @Override
    public String getMessage(@Nullable LanguageCode languageCode, @NonNull String key, @Nullable Object... args) {
        return getMessage(languageCode, null, key, args);
    }

    @Override
    public String getMessage(@Nullable LanguageCode languageCode, @Nullable ClassLoader classLoader, @NonNull String key, @Nullable Object... args) {
        return getMessage(toLocale(languageCode), classLoader, key, args);
    }

    @Override
    public String getMessage(@Nullable LocaleCode localeCode, @NonNull String key, @Nullable Object... args) {
        return getMessage(localeCode, null, key, args);
    }

    @Override
    public String getMessage(@Nullable LocaleCode localeCode, @Nullable ClassLoader classLoader, @NonNull String key, @Nullable Object... args) {
        return getMessage(toLocale(localeCode), classLoader, key, args);
    }

    @Override
    public String getMessage(@Nullable Locale locale, @NonNull String key, @Nullable Object... args) {
        return getMessageInternal(locale, null, key, args);
    }

    @Override
    public String getMessage(@Nullable Locale locale, @Nullable ClassLoader classLoader, @NonNull String key, @Nullable Object... args) {
        return getMessageInternal(locale, classLoader, key, args);
    }

    protected abstract String getMessageInternal(@NonNull Locale locale, @NonNull ClassLoader classLoader, @NonNull String key, Object... args);


    protected Locale toLocale(Object locale) {
        if (locale == null) {
            return getLocale();
        }
        if (locale instanceof Locale) {
            return (Locale) locale;
        }
        if (locale instanceof LocaleCode) {
            return ((LocaleCode) locale).toLocale();
        }
        if (locale instanceof LanguageCode) {
            return ((LanguageCode) locale).toLocale();
        }
        return getLocale();
    }

    protected ClassLoader getClassLoader(ClassLoader classLoader) {
        if (classLoader == null) {
            return ClassLoaders.getDefaultClassLoader();
        }
        return classLoader;
    }
}
