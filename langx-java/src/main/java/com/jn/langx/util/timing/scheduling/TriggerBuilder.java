package com.jn.langx.util.timing.scheduling;

import com.jn.langx.Builder;
import com.jn.langx.annotation.NotBlank;
import com.jn.langx.annotation.Nullable;
import com.jn.langx.text.StringTemplates;
import com.jn.langx.util.Strings;
import com.jn.langx.util.regexp.Regexp;
import com.jn.langx.util.regexp.RegexpMatcher;
import com.jn.langx.util.regexp.Regexps;


/**
 * @since 4.6.7
 */
public class TriggerBuilder implements Builder<Trigger> {
    private static Regexp EXPRESSION_REGEXP = Regexps.createRegexp("(?<type>\\w+)(?::(?<exp>.*))?");

    /**
     * 带有 trigger 类型的 expression
     */
    @NotBlank
    private String expression;

    @Nullable
    private TriggerFactoryRegistry registry;

    public TriggerBuilder expression(String expression) {
        if (expression != null) {
            this.expression = expression;
        }
        return this;
    }

    public TriggerBuilder registry(TriggerFactoryRegistry registry) {
        this.registry = registry;
        return this;
    }


    @Override
    public Trigger build() {
        if (Strings.isBlank(expression)) {
            throw new IllegalArgumentException(StringTemplates.formatWithPlaceholder("illegal trigger expression: {}", expression));
        }
        RegexpMatcher matcher = EXPRESSION_REGEXP.matcher(expression);
        String triggerType = null;
        String exp = null;
        if (matcher.matches()) {
            triggerType = matcher.group("type");
            exp = matcher.group("exp");
        } else {
            throw new IllegalArgumentException(StringTemplates.formatWithPlaceholder("illegal trigger expression: {}", expression));
        }
        if (Strings.isBlank(triggerType)) {
            throw new IllegalArgumentException(StringTemplates.formatWithPlaceholder("illegal trigger expression: {}, unrecognized trigger type: {}", expression, triggerType));
        }
        if (registry == null) {
            registry = TriggerFactoryRegistry.GLOBAL_TRIGGER_REGISTRY;
        }

        TriggerFactory factory = registry.get(triggerType);
        if (factory == null) {
            throw new IllegalArgumentException(StringTemplates.formatWithPlaceholder("illegal trigger expression: {}, unrecognized trigger type: {}", expression, triggerType));
        }
        return factory.get(exp);
    }
}
