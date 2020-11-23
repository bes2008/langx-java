package com.jn.langx.text.i18n;

import com.jn.langx.util.Strings;

import java.util.Locale;

public class JdkResourceBundleI18nMessageStorage extends AbstractResourceBundleI18nMessageStorage {

    @Override
    protected String getMessageInternal(final String basename, final Locale locale, final ClassLoader classLoader, String key, Object... args) {
        String message = ResourceBundles.getString(basename, locale, classLoader, key, args);
        if (Strings.isBlank(message) && getParent() != null) {
            message = getParent().getMessage(locale, classLoader, key, args);
        }
        return message;
    }
}
