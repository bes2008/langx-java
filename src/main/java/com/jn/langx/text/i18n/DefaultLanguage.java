package com.jn.langx.text.i18n;

import com.jn.langx.lifecycle.InitializationException;

import java.util.Locale;

public class DefaultLanguage implements Language {
    private String bundleName;
    private Locale locale;
    private DefaultI18nMessageRegistry i18n;
    private String error;

    public DefaultLanguage() {
        this.i18n = new DefaultI18nMessageRegistry();
    }

    public DefaultLanguage(Class clazz) {
        this.i18n = new DefaultI18nMessageRegistry();
        this.bundleName = clazz.getPackage().getName() + "." + "Messages";

        try {
            this.i18n.init();
        } catch (InitializationException var3) {
            this.error = var3.getMessage();
        }

    }

    public DefaultLanguage(Class clazz, Locale locale) {
        this(clazz);
        this.locale = locale;
    }

    public String getMessage(String key, String... args) {
        if (this.error != null) {
            return this.error;
        } else {
            return args != null && args.length != 0 ? this.i18n.format(this.bundleName, this.locale, key, args) : this.i18n.getString(this.bundleName, this.locale, key);
        }
    }
}
