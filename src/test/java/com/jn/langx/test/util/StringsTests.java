package com.jn.langx.test.util;

import com.jn.langx.util.Strings;
import org.junit.Assert;
import org.junit.Test;

import static com.jn.langx.util.Strings.isBlank;
import static com.jn.langx.util.Strings.isEmpty;

public class StringsTests {
    @Test
    public void isEmptyTest() {
        Assert.assertTrue(isEmpty(null));
        Assert.assertTrue(isEmpty(""));
        Assert.assertFalse(isEmpty(" "));
        Assert.assertFalse(isEmpty("\t"));
        Assert.assertFalse(isEmpty("\n"));
        Assert.assertFalse(isEmpty("a"));
    }

    @Test
    public void isBlankTest() {
        Assert.assertTrue(isBlank(null));
        Assert.assertTrue(isBlank(""));
        Assert.assertTrue(isBlank(" "));
        Assert.assertTrue(isBlank("\t"));
        Assert.assertTrue(isBlank("\n"));
        Assert.assertFalse(isEmpty("a"));
    }

    @Test
    public void truncateTest() {
        String str = "123456789";
        Assert.assertTrue(Strings.truncate(str, 0).equals(""));
        Assert.assertTrue(Strings.truncate(str, 3).equals("123"));
        Assert.assertTrue(Strings.truncate(str, 9).equals("123456789"));
        Assert.assertTrue(Strings.truncate(str, 20).equals("123456789"));

    }
}
