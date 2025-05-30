package com.jn.langx.security.crypto.pbe.pbkdf;

import com.jn.langx.Converter;

/**
 * @since 5.5.0
 */
public abstract class PasswordConverter implements Converter<char[], byte[]> {
    @Override
    public boolean isConvertible(Class sourceClass, Class targetClass) {
        if (sourceClass != char[].class) {
            return false;
        }
        if (targetClass != byte[].class) {
            return false;
        }
        return true;
    }

    @Override
    public abstract byte[] apply(char[] input);
}
