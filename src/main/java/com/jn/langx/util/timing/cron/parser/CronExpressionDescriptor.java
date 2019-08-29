package com.jn.langx.util.timing.cron.parser;

import com.jn.langx.util.Strings;
import com.jn.langx.util.timing.cron.parser.builder.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author grhodes
 * @since 10 Dec 2012 11:36:38
 */
public class CronExpressionDescriptor {

    private static final Logger LOG = LoggerFactory.getLogger(CronExpressionDescriptor.class);
    private static final char[] specialCharacters = new char[]{'/', '-', ',', '*'};
    private static Pattern pattern = Pattern.compile("(\\dW)|(W\\d)");

    private CronExpressionDescriptor() {
    }

    public static String getDescription(String expression) throws ParseException {
        return getDescription(DescriptionTypeEnum.FULL, expression, new Options(), CronI18nMessages.DEFAULT_LOCALE);
    }

    public static String getDescription(String expression, Options options) throws ParseException {
        return getDescription(DescriptionTypeEnum.FULL, expression, options, CronI18nMessages.DEFAULT_LOCALE);
    }

    public static String getDescription(String expression, Locale locale) throws ParseException {
        return getDescription(DescriptionTypeEnum.FULL, expression, new Options(), locale);
    }

    public static String getDescription(String expression, Options options, Locale locale) throws ParseException {
        return getDescription(DescriptionTypeEnum.FULL, expression, options, locale);
    }

    public static String getDescription(DescriptionTypeEnum type, String expression) throws ParseException {
        return getDescription(type, expression, new Options(), CronI18nMessages.DEFAULT_LOCALE);
    }

    public static String getDescription(DescriptionTypeEnum type, String expression, Locale locale) throws ParseException {
        return getDescription(type, expression, new Options(), locale);
    }

    public static String getDescription(DescriptionTypeEnum type, String expression, Options options) throws ParseException {
        return getDescription(type, expression, options, CronI18nMessages.DEFAULT_LOCALE);
    }

    public static String getDescription(DescriptionTypeEnum type, String expression, Options options, Locale locale) throws ParseException {
        CronI18nMessages.setCurrentLocale(locale);
        String[] expressionParts;
        String description = "";
        try {
            expressionParts = ExpressionParser.parse(expression, options);
            switch (type) {
                case FULL:
                    description = getFullDescription(expressionParts, options);
                    break;
                case TIMEOFDAY:
                    description = getTimeOfDayDescription(expressionParts, options);
                    break;
                case HOURS:
                    description = getHoursDescription(expressionParts, options);
                    break;
                case MINUTES:
                    description = getMinutesDescription(expressionParts, options);
                    break;
                case SECONDS:
                    description = getSecondsDescription(expressionParts, options);
                    break;
                case DAYOFMONTH:
                    description = getDayOfMonthDescription(expressionParts, options);
                    break;
                case MONTH:
                    description = getMonthDescription(expressionParts, options);
                    break;
                case DAYOFWEEK:
                    description = getDayOfWeekDescription(expressionParts, options);
                    break;
                case YEAR:
                    description = getYearDescription(expressionParts, options);
                    break;
                default:
                    description = getSecondsDescription(expressionParts, options);
                    break;
            }
        } catch (ParseException e) {
            if (!options.isThrowExceptionOnParseError()) {
                description = e.getMessage();
                LOG.debug("Exception parsing expression.", e);
            } else {
                LOG.error("Exception parsing expression.", e);
                throw e;
            }
        }
        return description;
    }

    private static String getYearDescription(String[] expressionParts, Options options) {
        return new YearDescriptionBuilder(options).getSegmentDescription(expressionParts[6], ", " + CronI18nMessages.get("every_year"));
    }

    private static String getDayOfWeekDescription(String[] expressionParts, Options options) {
        return new DayOfWeekDescriptionBuilder(options).getSegmentDescription(expressionParts[5], ", " + CronI18nMessages.get("every_day"));
    }

    private static String getMonthDescription(String[] expressionParts, Options options) {
        return new MonthDescriptionBuilder(options).getSegmentDescription(expressionParts[4], "");
    }

    private static String getDayOfMonthDescription(String[] expressionParts, Options options) {
        String description;
        String exp = expressionParts[3].replace("?", "*");
        if ("L".equals(exp)) {
            description = ", " + CronI18nMessages.get("on_the_last_day_of_the_month");
        } else if ("WL".equals(exp) || "LW".equals(exp)) {
            description = ", " + CronI18nMessages.get("on_the_last_weekday_of_the_month");
        } else {

            Matcher matcher = pattern.matcher(exp);
            if (matcher.matches()) {
                int dayNumber = Integer.parseInt(matcher.group().replace("W", ""));
                String dayString = dayNumber == 1 ? CronI18nMessages.get("first_weekday") : MessageFormat.format(CronI18nMessages.get("weekday_nearest_day"), dayNumber);
                description = MessageFormat.format(", " + CronI18nMessages.get("on_the_of_the_month"), dayString);
            } else {
                description = new DayOfMonthDescriptionBuilder(options).getSegmentDescription(exp, ", " + CronI18nMessages.get("every_day"));
            }
        }
        return description;
    }

    private static String getSecondsDescription(String[] expressionParts, Options opts) {
        return new SecondsDescriptionBuilder(opts).getSegmentDescription(expressionParts[0], CronI18nMessages.get("every_second"));
    }

