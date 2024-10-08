package com.jn.langx.text.i18n;


import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;

import java.util.Locale;

public interface I18nMessageStorage {

    /**
     * 设置默认的locale
     *
     */
    void setLocale(@NonNull Locale locale);

    @NonNull
    Locale getLocale();

    String getMessage(@NonNull String key, @Nullable Object[] args);

    String getMessage(@Nullable ClassLoader classLoader, @NonNull String key, @Nullable Object... args);

    String getMessage(@Nullable Locale locale, @NonNull String key, @Nullable Object... args);

    String getMessage(@Nullable Locale locale, @Nullable ClassLoader classLoader, @NonNull String key, @Nullable Object... args);


}
