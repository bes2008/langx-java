package com.jn.langx.util.timing.cron.parser.builder;

import com.jn.langx.util.timing.cron.parser.CronI18nMessages;
import com.jn.langx.util.timing.cron.parser.DateAndTimeUtils;
import com.jn.langx.util.timing.cron.parser.Options;

import java.text.MessageFormat;

/**
 * @author grhodes
 * @since 10 Dec 2012 14:11:11
 */
public class MinutesDescriptionBuilder extends AbstractDescriptionBuilder {
    private final Options options;

    public MinutesDescriptionBuilder(Options options) {
        this.options = options;
    }
    @Override
    protected String getSingleItemDescription(String expression) {
        return DateAndTimeUtils.formatMinutes(expression);
    }

    @Override
    protected String getIntervalDescriptionFormat(String expression) {
        return MessageFormat.format(CronI18nMessages.get("every_x") + getSpace(options) + minPlural(expression), expression);
    }

    @Override
    protected String getBetweenDescriptionFormat(String expression, boolean omitSeparator) {
        return CronI18nMessages.get("minutes_through_past_the_hour");
    }

    @Override
    protected String getDescriptionFormat(String expression) {
        return "0".equals(expression) ? "" : CronI18nMessages.get("at_x") + getSpace(options) + minPlural(expression) +
                getSpace(options) + CronI18nMessages.get("past_the_hour");
    }

    @Override
    protected Boolean needSpaceBetweenWords() {
        return options.isNeedSpaceBetweenWords();
    }

    private String minPlural(String expression) {
        return plural(expression, CronI18nMessages.get("minute"), CronI18nMessages.get("minutes"));
    }

}
