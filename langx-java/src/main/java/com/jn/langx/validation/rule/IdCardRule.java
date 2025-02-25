package com.jn.langx.validation.rule;

import com.jn.langx.util.regexp.RegexpPatterns;

public class IdCardRule extends RegexpRule{
    public IdCardRule(String errorMessage) {
        super(errorMessage, RegexpPatterns.PATTERN_IDENTITY);
    }
}
