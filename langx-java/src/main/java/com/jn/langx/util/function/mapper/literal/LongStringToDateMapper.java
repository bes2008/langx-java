package com.jn.langx.util.function.mapper.literal;

import com.jn.langx.util.converter.LongToDateConverter;
import com.jn.langx.util.function.mapper.StringLiteralMapper;

import java.util.Date;

public class LongStringToDateMapper implements StringLiteralMapper<Date> {
    @Override
    public Date apply(String value) {
        Long v = new ToLongMapper().apply(value);
        return LongToDateConverter.INSTANCE.apply(v);
    }
}
