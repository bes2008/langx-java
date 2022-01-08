package com.jn.langx.util.function.predicate;

import com.jn.langx.util.Strings;
import com.jn.langx.util.function.Predicate;

public class StringContainsPredicate implements Predicate<String> {
    private String expectedValue;
    public StringContainsPredicate() {
    }

    public StringContainsPredicate(String expected){
        setExpectedValue(expected);
    }

    public void setExpectedValue(String expectedValue) {
        this.expectedValue = expectedValue;
    }


    @Override
    public boolean test(String actualValue) {
        return Strings.contains(actualValue, this.expectedValue);
    }
}
