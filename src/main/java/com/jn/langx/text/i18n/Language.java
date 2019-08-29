package com.jn.langx.text.i18n;

public interface Language {
    String ROLE = Language.class.getName();
    String DEFAULT_NAME = "Messages";

    String getMessage(String var1, String... var2);
}
