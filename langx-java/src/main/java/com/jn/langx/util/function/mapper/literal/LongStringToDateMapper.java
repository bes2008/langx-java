package com.jn.langx.util.function.mapper.literal;

import com.jn.langx.util.converter.ConverterService;
import com.jn.langx.util.function.mapper.StringLiteralMapper;

import java.util.Date;

public class LongStringToDateMapper implements StringLiteralMapper<Date> {
    @Override
    public Date apply(String value) {
        Long v = ConverterService.DEFAULT.convert(value, Long.class);
        return ConverterService.DEFAULT.convert(v, Date.class);
    }
}
