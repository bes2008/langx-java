package com.jn.langx.text.grok;

import com.jn.langx.Converter;
import com.jn.langx.annotation.NonNull;
import com.jn.langx.text.StringTemplates;
import com.jn.langx.text.grok.pattern.PatternDefinitionRepository;
import com.jn.langx.text.grok.pattern.PatternDefinitionSource;
import com.jn.langx.text.placeholder.PlaceholderSubExpressionHandler;
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

    @NonNull
    private PropertySourcePlaceholderParser patternDefinitionSource;


    public void setPatternDefinitionRepository(PatternDefinitionRepository repository) {
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
        PlaceholderSubExpressionHandler subExpressionHandler = new PlaceholderSubExpressionHandler() {
            @Override
            public void accept(String variable, String expression, Holder<String> variableValueHolder) {
                if (Strings.isNotEmpty(expression) && !variableValueHolder.isEmpty()) {
                    expression = Strings.replace(expression, "][", "_");
                    //expression = Strings.replace(expression, "][", "");
                    expression = Strings.replace(expression, "[", "");
                    expression = Strings.replace(expression, "]", "");
                    expression = Strings.underlineToCamel(expression, true);
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
                    /*
                        if (variableValue != null) {
                            // 有的正则表达式中 有 \b，会导致匹配出错
                            variableValue = Strings.replace(variableValue, "\\b", "");
                        }
                    */
                    if (field != null) {
                        if (fieldToOriginPatternMap.containsKey(field)) {
                            String originPattern = fieldToOriginPatternMap.get(field);
                            // 因为一个正在表达式中，每一个 group的name必须 唯一，如果有存在重复的，可以用 \k<name>方式替代
                            // 所以这里要检查两个是否一致
                            if (Objs.equals(originPattern, variableValue)) {
                                variableValue = "\\k<" + field + ">";
                            } else {
                                throw new NamedGroupConflictedException(StringTemplates.formatWithPlaceholder("named group '{}' conflicted in grok regexp '{}':\n\t1: {}\n\t2: {}", field, patternTemplate, originPattern, variableValue));
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
        handler.setSubExpressionHandler(subExpressionHandler);
        String parsedPattern = handler.replacePlaceholders(patternTemplate, this.patternDefinitionSource);

        TemplatizedPattern pattern = new TemplatizedPattern();
        pattern.setExpression(patternTemplate);
        Option option = new Option();
        option.setMultiline(false);
        pattern.setRegexp(Regexps.createRegexp(parsedPattern, option));
        pattern.setFields(fieldToOriginPatternMap.keySet());
        pattern.setExpectedConverters(converterMap);

        return pattern;
    }

}
