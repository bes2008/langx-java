package com.jn.langx.util.timing.cron.parser.builder;

import com.jn.langx.util.timing.cron.parser.CronI18nMessages;
import com.jn.langx.util.timing.cron.parser.Options;
import com.jn.langx.util.jodatime.DateTime;

import java.text.MessageFormat;

/**
 * @author grhodes
 * @since 15 Sep 2014
 */
public class YearDescriptionBuilder extends AbstractDescriptionBuilder {
    private final Options options;

    public YearDescriptionBuilder(Options options) {
        this.options = options;
    }

    @Override
    protected String getSingleItemDescription(String expression) {
        return new DateTime().withYear(Integer.parseInt(expression)).toString("yyyy", CronI18nMessages.getCurrentLocale());
    }

    @Override
    protected String getIntervalDescriptionFormat(String expression) {
        return MessageFormat.format(", " + CronI18nMessages.get("every_x") + getSpace(options) +
                plural(expression, CronI18nMessages.get("year"), CronI18nMessages.get("years")), expression);
    }

    @Override
    protected String getBetweenDescriptionFormat(String expression, boolean omitSeparator) {
        String format = CronI18nMessages.get("between_description_format");
        return omitSeparator ? format : ", " + format;
    }

    @Override
    protected String getDescriptionFormat(String expression) {
        return ", " + CronI18nMessages.get("only_in_year");
    }

    @Override
    protected Boolean needSpaceBetweenWords() {
        return options.isNeedSpaceBetweenWords();
    }
}
