package com.jn.langx.test.util.function.mapper;

import com.jn.langx.util.Objects;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.function.mapper.literal.*;
import org.junit.Assert;
import org.junit.Test;

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
        Assert.assertTrue(Objects.equals(new Byte(value), new ToCharMapper().apply(value)));
    }
}
