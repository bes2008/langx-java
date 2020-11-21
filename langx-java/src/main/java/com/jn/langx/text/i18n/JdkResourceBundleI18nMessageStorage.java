package com.jn.langx.text.i18n;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.text.StringTemplates;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.function.Function2;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public class JdkResourceBundleI18nMessageStorage extends AbstractI18nMessageStorage {
    private static final Object[] NO_ARGS = new Object[0];
    private String defaultBundleBaseName = "i18n";
    private Locale defaultLocale = Locale.getDefault();

    public JdkResourceBundleI18nMessageStorage() {
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
    protected String getMessageInternal(final String basename, final Locale locale, final ClassLoader classLoader, String key, Object... args) {

        ResourceBundle bundle = ResourceBundle.getBundle(basename, locale, classLoader);
        if (bundle == null) {
            return null;
        }
        String message = bundle.getString(key);
        if (args == null) {
            args = NO_ARGS;
        }
        // https://blog.csdn.net/new03/article/details/84826958
        // 使用 {0},{1},{2}... 来进行参数替换
        //MessageFormat formatter = new MessageFormat(message, locale);
        //message = formatter.format(args);

        message = StringTemplates.formatWithIndex(message, args);
        return StringTemplates.format(message, "${", "}", new Function2<String, Object[], String>() {
            @Override
            public String apply(String variable, Object[] args) {
                return getMessageInternal(basename, locale, classLoader, variable);
            }
        });
    }
}
