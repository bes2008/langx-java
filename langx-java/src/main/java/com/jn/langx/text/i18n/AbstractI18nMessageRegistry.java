package com.jn.langx.text.i18n;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.util.ClassLoaders;
import com.jn.langx.util.Emptys;

import java.util.Locale;

public abstract class AbstractI18nMessageRegistry implements I18nMessageRegistry {
    @Override
    public String getMessage(String key, Object... args) {
        return getMessage((ClassLoader) null, key, args);
    }

    @Override
    public String getMessage(ClassLoader classLoader, String key, Object... args) {
        return getMessage(getLocale(), classLoader, key, args);
    }

    @Override
    public String getMessage(LanguageCode languageCode, String key, Object... args) {
        return getMessage(languageCode, null, key, args);
    }

    @Override
    public String getMessage(LanguageCode languageCode, ClassLoader classLoader, String key, Object... args) {
        return getMessage(toLocale(languageCode), classLoader, key, args);
    }

    @Override
    public String getMessage(LocaleCode localeCode, String key, Object... args) {
        return getMessage(localeCode, null, key, args);
    }

    @Override
    public String getMessage(LocaleCode localeCode, ClassLoader classLoader, String key, Object... args) {
        return getMessage(toLocale(localeCode), classLoader, key, args);
    }

    @Override
    public String getMessage(Locale locale, String key, Object... args) {
        return getMessage(locale, null, key, args);
    }

    @Override
    public String getMessage(Locale locale, ClassLoader classLoader, String key, Object... args) {
        return getMessage(getDefaultBaseName(), locale, classLoader, key, args);
    }

    @Override
    public String getMessage(String basename, Locale locale, String key, Object... args) {
        return getMessage(basename, locale, null, key, args);
    }

    @Override
    public String getMessage(String basename, Locale locale, ClassLoader classLoader, String key, Object... args) {
        return getMessageInternal(getBundleBaseName(basename), toLocale(locale), getClassLoader(classLoader), key, args);
    }

    protected abstract String getMessageInternal(@NonNull String basename, @NonNull Locale locale, @NonNull ClassLoader classLoader, @NonNull String key, Object... args);

    private String getBundleBaseName(String basename) {
        if (Emptys.isEmpty(basename)) {
            return getDefaultBaseName();
        }
        return basename;
    }

    private Locale toLocale(Object locale) {
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

    private ClassLoader getClassLoader(ClassLoader classLoader) {
        if (classLoader == null) {
            return ClassLoaders.getDefaultClassLoader();
        }
        return classLoader;
    }
}
