package com.jn.langx.text.i18n;

public interface HierarchicalI18nMessageStorage extends I18nMessageStorage{
    void setParent(I18nMessageStorage  parent);
    I18nMessageStorage getParent();
}
