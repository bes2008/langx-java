package com.jn.langx.text.i18n;

import java.util.Locale;

public class JdkResourceBundleI18nMessageStorage extends AbstractResourceBundleI18nMessageStorage {

    @Override
    protected String getMessageInternal(final String basename, final Locale locale, final ClassLoader classLoader, String key, Object... args) {
        return ResourceBundles.getString(basename, locale, classLoader, key, args);
    }
}
