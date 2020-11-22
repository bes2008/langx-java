package com.jn.langx.text.i18n;

import com.jn.langx.text.StringTemplates;
import com.jn.langx.util.Emptys;
import com.jn.langx.util.function.Function2;

import java.util.Locale;
import java.util.ResourceBundle;

public class JdkResourceBundleI18nMessageStorage extends AbstractResourceBundleI18nMessageStorage {

    @Override
    protected String getMessageInternal(final String basename, final Locale locale, final ClassLoader classLoader, String key, Object... args) {

        ResourceBundle bundle = ResourceBundle.getBundle(basename, locale, classLoader);
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
