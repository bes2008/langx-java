package com.jn.langx.validation.rule;

import com.jn.langx.util.Objs;
import com.jn.langx.util.regexp.RegexpPatterns;

public class PlateNumberRule extends RegexpRule{
    public PlateNumberRule(String errorMessage) {
        super(Objs.useValueIfEmpty(errorMessage, "不是合法的车牌号"), RegexpPatterns.PATTERN_PLATE_NUMBER);
    }
}
