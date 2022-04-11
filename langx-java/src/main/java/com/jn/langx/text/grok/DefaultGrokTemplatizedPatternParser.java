package com.jn.langx.text.grok;

import com.jn.langx.Converter;
import com.jn.langx.text.StringTemplates;
import com.jn.langx.text.placeholder.PlaceholderExpressionConsumer;
import com.jn.langx.text.placeholder.PropertyPlaceholderHandler;
import com.jn.langx.text.placeholder.PropertySourcePlaceholderParser;
import com.jn.langx.util.Objs;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.converter.IntegerConverter;
import com.jn.langx.util.regexp.NamedGroupConflictedException;
import com.jn.langx.util.regexp.Option;
import com.jn.langx.util.regexp.Regexps;
import com.jn.langx.util.struct.Holder;

import java.util.*;


/**
 * @since 4.5.0
 */
public class DefaultGrokTemplatizedPatternParser implements GrokTemplatizedPatternParser {

    private PropertySourcePlaceholderParser patternDefinitionSource;

    public void setPatternDefinitionRepository(MultipleLevelPatternDefinitionRepository repository) {
        PatternDefinitionSource source = new PatternDefinitionSource();
        source.setRepository(repository);
        repository.startup();
        this.patternDefinitionSource = new PropertySourcePlaceholderParser(source);
    }

    @Override
    public TemplatizedPattern parse(final String patternTemplate) {
        Preconditions.checkNotNull(patternTemplate, "template");

        final Map<String, String> fieldToOriginPatternMap = Collects.emptyHashMap(true);
        final Map<String, Converter> converterMap = new HashMap<String, Converter>();
        PlaceholderExpressionConsumer consumer = new PlaceholderExpressionConsumer() {
            @Override
            public void accept(String variable, String expression, Holder<String> variableValueHolder) {
                if (Strings.isNotEmpty(expression) && !variableValueHolder.isEmpty()) {
                    //expression = Strings.replace(expression, "][", "_");
                    expression = Strings.replace(expression, "][", "");
                    expression = Strings.replace(expression, "[", "");
                    expression = Strings.replace(expression, "]", "");
                    Converter converter = null;
                    String field = null;
                    String variableValue = variableValueHolder.get();
                    if (expression.contains(":")) {
                        String[] segments = Strings.split(expression, ":");
                        if (segments.length > 0) {
                            field = segments[0];
                            if (segments.length >= 2) {
                                String converterSegment = segments[1];
                                if ("int".equals(converterSegment)) {
                                    converter = new IntegerConverter();
                                }
                            }
                        }
                    } else {
                        field = expression;
                    }

                    if (field != null) {
                        if (fieldToOriginPatternMap.containsKey(field)) {
                            String originPattern = fieldToOriginPatternMap.get(field);
                            if(Objs.equals(originPattern, variable)) {
                                variableValue = "\\k<" + field + ">";
                            }else{
                                throw new NamedGroupConflictedException(StringTemplates.formatWithPlaceholder("named group '{}' conflicted in grok regexp: {}", field, patternTemplate));
                            }
                        } else {
                            fieldToOriginPatternMap.put(field, variableValue);
                            variableValue = "(?<" + field + ">" + variableValue + ")";
                        }
                        variableValueHolder.set(variableValue);
                        if (converter != null) {
                            converterMap.put(field, converter);
                        }
                    }
                }
            }
        };

        PropertyPlaceholderHandler handler = new PropertyPlaceholderHandler("%{", "}", ":", false);
        handler.setExpressionConsumer(consumer);
        String parsedPattern = handler.replacePlaceholders(patternTemplate, this.patternDefinitionSource);

        TemplatizedPattern pattern = new TemplatizedPattern();
        pattern.setExpression(patternTemplate);
        Option option = new Option();
        option.setMultiple(true);
        pattern.setRegexp(Regexps.createRegexp(parsedPattern, option));
        pattern.setFields(fieldToOriginPatternMap.keySet());
        pattern.setExpectedConverters(converterMap);

        return pattern;
    }

}
