package com.jn.langx.util.enums;

import java.util.List;

public interface EnumGetter {
    List<Class> applyTo();

    Enum getByName(Class enumClass, String name);

    Enum getByCode(Class enumClass, int code);

    Enum getByDisplayText(Class enumClass, String displayText);

    Enum getByToString(Class enumClass, String toString);

    String getName(Enum e);

    String getDisplayText(Enum e);

    int getCode(Enum e);
}
