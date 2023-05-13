package com.jn.langx.util.converter;

import com.jn.langx.Converter;
import com.jn.langx.util.Chars;

public class CharacterConverter implements Converter<Object, Character> {

    public static final CharacterConverter INSTANCE = new CharacterConverter();

    @Override
    public boolean isConvertible(Class sourceClass, @SuppressWarnings("rawtypes") Class targetClass) {
        return Character.TYPE == sourceClass || Character.class == sourceClass || String.class == sourceClass || Integer.class == sourceClass;
    }

    @Override
    public Character apply(Object value) {
        if (value == null) {
            return null;
        }
        if (Character.TYPE == value.getClass() || Character.class == value.getClass()) {
            return (Character) value;
        }
        if (Integer.class == value.getClass()) {
            int v = (Integer) value;
            return Chars.from(v);
        }
        if (value.getClass() == String.class) {
            String v = (String) value;
            if (v.isEmpty()) {
                return null;
            }
            return v.charAt(0);
        }
        return null;
    }
}
