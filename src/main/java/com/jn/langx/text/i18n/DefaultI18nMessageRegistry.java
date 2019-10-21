package com.jn.langx.text.i18n;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;
import com.jn.langx.lifecycle.Initializable;
import com.jn.langx.lifecycle.InitializationException;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.Strings;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class DefaultI18nMessageRegistry implements I18nMessageRegistry, Initializable {
    private static final Object[] NO_ARGS = new Object[0];
    // Map<bundleName, Map<language, Map<key, message>>>
    private String defaultBundleName;
    private Locale defaultLocale = Locale.getDefault();

    public DefaultI18nMessageRegistry() {
    }

    @Override
    public void setLocal(Locale locale) {
        this.defaultLocale = locale;
    }

    public String getDefaultBundleName() {
        return this.defaultBundleName;
    }


    protected ResourceBundle getBundle(@Nullable String bundleName) {
        return this.getBundle(bundleName, getLocale());
    }

    protected ResourceBundle getBundle(@Nullable String bundleName, @Nullable String languageHeader) {
        return this.getBundle(bundleName, getLocale(languageHeader));
    }

    protected ResourceBundle getBundle(@Nullable String bundleName, @Nullable Locale locale) {
        bundleName = Strings.isBlank(bundleName) ? this.getDefaultBundleName() : bundleName.trim();
        if (locale == null) {
            locale = this.getLocale();
        }
        return cacheBundle(bundleName, locale);

    }

    public void setLocale(@NonNull Locale locale) {
        Preconditions.checkNotNull(locale);
        this.defaultLocale = locale;
    }

    @NonNull
    public Locale getLocale() {
        return this.defaultLocale;
    }

    public Locale getLocale(String header) {
        if (!Strings.isEmpty(header)) {
            I18nTokenizer tok = new I18nTokenizer(header);
            if (tok.hasNext()) {
                return (Locale) tok.next();
            }
        }

        return this.defaultLocale;
    }

    public String getMessage(String key) {
        return this.getMessage(null, key);
    }

    public String getMessage(Locale locale, String key) {
        return this.getMessage(this.getDefaultBundleName(), locale, key);
    }

    public String getMessage(String bundleName, Locale locale, String key) {
        ResourceBundle bundle = cacheBundle(bundleName, locale);
        return bundle.getString(key);
    }

    public String getMessage(String bundleName, Locale locale, String key, Object... args) {
        String value = this.getMessage(bundleName, locale, key);
        if (args == null) {
            args = NO_ARGS;
        }

        MessageFormat messageFormat = new MessageFormat("");
        messageFormat.setLocale(locale);
        messageFormat.applyPattern(value);
        return messageFormat.format(args);
    }

    public void init() throws InitializationException {

    }

    protected void initializeBundleNames() {

    }

    private synchronized ResourceBundle cacheBundle(String bundleName, Locale locale) throws MissingResourceException {
        return ResourceBundle.getBundle(bundleName, locale);
    }

    private ResourceBundle getBundleIgnoreException(String bundleName, Locale locale) {
        try {
            return ResourceBundle.getBundle(bundleName, locale);
        } catch (MissingResourceException var4) {
            return null;
        }
    }

    protected final String getStringOrNull(ResourceBundle rb, String key) {
        if (rb != null) {
            try {
                return rb.getString(key);
            } catch (MissingResourceException var4) {
                // NOOP
            }
        }

        return null;
    }
}
