package com.jn.langx.text.i18n;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;
import com.jn.langx.util.Emptys;
import com.jn.langx.util.Objs;

import java.util.Locale;

public abstract class AbstractResourceBundleI18nMessageStorage extends AbstractI18nMessageStorage {
    private String basename = "i18n";

    public String getBaseName() {
        return basename;
    }

    public void setBasename(String basename) {
        if (Emptys.isNotEmpty(basename)) {
            this.basename = basename;
        }
    }

    public String getMessage(@Nullable String basename, @Nullable Locale locale, @NonNull String key, @Nullable Object... args) {
        return getMessage(basename, locale, getClassLoader(null), key, args);
    }

    public String getMessage(@Nullable String basename, @Nullable Locale locale, @Nullable ClassLoader classLoader, @NonNull String key, @Nullable Object... args) {
        return getMessageInternal(getBundleBaseName(basename), Objs.useValueIfEmpty(locale,getLocale()), getClassLoader(classLoader), key, args);
    }

    protected String getMessageInternal(@NonNull Locale locale, @NonNull ClassLoader classLoader, @NonNull String key, Object... args) {
        return getMessageInternal(getBundleBaseName(null), locale, classLoader, key, args);
    }

    protected abstract String getMessageInternal(@NonNull String bundleBaseName, @NonNull Locale locale, @NonNull ClassLoader classLoader, @NonNull String key, Object... args);

    private String getBundleBaseName(String basename) {
        if (Emptys.isEmpty(basename)) {
            return getBaseName();
        }
        return basename;
    }

}
