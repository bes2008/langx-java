package com.jn.langx.text.i18n;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.util.Preconditions;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public class JdkResourceBundleI18nRegistry extends AbstractI18nMessageRegistry {
    private static final Object[] NO_ARGS = new Object[0];
    private String defaultBundleBaseName = "i18n";
    private Locale defaultLocale = Locale.getDefault();

    public JdkResourceBundleI18nRegistry() {
    }

    @Override
    public void setLocal(Locale locale) {
        this.defaultLocale = locale;
    }

    public String getDefaultBaseName() {
        return this.defaultBundleBaseName;
    }


    public void setLocale(@NonNull Locale locale) {
        Preconditions.checkNotNull(locale);
        this.defaultLocale = locale;
    }

    @NonNull
    public Locale getLocale() {
        return this.defaultLocale;
    }


    @Override
    protected String getMessageInternal(String basename, Locale locale, ClassLoader classLoader, String key, Object... args) {

        ResourceBundle bundle = ResourceBundle.getBundle(basename, locale, classLoader);
        if (bundle == null) {
            return null;
        }
        String message = bundle.getString(key);
        if (args == null) {
            args = NO_ARGS;
        }
        MessageFormat messageFormat = new MessageFormat("");
        messageFormat.setLocale(locale);
        messageFormat.applyPattern(message);
        return messageFormat.format(args);
    }
}
