package com.jn.langx.util.function.predicate;

import com.jn.langx.util.Strings;
import com.jn.langx.util.function.Predicate;

public class StringContainsPredicate implements Predicate<String> {
    private String expectedValue;
    private boolean ignoreCase= true;

    public StringContainsPredicate() {
    }
    public StringContainsPredicate(String expected){
        this(expected,true);
    }
    public StringContainsPredicate(String expected, boolean ignoreCase){
        setExpectedValue(expected);
        setIgnoreCase(ignoreCase);
    }

    public void setIgnoreCase(boolean ignoreCase) {
        this.ignoreCase = ignoreCase;
    }

    public void setExpectedValue(String expectedValue) {
        this.expectedValue = expectedValue;
    }


    @Override
    public boolean test(String actualValue) {
        return Strings.contains(actualValue, this.expectedValue, ignoreCase);
    }
}
