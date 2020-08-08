package com.jn.langx.util.function.mapper.literal;

import com.jn.langx.util.converter.ConverterService;
import com.jn.langx.util.function.mapper.StringLiteralMapper;

public class ToDoubleMapper implements StringLiteralMapper<Double> {
    @Override
    public Double apply(String value) {
        return ConverterService.DEFAULT.convert(value, Double.class);
    }
}
