package com.jn.langx.util.timing.scheduling;

import com.jn.langx.text.StringTemplates;
import com.jn.langx.util.BooleanEvaluator;
import com.jn.langx.util.Numbers;
import com.jn.langx.util.Strings;
import com.jn.langx.util.enums.Enums;
import com.jn.langx.util.regexp.Regexp;
import com.jn.langx.util.regexp.RegexpMatcher;
import com.jn.langx.util.regexp.Regexps;

import java.util.concurrent.TimeUnit;

/**
 * {initialDelay} {fixedRate} {period} {timeunit}
 *
 * @since 4.6.7
 */
public class PeriodicTriggerFactory implements TriggerFactory {
    private static final String NAME = "periodic";
    private static final Regexp REGEXP = Regexps.createRegexp("(?:(?<initialDelay>\\d+)?\\s)(?:(?<fixedRate>true|false)?\\s)(?:(?<period>\\d+)\\s)(?:(?<timeunit>nanoseconds|microseconds|milliseconds|seconds|minutes|hours|days))");

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Trigger get(String expression) {
        RegexpMatcher matcher = REGEXP.matcher(expression);
        if (matcher.matches()) {
            String initialDelayString = matcher.group("initialDelay");
            String fixedRateString = matcher.group("fixedRate");
            String periodString = matcher.group("period");
            String timeunitString = matcher.group("timeunit");

            long initialDelay = Strings.isBlank(initialDelayString) ? 0 : Numbers.createLong(Strings.trim(initialDelayString));
            boolean fixedRate = BooleanEvaluator.simpleStringEvaluator.evalTrue(fixedRateString);
            long period = Strings.isBlank(periodString) ? 0 : Numbers.createLong(Strings.trim(periodString));
            TimeUnit timeUnit = Enums.ofName(TimeUnit.class, Strings.upperCase(timeunitString));

            PeriodicTrigger periodicTrigger = new PeriodicTrigger(period, timeUnit);
            periodicTrigger.setInitialDelay(initialDelay);
            periodicTrigger.setFixedRate(fixedRate);
            return periodicTrigger;
        }
        throw new IllegalArgumentException(StringTemplates.formatWithPlaceholder("illegal cron trigger expression: {}", expression));
    }
}
