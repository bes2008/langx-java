package com.jn.langx.text.grok;

import com.jn.langx.Converter;
import com.jn.langx.text.placeholder.PropertyPlaceholderHandler;
import com.jn.langx.text.placeholder.PropertySourcePlaceholderParser;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.function.Consumer2;
import com.jn.langx.util.regexp.Regexps;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;


/**
 * @since 4.5.0
 */
public class DefaultGrokTemplatizedPatternParser implements GrokTemplatizedPatternParser{

    private PropertySourcePlaceholderParser patternDefinitionSource;

    public void setPatternDefinitionRepository(PatternDefinitionRepository repository){
        PatternDefinitionSource source = new PatternDefinitionSource();
        source.setRepository(repository);
        this.patternDefinitionSource = new PropertySourcePlaceholderParser(source);
    }

    @Override
    public TemplatizedPattern parse(String patternTemplate) {
        Preconditions.checkNotNull(patternTemplate, "template");

        final Set<String> fields = new LinkedHashSet<String>();
        final Map<String, Converter> converterMap =new HashMap<String, Converter>();
        Consumer2<String,String> consumer2 = new Consumer2<String, String>() {
            @Override
            public void accept(String key, String value) {

            }
        };

        PropertyPlaceholderHandler handler = new PropertyPlaceholderHandler("%{","}", ":", true);
        String parsedPattern = handler.replacePlaceholders(patternTemplate, this.patternDefinitionSource, consumer2);

        TemplatizedPattern pattern = new TemplatizedPattern();
        pattern.setExpression(patternTemplate);
        pattern.setRegexp(Regexps.createRegexp(parsedPattern));
        pattern.setFields(fields);
        pattern.setExpectedConverters(converterMap);

        return pattern;
    }

}
