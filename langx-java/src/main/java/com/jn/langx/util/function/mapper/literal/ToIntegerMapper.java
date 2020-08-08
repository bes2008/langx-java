package com.jn.langx.util.function.mapper.literal;

import com.jn.langx.util.converter.ConverterService;
import com.jn.langx.util.function.mapper.StringLiteralMapper;

public class ToIntegerMapper implements StringLiteralMapper<Integer> {
    @Override
    public Integer apply(String value) {
        return ConverterService.DEFAULT.convert(value, Integer.class);
    }
}