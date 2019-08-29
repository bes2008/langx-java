package com.jn.langx.util.timing.cron.parser;

import com.jn.langx.util.Strings;
import com.jn.langx.util.jodatime.DateTime;

import java.text.ParseException;
import java.util.Locale;
import java.util.regex.Pattern;

public class ExpressionParser {
    static final Pattern yearRegex = Pattern.compile("(.*)\\d{4}$");

    private ExpressionParser() {
    }

    /**
     * @deprecated Use ExpressionParser.parse(String, Options) instead.
     */
    @Deprecated
    public static String[] parse(String expression) throws ParseException {
        return parse(expression, null);
    }

    public static String[] parse(String expression, Options options) throws ParseException {
        String[] parsed = new String[]{"", "", "", "", "", "", ""};
        if (Strings.isEmpty(expression)) {
            throw new IllegalArgumentException(CronI18nMessages.get("expression_empty_exception"));
        }

        String[] expressionParts = expression.split(" ");
        if (expressionParts.length < 5) {
            throw new ParseException(expression, 0);
        } else if (expressionParts.length == 5) {
            parsed[0] = ""; // 5 part CRON so shift array past seconds element
            System.arraycopy(expressionParts, 0, parsed, 1, 5);
        } else if (expressionParts.length == 6) {
            //If last element ends with 4 digits, a year element has been supplied and no seconds element
            if (yearRegex.matcher(expressionParts[5]).matches()) {
                System.arraycopy(expressionParts, 0, parsed, 1, 6);
            } else {
                System.arraycopy(expressionParts, 0, parsed, 0, 6);
            }
        } else if (expressionParts.length == 7) {
            parsed = expressionParts;
        } else {
            throw new ParseException(expression, 7);
        }

        normaliseExpression(parsed, options);

        return parsed;
    }

    /**
     * @param expressionParts
     */
    private static void normaliseExpression(String[] expressionParts, Options options) {
        // Convert ? to * only for day of month and day of week
        expressionParts[3] = expressionParts[3].replace('?', '*');
        expressionParts[5] = expressionParts[5].replace('?', '*');

        // Convert 0/, 1/ to */
        expressionParts[0] = expressionParts[0].startsWith("0/") ? expressionParts[0].replace("0/", "*/") : expressionParts[0]; // seconds
        expressionParts[1] = expressionParts[1].startsWith("0/") ? expressionParts[1].replace("0/", "*/") : expressionParts[1]; // minutes
        expressionParts[2] = expressionParts[2].startsWith("0/") ? expressionParts[2].replace("0/", "*/") : expressionParts[2]; // hours
        expressionParts[3] = expressionParts[3].startsWith("1/") ? expressionParts[3].replace("1/", "*/") : expressionParts[3]; // DOM
        expressionParts[4] = expressionParts[4].startsWith("1/") ? expressionParts[4].replace("1/", "*/") : expressionParts[4]; // Month
        expressionParts[5] = expressionParts[5].startsWith("1/") ? expressionParts[5].replace("1/", "*/") : expressionParts[5]; // DOW

        // convert */1 to *
        for (int i = 0; i < expressionParts.length; i++) {
            if ("*/1".equals(expressionParts[i])) {
                expressionParts[i] = "*";
            }
        }

        // convert SUN-SAT format to 0-6 format
        if (!Strings.isNumeric(expressionParts[5])) {
            for (int i = 0; i <= 6; i++) {
                expressionParts[5] = expressionParts[5].replace(DateAndTimeUtils.getDayOfWeekName(i + 1), String.valueOf(i));
            }
        }

        // convert JAN-DEC format to 1-12 format
        if (!Strings.isNumeric(expressionParts[4])) {
            for (int i = 1; i <= 12; i++) {
                DateTime currentMonth = new DateTime().withDayOfMonth(1).withMonthOfYear(i);
                String currentMonthDescription = currentMonth.toString("MMM", Locale.ENGLISH).toUpperCase();
                expressionParts[4] = expressionParts[4].replace(currentMonthDescription, String.valueOf(i));
            }
        }

        // convert 0 second to (empty)
        if ("0".equals(expressionParts[0])) {
            expressionParts[0] = "";
        }

        // convert 0 DOW to 7 so that 0 for Sunday in zeroBasedDayOfWeek is valid
        if ((options == null || options.isZeroBasedDayOfWeek()) && "0".equals(expressionParts[5])) {
            expressionParts[5] = "7";
        }
    }

}
