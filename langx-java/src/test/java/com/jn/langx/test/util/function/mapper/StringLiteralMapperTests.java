package com.jn.langx.test.util.function.mapper;

import com.jn.langx.util.Dates;
import com.jn.langx.util.Objects;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.converter.ConverterService;
import com.jn.langx.util.function.mapper.literal.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

public class StringLiteralMapperTests {
    @Test
    public void test() {
        String value = "123123";
        Assert.assertTrue(Objects.equals(123123, new ToIntegerMapper().apply(value)));
        Assert.assertTrue(Objects.equals(123123L, new ToLongMapper().apply(value)));
        value="123123.0";
        Assert.assertTrue(Objects.equals(123123.0F, new ToFloatMapper().apply(value)));
        Assert.assertTrue(Objects.equals(123123.0D, new ToDoubleMapper().apply(value)));
        value="127";
        Assert.assertTrue(Objects.equals(new Short(value), new ToShortMapper().apply(value)));
        Assert.assertTrue(Objects.equals(new Byte(value), new ToByteMapper().apply(value)));
        Assert.assertTrue(Objects.equals('1', new ToCharMapper().apply(value)));

        long longValue =System.currentTimeMillis();
        value = ""+longValue;
        Assert.assertTrue(Objects.equals(longValue, new ToLongMapper().apply(value)));
        Assert.assertTrue(Objects.equals(longValue, new LongStringToDateMapper().apply(value).getTime()));
        value = Dates.format(longValue, Dates.yyyy_MM_dd_HH_mm_ss_SSS);
        Assert.assertTrue(Objects.equals(longValue, ConverterService.DEFAULT.convert(value, Date.class).getTime()));
    }
}
