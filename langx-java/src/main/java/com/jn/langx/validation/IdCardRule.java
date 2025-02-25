package com.jn.langx.validation;

import com.jn.langx.util.regexp.RegexpPatterns;

public class IdCardRule extends RegexpRule{
    public IdCardRule(String errorMessage) {
        super(errorMessage, RegexpPatterns.PATTERN_IDENTITY);
    }
}
