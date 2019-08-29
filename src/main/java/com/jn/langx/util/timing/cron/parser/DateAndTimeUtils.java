package com.jn.langx.util.timing.cron.parser;

import com.jn.langx.util.Strings;
import com.jn.langx.util.jodatime.DateTime;
import com.jn.langx.util.jodatime.LocalTime;
import com.jn.langx.util.jodatime.format.DateTimeFormat;
import com.jn.langx.util.jodatime.format.DateTimeFormatter;

public final class DateAndTimeUtils {

    private DateAndTimeUtils() {
    }

    /**
     * @param hoursExpression
     * @param minutesExpression
     * @return
     */
    public static String formatTime(String hoursExpression, String minutesExpression, Options opts) {
        return formatTime(hoursExpression, minutesExpression, "", opts);
    }

    /**
     * @param hoursExpression
     * @param minutesExpression
     * @param secondsExpression
     * @return
     */
    public static String formatTime(String hoursExpression, String minutesExpression, String secondsExpression, Options opts) {
        int hour = Integer.parseInt(hoursExpression);
        int minutes = Integer.parseInt(minutesExpression);

        LocalTime localTime;
        DateTimeFormatter timeFormat;

        if (opts.isTwentyFourHourTime()) {
            if (!Strings.isEmpty(secondsExpression)) {
                final int seconds = Integer.parseInt(secondsExpression);
                localTime = new LocalTime(hour, minutes, seconds);
                timeFormat = DateTimeFormat.mediumTime();
            } else {
                localTime = new LocalTime(hour, minutes);
                timeFormat = DateTimeFormat.shortTime();
            }
        } else {
            if (!Strings.isEmpty(secondsExpression)) {
                final int seconds = Integer.parseInt(secondsExpression);
                localTime = new LocalTime(hour, minutes, seconds);
                timeFormat = DateTimeFormat.forPattern("h:mm:ss a");
            } else {
                localTime = new LocalTime(hour, minutes);
                timeFormat = DateTimeFormat.forPattern("h:mm a");
            }
        }
        return localTime.toString(timeFormat.withLocale(CronI18nMessages.getCurrentLocale()));
    }

    public static String getDayOfWeekName(int dayOfWeek) {
        return new DateTime().withDayOfWeek(dayOfWeek).dayOfWeek().getAsText(CronI18nMessages.getCurrentLocale());
    }

    /**
     * @param minutesExpression
     * @return
     * @since https://github.com/RedHogs/cron-parser/issues/2
     */
    public static String formatMinutes(String minutesExpression) {
        if (minutesExpression.contains(",")) {
            StringBuilder formattedExpression = new StringBuilder();
            for (String minute : Strings.split(minutesExpression, ",")) {
                formattedExpression.append(Strings.leftPad(minute, 2, '0'));
                formattedExpression.append(",");
            }
            return formattedExpression.toString();
        }
        return Strings.leftPad(minutesExpression, 2, '0');
    }

}
