package com.jn.langx.validation.rule;

import com.jn.langx.util.regexp.RegexpPatterns;

public class ChineseIdCardRule extends RegexpRule{
    public ChineseIdCardRule(String errorMessage) {
        super(errorMessage, RegexpPatterns.PATTERN_IDENTITY);
    }
}
