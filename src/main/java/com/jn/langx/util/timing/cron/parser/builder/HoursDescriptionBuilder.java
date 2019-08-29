package com.jn.langx.util.timing.cron.parser.builder;

import com.jn.langx.util.timing.cron.parser.CronI18nMessages;
import com.jn.langx.util.timing.cron.parser.DateAndTimeUtils;
import com.jn.langx.util.timing.cron.parser.Options;

import java.text.MessageFormat;

/**
 * @author grhodes
 * @since 10 Dec 2012 14:18:21
 */
public class HoursDescriptionBuilder extends AbstractDescriptionBuilder {

    private final Options options;

    public HoursDescriptionBuilder(Options options) {
        this.options = options;
    }

    @Override
    protected String getSingleItemDescription(String expression) {
        return DateAndTimeUtils.formatTime(expression, "0", options);
    }

    @Override
    protected String getIntervalDescriptionFormat(String expression) {
        return MessageFormat.format(CronI18nMessages.get("every_x") + getSpace(options) +
                plural(expression, CronI18nMessages.get("hour"), CronI18nMessages.get("hours")), expression);
    }

    @Override
    protected String getBetweenDescriptionFormat(String expression, boolean omitSeparator) {
        return CronI18nMessages.get("between_x_and_y");
    }

    @Override
    protected String getDescriptionFormat(String expression) {
        return CronI18nMessages.get("at_x");
    }

    @Override
    protected Boolean needSpaceBetweenWords() {
        return options.isNeedSpaceBetweenWords();
    }

}
