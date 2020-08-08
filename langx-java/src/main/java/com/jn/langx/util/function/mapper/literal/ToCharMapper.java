package com.jn.langx.util.function.mapper.literal;

import com.jn.langx.util.converter.ConverterService;
import com.jn.langx.util.function.mapper.StringLiteralMapper;

public class ToCharMapper implements StringLiteralMapper<Character> {
    @Override
    public Character apply(String value) {
        return ConverterService.DEFAULT.convert(value, Character.class);
    }
}
