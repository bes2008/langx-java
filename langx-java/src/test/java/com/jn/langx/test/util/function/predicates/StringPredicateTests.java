package com.jn.langx.test.util.function.predicates;

import com.jn.langx.util.function.predicate.StringContainsAnyPredicate;
import org.junit.Assert;
import org.junit.Test;

public class StringPredicateTests {
    @Test
    public void testStringContainsAny(){
        StringContainsAnyPredicate predicate = new StringContainsAnyPredicate("Apache","ASL");
        Assert.assertTrue(predicate.test("Apachev2 or later or GPLv2"));
    }
}
