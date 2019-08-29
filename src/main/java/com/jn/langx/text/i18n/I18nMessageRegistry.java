package com.jn.langx.text.i18n;


import java.util.Locale;

public interface I18nMessageRegistry {
    String ROLE = I18nMessageRegistry.class.getName();
    String ACCEPT_LANGUAGE = "Accept-Language";

    String getDefaultLanguage();

    String getDefaultCountry();

    String getDefaultBundleName();

    Locale getLocale();

    Locale getLocale(String language);

    String getMessage(String key);

    String getMessage(Locale locale, String key);

    String getMessage(String bundleName, Locale locale, String key);

    String getMessage(String bundleName, Locale locale, String key, Object... args);

}
