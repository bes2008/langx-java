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

    @Test
    public void splitTest() {
        String string = "a, b, 23, af, (, {, }, 323";

        String[] segments = Strings.split(string, "");
        Assert.assertTrue(segments.length > 8);

        segments = Strings.split(string, ",");
        Assert.assertTrue(segments.length == 8);

        segments = Strings.split(string, " ");
        Assert.assertTrue(segments.length == 8);

        string = "system0@*v*@0share-ns-org-10@*v*@0i632d4c-tomcat-00@*v*@0tomcat";
        segments = Strings.split(string, "0@*v*@0");
        // 分割后会有Bug
        Assert.assertEquals(segments[2], "i632d4c-tomcat-0");

    }

    @Test
    public void testUpperCaseTests() {
        String str = "abcdefghklm_nopRstUvwx_yz";
        System.out.println(Strings.upperCase(str, 0, 1));
        System.out.println(Strings.upperCase(str, 2, 7));
        System.out.println(Strings.upperCase(str, 9, 20));
    }

    @Test
    public void testIndexOf() {
        String str = "abcde123fg34hklm_34534nopRst234Uvw4x_yz";
        System.out.println(Strings.indexOf(str, '4'));
        System.out.println(Strings.indexOf(str, '4', 20));
        System.out.println(Strings.indexOf(str, "nop"));
        System.out.println(Strings.indexOf(str, "nop", 20));
    }

    @Test
    public void testJoin() {
        String string = "a, b, 23, af, (, {, }, 323";
        String[] segments = Strings.split(string, ",");
        String newString = Strings.join(", ", segments);
        System.out.println(newString);
    }

    @Test
    public void testContains() {
        System.out.println(Strings.containsAny("%(tls)", "%,()"));
    }

}
