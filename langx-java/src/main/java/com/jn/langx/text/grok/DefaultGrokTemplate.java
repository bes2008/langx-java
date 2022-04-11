package com.jn.langx.text.grok;

import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.function.Function;
import com.jn.langx.util.function.Functions;
import com.jn.langx.util.function.Supplier0;
import com.jn.langx.util.logging.Loggers;
import com.jn.langx.util.regexp.RegexpMatcher;
import org.slf4j.Logger;

import java.util.Map;

/**
 * @since 4.5.0
 */
public class DefaultGrokTemplate implements GrokTemplate {
    private static final Logger logger = Loggers.getLogger(DefaultGrokTemplate.class);
    private TemplatizedPattern pattern;

    public DefaultGrokTemplate(TemplatizedPattern pattern) {
        this.pattern = pattern;
    }

    @Override
    public Map<String, Object> extract(String text) {
        final RegexpMatcher matcher = pattern.getRegexp().matcher(text);
        boolean matched = matcher.matches();
        if (!matched) {
            if (logger.isWarnEnabled()) {
                logger.warn("not matched, pattern: {}, text: {}", pattern, text);
            }
        } else {
            Map<String, Object> map = Pipeline.of(pattern.getFields())
                    .collect(Collects.toMap(
                            new Supplier0<Map<String, Object>>() {
                                @Override
                                public Map<String, Object> get() {
                                    return Collects.emptyHashMap(true);
                                }
                            },
                            Functions.<String>noopFunction(),
                            new Function<String, Object>() {
                                @Override
                                public Object apply(String field) {
                                    try {
                                        String textValue = matcher.group(field);
                                        return pattern.convert(field, textValue);
                                    } catch (Throwable ex) {
                                        logger.error(ex.getMessage(), ex);
                                    }
                                    return null;
                                }
                            })
                    );
            return map;
        }
        return null;
    }


    @Override
    public String format(Map<String, Object> args) {
        return null;
    }
}
