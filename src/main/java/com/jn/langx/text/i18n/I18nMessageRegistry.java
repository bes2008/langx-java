package com.jn.langx.text.i18n;


import com.jn.langx.annotation.NonNull;

import java.util.Locale;

public interface I18nMessageRegistry {

    String getDefaultBundleName();

    void setLocal(@NonNull Locale locale);

    @NonNull
    Locale getLocale();

    Locale getLocale(String language);

    String getMessage(String key);

    String getMessage(Locale locale, String key);

    String getMessage(Locale locale, String key, Object... args);

    String getMessage(String bundleName, Locale locale, String key);

    String getMessage(String bundleName, Locale locale, String key, Object... args);

}
