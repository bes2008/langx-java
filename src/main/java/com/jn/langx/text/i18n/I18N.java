package com.jn.langx.text.i18n;


import java.util.Locale;
import java.util.ResourceBundle;

public interface I18N {
    String ROLE = I18N.class.getName();
    String ACCEPT_LANGUAGE = "Accept-Language";

    String getDefaultLanguage();

    String getDefaultCountry();

    String getDefaultBundleName();

    String[] getBundleNames();

    ResourceBundle getBundle();

    ResourceBundle getBundle(String var1);

    ResourceBundle getBundle(String var1, String var2);

    ResourceBundle getBundle(String var1, Locale var2);

    Locale getLocale(String var1);

    String getString(String var1);

    String getString(String var1, Locale var2);

    String getString(String var1, Locale var2, String var3);

    String format(String var1, Object var2);

    String format(String var1, Object var2, Object var3);

    String format(String var1, Locale var2, String var3, Object var4);

    String format(String var1, Locale var2, String var3, Object var4, Object var5);

    String format(String var1, Locale var2, String var3, Object[] var4);
}