    private static String getMinutesDescription(String[] expressionParts, Options opts) {
        return new MinutesDescriptionBuilder(opts).getSegmentDescription(expressionParts[1], CronI18nMessages.get("every_minute"));
    }

    private static String getHoursDescription(String[] expressionParts, Options opts) {
        return new HoursDescriptionBuilder(opts).getSegmentDescription(expressionParts[2], CronI18nMessages.get("every_hour"));
    }

    private static String getTimeOfDayDescription(String[] expressionParts, Options opts) {
        String secondsExpression = expressionParts[0];
        String minutesExpression = expressionParts[1];
        String hoursExpression = expressionParts[2];
        StringBuilder description = new StringBuilder();
        // Handle special cases first
        if (!Strings.containsAny(minutesExpression, specialCharacters) && !Strings.containsAny(hoursExpression, specialCharacters) && !Strings.containsAny(secondsExpression, specialCharacters)) {
            description.append(CronI18nMessages.get("at"));
            if (opts.isNeedSpaceBetweenWords()) {
                description.append(" ");
            }
            description.append(DateAndTimeUtils.formatTime(hoursExpression, minutesExpression, secondsExpression, opts)); // Specific time of day (e.g. 10 14)
        } else if (minutesExpression.contains("-") && !minutesExpression.contains("/") && !Strings.containsAny(hoursExpression, specialCharacters)) {
            // Minute range in single hour (e.g. 0-10 11)
            String[] minuteParts = minutesExpression.split("-");
            description.append(MessageFormat.format(CronI18nMessages.get("every_minute_between"), DateAndTimeUtils.formatTime(hoursExpression, minuteParts[0], opts),
                    DateAndTimeUtils.formatTime(hoursExpression, minuteParts[1], opts)));
        } else if (hoursExpression.contains(",") && !Strings.containsAny(minutesExpression, specialCharacters)) {
            // Hours list with single minute (e.g. 30 6,14,16)
            String[] hourParts = hoursExpression.split(",");
            description.append(CronI18nMessages.get("at"));
            for (int i = 0; i < hourParts.length; i++) {
                if (opts.isNeedSpaceBetweenWords()) {
                    description.append(" ");
                }
                description.append(DateAndTimeUtils.formatTime(hourParts[i], minutesExpression, opts));
                if (i < hourParts.length - 2) {
                    description.append(",");
                }
                if (i == hourParts.length - 2) {
                    if (opts.isNeedSpaceBetweenWords()) {
                        description.append(" ");
                    }
                    description.append(CronI18nMessages.get("and"));
                }
            }
        } else {
            String secondsDescription = getSecondsDescription(expressionParts, opts);
            String minutesDescription = getMinutesDescription(expressionParts, opts);
            String hoursDescription = getHoursDescription(expressionParts, opts);
            description.append(secondsDescription);
            if (description.length() > 0 && Strings.isNotEmpty(minutesDescription)) {
                description.append(", ");
            }
            description.append(minutesDescription);
            if (description.length() > 0 && Strings.isNotEmpty(hoursDescription)) {
                description.append(", ");
            }
            description.append(hoursDescription);
        }
        return description.toString();
    }

    private static String getFullDescription(String[] expressionParts, Options options) {
        String description = "";
        String timeSegment = getTimeOfDayDescription(expressionParts, options);
        String dayOfMonthDesc = getDayOfMonthDescription(expressionParts, options);
        String monthDesc = getMonthDescription(expressionParts, options);
        String dayOfWeekDesc = getDayOfWeekDescription(expressionParts, options);
        String yearDesc = getYearDescription(expressionParts, options);
        description = MessageFormat.format("{0}{1}{2}{3}", timeSegment, ("*".equals(expressionParts[3]) ? dayOfWeekDesc : dayOfMonthDesc), monthDesc, yearDesc);
        description = transformVerbosity(description, options);
        description = transformCase(description, options);
        return description;
    }

    private static String transformCase(String description, Options options) {
        String descTemp = description;
        switch (options.getCasingType()) {
            case Sentence:
                descTemp = Strings.upperCase("" + descTemp.charAt(0)) + descTemp.substring(1);
                break;
            case Title:
                descTemp = Strings.capitalize(descTemp);
                break;
            default:
                descTemp = descTemp.toLowerCase();
                break;
        }
        return descTemp;
    }

    private static String transformVerbosity(String description, Options options) {
        String descTemp = description;
        if (!options.isVerbose()) {
            descTemp = descTemp.replace(CronI18nMessages.get("every_1_minute"), CronI18nMessages.get("every_minute"));
            descTemp = descTemp.replace(CronI18nMessages.get("every_1_hour"), CronI18nMessages.get("every_hour"));
            descTemp = descTemp.replace(CronI18nMessages.get("every_1_day"), CronI18nMessages.get("every_day"));
            descTemp = descTemp.replace(", " + CronI18nMessages.get("every_minute"), "");
            descTemp = descTemp.replace(", " + CronI18nMessages.get("every_hour"), "");
            descTemp = descTemp.replace(", " + CronI18nMessages.get("every_day"), "");
            descTemp = descTemp.replace(", " + CronI18nMessages.get("every_year"), "");
        }
        return descTemp;
    }

}
