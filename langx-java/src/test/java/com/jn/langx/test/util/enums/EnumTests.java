package com.jn.langx.test.util.enums;

import com.jn.langx.util.enums.Enums;
import org.junit.Assert;
import org.junit.Test;

public class EnumTests {
    @Test
    public void test() {
        Assert.assertEquals(Period.DAY, Enums.ofCode(Period.class, 2));
        Assert.assertEquals(Period.DAY, Enums.ofName(Period.class, "day"));
        Assert.assertEquals(Period.DAY, Enums.ofDisplayText(Period.class, "day"));
        Assert.assertEquals(TestLevel.A, Enums.ofName(TestLevel.class, "A"));
        Assert.assertEquals(TestLevel.A, Enums.ofValue(0, TestLevel.class));
    }
}
