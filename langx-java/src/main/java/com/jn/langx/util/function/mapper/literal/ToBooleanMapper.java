package com.jn.langx.util.function.mapper.literal;

import com.jn.langx.util.converter.ConverterService;
import com.jn.langx.util.function.mapper.StringLiteralMapper;

public class ToBooleanMapper implements StringLiteralMapper<Boolean> {
    @Override
    public Boolean apply(String value) {
        return ConverterService.DEFAULT.convert(value, Boolean.class);
    }
}
