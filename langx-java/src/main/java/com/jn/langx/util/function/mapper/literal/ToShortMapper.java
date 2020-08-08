package com.jn.langx.util.function.mapper.literal;

import com.jn.langx.util.converter.ConverterService;
import com.jn.langx.util.function.mapper.StringLiteralMapper;

public class ToShortMapper implements StringLiteralMapper<Short> {
    @Override
    public Short apply(String value) {
        return ConverterService.DEFAULT.convert(value, Short.class);
    }
}
