package com.jn.langx.text.grok;

import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.function.Function;
import com.jn.langx.util.function.Functions;
import com.jn.langx.util.function.Supplier0;
import com.jn.langx.util.regexp.RegexpMatcher;

import java.util.Map;

public class DefaultGrokTemplate implements GrokTemplate {
    private TemplatizedPattern pattern;

    public DefaultGrokTemplate(TemplatizedPattern pattern) {
        this.pattern = pattern;
    }

    @Override
    public Map<String, Object> extract(String text) {
        final RegexpMatcher matcher = pattern.getRegexp().matcher(text);
        matcher.matches();
        Map<String, Object> map = Pipeline.of(pattern.getFields())
                .collect(Collects.toMap(new Supplier0<Map<String, Object>>() {
                                            @Override
                                            public Map<String, Object> get() {
                                                return Collects.emptyHashMap(true);
                                            }
                                        },
                                Functions.<String>noopFunction(),
                                new Function<String, Object>() {
                                    @Override
                                    public Object apply(String field) {
                                        String textValue = matcher.group(field);
                                        return pattern.convert(field,textValue);
                                    }
                                })
                );
        return map;
    }


    @Override
    public String format(Map<String, Object> args) {
        return null;
    }
}
