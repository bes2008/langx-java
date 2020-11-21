package com.jn.langx.text.i18n;


import com.jn.langx.annotation.NonNull;

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

    String getMessage(String key, Object... args);

    String getMessage(ClassLoader classLoader, String key, Object... args);


    String getMessage(LanguageCode languageCode, String key, Object... args);

    String getMessage(LanguageCode languageCode, ClassLoader classLoader, String key, Object... args);


    String getMessage(LocaleCode localeCode, String key, Object... args);

    String getMessage(LocaleCode localeCode, ClassLoader classLoader, String key, Object... args);


    String getMessage(Locale locale, String key, Object... args);

    String getMessage(Locale locale, ClassLoader classLoader, String key, Object... args);


    String getMessage(String bundleName, Locale locale, String key, Object... args);

    String getMessage(String bundleName, Locale locale, ClassLoader classLoader, String key, Object... args);

}
