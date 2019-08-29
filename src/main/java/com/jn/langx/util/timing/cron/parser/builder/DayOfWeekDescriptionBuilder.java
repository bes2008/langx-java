package com.jn.langx.util.timing.cron.parser.builder;


import com.jn.langx.util.Strings;
import com.jn.langx.util.WordUtils;
import com.jn.langx.util.timing.cron.parser.CronI18nMessages;
import com.jn.langx.util.timing.cron.parser.DateAndTimeUtils;
import com.jn.langx.util.timing.cron.parser.Options;
import com.jn.langx.util.jodatime.format.DateTimeFormat;
import com.jn.langx.util.jodatime.format.DateTimeFormatter;

import java.text.MessageFormat;
import java.util.Locale;

/**
 * @author grhodes
 * @since 10 Dec 2012 14:23:18
 */
public class DayOfWeekDescriptionBuilder extends AbstractDescriptionBuilder {

    private final Options options;

    /**
     * @deprecated Use DayOfWeekDescriptionBuilder(Options) instead.
     */
    @Deprecated
    public DayOfWeekDescriptionBuilder() {
        this.options = null;
    }

    public DayOfWeekDescriptionBuilder(Options options) {
        this.options = options;
    }

    @Override
    protected String getSingleItemDescription(String expression) {
        String exp = expression;
        if (expression.contains("#")) {
            exp = expression.substring(0, expression.indexOf("#"));
        } else if (expression.contains("L")) {
            exp = exp.replace("L", "");
        }
        if (Strings.isNumeric(exp)) {
            int dayOfWeekNum = Integer.parseInt(exp);
            boolean isZeroBasedDayOfWeek = (options == null || options.isZeroBasedDayOfWeek());
            boolean isInvalidDayOfWeekForSetting = (options != null && !options.isZeroBasedDayOfWeek() && dayOfWeekNum <= 1);
            if(isInvalidDayOfWeekForSetting || (isZeroBasedDayOfWeek && dayOfWeekNum == 0)) {
                return DateAndTimeUtils.getDayOfWeekName(7);
            } else if(options != null && !options.isZeroBasedDayOfWeek()) {
                dayOfWeekNum -= 1;
            }
            return DateAndTimeUtils.getDayOfWeekName(dayOfWeekNum);
        } else {
            DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("EEE").withLocale(Locale.ENGLISH);
            return dateTimeFormatter.parseDateTime(WordUtils.capitalizeFully(exp)).dayOfWeek().getAsText(CronI18nMessages.getCurrentLocale());
        }
    }

    @Override
    protected String getIntervalDescriptionFormat(String expression) {
        return MessageFormat.format(", "+ CronI18nMessages.get("interval_description_format"), expression);
    }

    @Override
    protected String getBetweenDescriptionFormat(String expression, boolean omitSeparator) {
        String format = CronI18nMessages.get("between_weekday_description_format");
        return omitSeparator ? format : ", "+format;
    }

    @Override
    protected String getDescriptionFormat(String expression) {
        String format = null;
        if (expression.contains("#")) {
            String dayOfWeekOfMonthNumber = expression.substring(expression.indexOf("#") + 1);
            String dayOfWeekOfMonthDescription = "";
            if ("1".equals(dayOfWeekOfMonthNumber)) {
                dayOfWeekOfMonthDescription = CronI18nMessages.get("first");
            } else if ("2".equals(dayOfWeekOfMonthNumber)) {
                dayOfWeekOfMonthDescription = CronI18nMessages.get("second");
            } else if ("3".equals(dayOfWeekOfMonthNumber)) {
                dayOfWeekOfMonthDescription = CronI18nMessages.get("third");
            } else if ("4".equals(dayOfWeekOfMonthNumber)) {
                dayOfWeekOfMonthDescription = CronI18nMessages.get("fourth");
            } else if ("5".equals(dayOfWeekOfMonthNumber)) {
                dayOfWeekOfMonthDescription = CronI18nMessages.get("fifth");
            }
            format = ", "+String.format(CronI18nMessages.get("on_the_day_of_the_month"), dayOfWeekOfMonthDescription);
        } else if (expression.contains("L")) {
            format = ", "+CronI18nMessages.get("on_the_last_of_the_month");
        } else {
            format = ", "+CronI18nMessages.get("only_on");
        }
        return format;
    }

    @Override
    protected Boolean needSpaceBetweenWords() {
        return options.isNeedSpaceBetweenWords();
    }

}
