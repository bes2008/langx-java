package com.jn.langx.util.timing.cron.parser.builder;

import com.jn.langx.util.timing.cron.parser.CronI18nMessages;
import com.jn.langx.util.timing.cron.parser.Options;

/**
 * @author grhodes
 * @since 10 Dec 2012 14:24:08
 */
public class DayOfMonthDescriptionBuilder extends AbstractDescriptionBuilder {

    private final Options options;

    public DayOfMonthDescriptionBuilder(Options options) {
        this.options = options;
    }

    @Override
    protected String getSingleItemDescription(String expression) {
        return expression;
    }

    @Override
    protected String getIntervalDescriptionFormat(String expression) {

        return ", " + CronI18nMessages.get("every_x") + getSpace(options) + plural(expression, CronI18nMessages.get("day"), CronI18nMessages.get("days"));
    }

    @Override
    protected String getBetweenDescriptionFormat(String expression, boolean omitSeparator) {
        String format = CronI18nMessages.get("between_days_of_the_month");
        return omitSeparator ? format : ", " + format;
    }

    @Override
    protected String getDescriptionFormat(String expression) {
        return ", " + CronI18nMessages.get("on_day_of_the_month");
    }

    @Override
    protected Boolean needSpaceBetweenWords() {
        return options.isNeedSpaceBetweenWords();
    }

}
