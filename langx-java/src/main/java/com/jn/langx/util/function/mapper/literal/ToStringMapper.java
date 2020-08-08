package com.jn.langx.util.function.mapper.literal;

import com.jn.langx.util.function.mapper.StringLiteralMapper;

public class ToStringMapper implements StringLiteralMapper<String> {
    @Override
    public String apply(String value) {
        return value;
    }
}