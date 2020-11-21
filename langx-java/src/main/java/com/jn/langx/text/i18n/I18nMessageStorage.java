package com.jn.langx.text.i18n;


import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;

import java.util.Locale;

public interface I18nMessageStorage {

    String getDefaultBaseName();

    /**
     * 设置默认的locale
     *
     * @param locale
     */
    void setLocal(@NonNull Locale locale);

    @NonNull
    Locale getLocale();

    String getMessage(@NonNull String key, @Nullable Object[] args);

    String getMessage(@Nullable ClassLoader classLoader, @NonNull String key, @Nullable Object... args);


    String getMessage(@Nullable LanguageCode languageCode, @NonNull String key, @Nullable Object... args);

    String getMessage(@Nullable LanguageCode languageCode, @Nullable ClassLoader classLoader, @NonNull String key, @Nullable Object... args);


    String getMessage(@Nullable LocaleCode localeCode, @NonNull String key, @Nullable Object... args);

    String getMessage(@Nullable LocaleCode localeCode, @Nullable ClassLoader classLoader, @NonNull String key, @Nullable Object... args);


    String getMessage(@Nullable Locale locale, @NonNull String key, @Nullable Object... args);

    String getMessage(@Nullable Locale locale, @Nullable ClassLoader classLoader, @NonNull String key, @Nullable Object... args);


    String getMessage(@Nullable String basename,@Nullable Locale locale,  @NonNull String key, @Nullable Object... args);

    String getMessage(@Nullable String basename,@Nullable Locale locale,  @Nullable ClassLoader classLoader, @NonNull String key, @Nullable Object... args);

}
