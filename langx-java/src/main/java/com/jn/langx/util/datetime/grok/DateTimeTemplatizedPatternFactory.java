package com.jn.langx.util.datetime.grok;

import com.jn.langx.text.grok.TemplatizedPattern;
import com.jn.langx.text.grok.TemplatizedPatternFactory;
import com.jn.langx.text.grok.pattern.PatternDefinition;

public class DateTimeTemplatizedPatternFactory implements TemplatizedPatternFactory {
    @Override
    public TemplatizedPattern get(PatternDefinition definition) {
        if (definition instanceof DateTimePatternDefinition) {
            DateTimePatternDefinition def = (DateTimePatternDefinition) definition;
            DateTimePattern templatizedPattern = new DateTimePattern(def.getFormat(), def.getExpr());
            return templatizedPattern;
        }
        return new TemplatizedPattern();
    }
}
