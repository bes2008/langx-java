package com.jn.langx.util.function.mapper.literal;

import com.jn.langx.util.converter.ConverterService;
import com.jn.langx.util.function.mapper.StringLiteralMapper;

public class ToByteMapper implements StringLiteralMapper<Byte> {
    @Override
    public Byte apply(String value) {
        return ConverterService.DEFAULT.convert(value, Byte.class);
    }
}
