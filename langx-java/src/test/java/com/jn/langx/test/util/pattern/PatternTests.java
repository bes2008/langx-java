package com.jn.langx.test.util.pattern;

import com.jn.langx.util.pattern.patternset.AntStyleStringMatcher;
import com.jn.langx.util.pattern.regexp.RegExpMatcher;
import org.junit.Assert;
import org.junit.Test;

public class PatternTests {
    @Test
    public void testRegexpMatcher() {
        RegExpMatcher regExpMatcher = new RegExpMatcher("^a.x$");

        Assert.assertEquals(false, regExpMatcher.match("11111"));
        Assert.assertEquals(false, regExpMatcher.match("a01"));
        Assert.assertEquals(false, regExpMatcher.match("b0x"));
        Assert.assertEquals(true, regExpMatcher.match("a0x"));
        Assert.assertEquals(true, regExpMatcher.match("a.x"));
        Assert.assertEquals(false, regExpMatcher.match("a0.x"));
    }

    public void antStringPatternSetTests() {
        AntStyleStringMatcher antStyleStringMatcher = new AntStyleStringMatcher(null);

    }

}
