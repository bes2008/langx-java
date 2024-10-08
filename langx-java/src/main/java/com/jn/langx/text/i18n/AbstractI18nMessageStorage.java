package com.jn.langx.text.i18n;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;
import com.jn.langx.util.ClassLoaders;

import java.util.Locale;

public abstract class AbstractI18nMessageStorage implements HierarchicalI18nMessageStorage {
    private Locale locale = Locale.getDefault();
    private I18nMessageStorage parent;

    @Override
    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        if (locale != null) {
            this.locale = locale;
        }
    }

    @Override
    public void setParent(I18nMessageStorage parent) {
        this.parent = parent;
    }

    @Override
    public I18nMessageStorage getParent() {
        return parent;
    }

    @Override
    public String getMessage(@NonNull String key, @Nullable Object[] args) {
        return getMessage(getClassLoader(null), key, args);
    }

    @Override
    public String getMessage(@Nullable ClassLoader classLoader, @NonNull String key, @Nullable Object... args) {
        return getMessage(getLocale(), getClassLoader(classLoader), key, args);
    }

    @Override
    public String getMessage(@Nullable Locale locale, @NonNull String key, @Nullable Object... args) {
        return getMessageInternal(locale, getClassLoader(null), key, args);
    }

    @Override
    public String getMessage(@Nullable Locale locale, @Nullable ClassLoader classLoader, @NonNull String key, @Nullable Object... args) {
        return getMessageInternal(locale, getClassLoader(classLoader), key, args);
    }

    protected abstract String getMessageInternal(@NonNull Locale locale, @NonNull ClassLoader classLoader, @NonNull String key, Object... args);


    protected ClassLoader getClassLoader(ClassLoader classLoader) {
        if (classLoader == null) {
            return ClassLoaders.getDefaultClassLoader();
        }
        return classLoader;
    }
}
