package com.jn.langx.util.timing.cron.parser.builder;

import com.jn.langx.util.timing.cron.parser.CronI18nMessages;
import com.jn.langx.util.timing.cron.parser.Options;

import java.text.MessageFormat;

/**
 * @author grhodes
 * @since 10 Dec 2012 14:10:43
 */
public class SecondsDescriptionBuilder extends AbstractDescriptionBuilder {
    private final Options options;

    public SecondsDescriptionBuilder(Options options) {
        this.options = options;
    }

    @Override
    protected String getSingleItemDescription(String expression) {
        return expression;
    }

    @Override
    protected String getIntervalDescriptionFormat(String expression) {
        return MessageFormat.format(CronI18nMessages.get("every_x_seconds"), expression);
    }

    @Override
    protected String getBetweenDescriptionFormat(String expression, boolean omitSeparator) {
        return CronI18nMessages.get("seconds_through_past_the_minute");
    }

    @Override
    protected String getDescriptionFormat(String expression) {
        return CronI18nMessages.get("at_x_seconds_past_the_minute");
    }

    @Override
    protected Boolean needSpaceBetweenWords() {
        return options.isNeedSpaceBetweenWords();
    }

}
