package com.jn.langx.security.salt;

import com.jn.langx.util.function.Supplier;

public interface StringSaltGenerator extends Supplier<Integer, String> {
    @Override
    String get(Integer charsLength);
}
