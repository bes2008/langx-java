package com.jn.langx.util.function.mapper.literal;

import com.jn.langx.util.converter.ConverterService;
import com.jn.langx.util.function.mapper.StringLiteralMapper;

public class ToFloatMapper implements StringLiteralMapper<Float> {
    @Override
    public Float apply(String value) {
        return ConverterService.DEFAULT.convert(value, Float.class);
    }
}
