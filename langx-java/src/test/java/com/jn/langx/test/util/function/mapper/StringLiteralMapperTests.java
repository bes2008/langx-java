package com.jn.langx.test.util.function.mapper;

import com.jn.langx.util.Dates;
import com.jn.langx.util.Objs;
import com.jn.langx.util.converter.ConverterService;
import com.jn.langx.util.function.mapper.literal.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

public class StringLiteralMapperTests {
    @Test
    public void test() {
        String value = "123123";
        Assert.assertTrue(Objs.equals(123123, new ToIntegerMapper().apply(value)));
        Assert.assertTrue(Objs.equals(123123L, new ToLongMapper().apply(value)));
        value = "123123.0";
        Assert.assertTrue(Objs.equals(123123.0F, new ToFloatMapper().apply(value)));
        Assert.assertTrue(Objs.equals(123123.0D, new ToDoubleMapper().apply(value)));
        value = "127";
        Assert.assertTrue(Objs.equals(new Short(value), new ToShortMapper().apply(value)));
        Assert.assertTrue(Objs.equals(new Byte(value), new ToByteMapper().apply(value)));
        Assert.assertTrue(Objs.equals('1', new ToCharMapper().apply(value)));

        long longValue = System.currentTimeMillis();
        value = "" + longValue;
        Assert.assertTrue(Objs.equals(longValue, new ToLongMapper().apply(value)));
        Assert.assertTrue(Objs.equals(longValue, new LongStringToDateMapper().apply(value).getTime()));
        value = Dates.format(longValue, Dates.yyyy_MM_dd_HH_mm_ss_SSS);
        Assert.assertTrue(Objs.equals(longValue, ConverterService.DEFAULT.convert(value, Date.class).getTime()));
    }
}
