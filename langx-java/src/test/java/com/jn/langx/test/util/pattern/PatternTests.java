package com.jn.langx.test.util.pattern;

import com.jn.langx.util.pattern.patternset.AntPathMatcher;
import com.jn.langx.util.pattern.regexp.RegexpMatcher;
import org.junit.Assert;
import org.junit.Test;

public class PatternTests {
    @Test
    public void testRegexpMatcher() {
        RegexpMatcher regExpMatcher = new RegexpMatcher("^a.x$");

        Assert.assertEquals(false, regExpMatcher.matches("11111"));
        Assert.assertEquals(false, regExpMatcher.matches("a01"));
        Assert.assertEquals(false, regExpMatcher.matches("b0x"));
        Assert.assertEquals(true, regExpMatcher.matches("a0x"));
        Assert.assertEquals(true, regExpMatcher.matches("a.x"));
        Assert.assertEquals(false, regExpMatcher.matches("a0.x"));
    }

    @Test
    public void antStringPatternSetTests() {
        AntPathMatcher antStyleStringMatcher = new AntPathMatcher((String) null, "/views/**/*.cfm");
        antStyleStringMatcher.setGlobal(true);
        Assert.assertEquals(true, antStyleStringMatcher.matches("/views/products/index.cfm"));
        Assert.assertEquals(true, antStyleStringMatcher.matches("/views/products/SE10/index.cfm"));
        Assert.assertEquals(true, antStyleStringMatcher.matches("/views/products/SE10/details.cfm"));
        Assert.assertEquals(true, antStyleStringMatcher.matches("/views/products/ST80/index.cfm"));
        Assert.assertEquals(true, antStyleStringMatcher.matches("/views/products/ST80/details.cfm"));
        Assert.assertEquals(false, antStyleStringMatcher.matches("/views/products/ST80/details.xyz"));


        Assert.assertEquals(true, antStyleStringMatcher.matches("/views/index.cfm"));
        Assert.assertEquals(true, antStyleStringMatcher.matches("/views/aboutUs/index.cfm"));
        Assert.assertEquals(true, antStyleStringMatcher.matches("/views/aboutUs/managementTeam.cfm"));
    }

    @Test
    public void antStringPatternSetTests2() {
        AntPathMatcher antStyleStringMatcher = new AntPathMatcher((String) null, "/views/products/**/*.cfm");
        antStyleStringMatcher.setGlobal(true);
        Assert.assertEquals(true, antStyleStringMatcher.matches("/views/products/index.cfm"));
        Assert.assertEquals(true, antStyleStringMatcher.matches("/views/products/SE10/index.cfm"));
        Assert.assertEquals(true, antStyleStringMatcher.matches("/views/products/SE10/details.cfm"));
        Assert.assertEquals(true, antStyleStringMatcher.matches("/views/products/ST80/index.cfm"));
        Assert.assertEquals(true, antStyleStringMatcher.matches("/views/products/ST80/details.cfm"));
        Assert.assertEquals(false, antStyleStringMatcher.matches("/views/products/ST80/details.xyz"));


        Assert.assertEquals(false, antStyleStringMatcher.matches("/views/index.cfm"));
        Assert.assertEquals(false, antStyleStringMatcher.matches("/views/aboutUs/index.cfm"));
        Assert.assertEquals(false, antStyleStringMatcher.matches("/views/aboutUs/managementTeam.cfm"));
    }


}
