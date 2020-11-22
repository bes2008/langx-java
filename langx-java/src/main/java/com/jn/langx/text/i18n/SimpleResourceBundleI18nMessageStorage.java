package com.jn.langx.text.i18n;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;
import com.jn.langx.text.StringTemplates;
import com.jn.langx.util.Emptys;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.function.Function2;

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
        if (bundle == null) {
            return null;
        }
        String message = bundle.getString(key);
        if (args == null) {
            args = Emptys.EMPTY_OBJECTS;
        }

        message = StringTemplates.format(message, "${", "}", new Function2<String, Object[], String>() {
            @Override
            public String apply(String variable, Object[] args) {
                return getMessageInternal(basename, locale, classLoader, variable);
            }
        });

        // https://blog.csdn.net/new03/article/details/84826958
        // 使用 {0},{1},{2}... 来进行参数替换
        //MessageFormat formatter = new MessageFormat(message, locale);
        //message = formatter.format(args);
        message = StringTemplates.formatWithIndex(message, args);
        return message;
    }

}
