package com.jn.langx.util.datetime.grok;

import com.jn.langx.text.grok.TemplatizedPattern;
import com.jn.langx.text.grok.TemplatizedPatternFactory;
import com.jn.langx.text.grok.pattern.PatternDefinition;

class DateTimeTemplatizedPatternFactory implements TemplatizedPatternFactory {
    @Override
    public TemplatizedPattern get(PatternDefinition definition) {
        if (definition instanceof DateTimeGrokPatternDefinition) {
            DateTimeGrokPatternDefinition def = (DateTimeGrokPatternDefinition) definition;
            DateTimeGrokPattern templatizedPattern = new DateTimeGrokPattern(def.getFormat(), def.getExpr());
            return templatizedPattern;
        }
        return new TemplatizedPattern();
    }
}
