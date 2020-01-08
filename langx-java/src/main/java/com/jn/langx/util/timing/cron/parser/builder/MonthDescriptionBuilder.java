package com.jn.langx.util.timing.cron.parser.builder;

import com.jn.langx.util.jodatime.DateTime;
import com.jn.langx.util.timing.cron.parser.CronI18nMessages;
import com.jn.langx.util.timing.cron.parser.Options;

import java.text.MessageFormat;

/**
 * @author grhodes
 * @since 10 Dec 2012 14:23:50
 */
public class MonthDescriptionBuilder extends AbstractDescriptionBuilder {
    private final Options options;

    public MonthDescriptionBuilder(Options options) {
        this.options = options;
    }

    @Override
    protected String getSingleItemDescription(String expression) {
        return new DateTime().withDayOfMonth(1).withMonthOfYear(Integer.parseInt(expression)).
                toString("MMMM", CronI18nMessages.getCurrentLocale());
    }

    @Override
    protected String getIntervalDescriptionFormat(String expression) {
        return MessageFormat.format(", " + CronI18nMessages.get("every_x") + getSpace(options) +
                plural(expression, CronI18nMessages.get("month"), CronI18nMessages.get("months")), expression);
    }

    @Override
    protected String getBetweenDescriptionFormat(String expression, boolean omitSeparator) {
        String format = CronI18nMessages.get("between_description_format");
        return omitSeparator ? format : ", " + format;
    }

    @Override
    protected String getDescriptionFormat(String expression) {
        return ", " + CronI18nMessages.get("only_in_month");
    }

    @Override
    protected Boolean needSpaceBetweenWords() {
        return options.isNeedSpaceBetweenWords();
    }

}
