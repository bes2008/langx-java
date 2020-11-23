package com.jn.langx.text.i18n;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.Strings;

import java.util.Locale;
import java.util.ResourceBundle;

public class SimpleResourceBundleI18nMessageStorage extends AbstractResourceBundleI18nMessageStorage {
    private ResourceBundle bundle;

    public void setBundle(ResourceBundle bundle) {
        Preconditions.checkNotNull(bundle);
        this.bundle = bundle;
        setLocale(bundle.getLocale());
    }

    @Override
    protected String getMessageInternal(@Nullable final String basename, @Nullable final Locale locale, final @Nullable ClassLoader classLoader, @NonNull String key, Object... args) {
        String message = ResourceBundles.getString(bundle, key, args);
        if (Strings.isBlank(message) && getParent() != null) {
            message = getParent().getMessage(locale, classLoader, key, args);
        }
        return message;
    }

}
