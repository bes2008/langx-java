package com.jn.langx.test.util.pattern;

import com.jn.langx.util.pattern.patternset.AntPathMatcher;
import com.jn.langx.util.pattern.regexp.RegexpMatcher;
import org.junit.Assert;
import org.junit.Test;

public class PatternTests {
    @Test
    public void testRegexpMatcher() {
        RegexpMatcher regExpMatcher = new RegexpMatcher("^a.x$");

        Assert.assertEquals(false, regExpMatcher.match("11111"));
        Assert.assertEquals(false, regExpMatcher.match("a01"));
        Assert.assertEquals(false, regExpMatcher.match("b0x"));
        Assert.assertEquals(true, regExpMatcher.match("a0x"));
        Assert.assertEquals(true, regExpMatcher.match("a.x"));
        Assert.assertEquals(false, regExpMatcher.match("a0.x"));
    }

    @Test
    public void antStringPatternSetTests() {
        AntPathMatcher antStyleStringMatcher = new AntPathMatcher((String) null, "/views/products/**/*.cfm");
        Assert.assertEquals(true, antStyleStringMatcher.match("/views/products/index.cfm"));
        Assert.assertEquals(true, antStyleStringMatcher.match("/views/products/SE10/index.cfm"));
        Assert.assertEquals(true, antStyleStringMatcher.match("/views/products/SE10/details.cfm"));
        Assert.assertEquals(true, antStyleStringMatcher.match("/views/products/ST80/index.cfm"));
        Assert.assertEquals(true, antStyleStringMatcher.match("/views/products/ST80/details.cfm"));


        Assert.assertEquals(false, antStyleStringMatcher.match("/views/index.cfm"));
        Assert.assertEquals(false, antStyleStringMatcher.match("/views/aboutUs/index.cfm"));
        Assert.assertEquals(false, antStyleStringMatcher.match("/views/aboutUs/managementTeam.cfm"));
    }

}
