package com.jn.langx.asn1.spec;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.text.StringTemplates;
import com.jn.langx.util.Preconditions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

class InternalGeneralizedTimes {

    private static final ThreadLocal<SimpleDateFormat> GENERALIZED_TIME_FORMATTERS = new ThreadLocal<SimpleDateFormat>();

    @NonNull
    public static String encodeGeneralizedTime(@NonNull Date d) {
        SimpleDateFormat dateFormat = GENERALIZED_TIME_FORMATTERS.get();
        if (dateFormat == null) {
            dateFormat = new SimpleDateFormat("yyyyMMddHHmmss.SSS'Z'");
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            GENERALIZED_TIME_FORMATTERS.set(dateFormat);
        }

        return dateFormat.format(d);
    }

    @NonNull
    public static Date decodeGeneralizedTime(@NonNull String t) throws ParseException {
        Preconditions.checkNotNull(t);
        int tzPos;
        TimeZone tz;
        if (t.endsWith("Z")) {
            tz = TimeZone.getTimeZone("UTC");
            tzPos = t.length() - 1;
        } else {
            tzPos = t.lastIndexOf(45);
            if (tzPos < 0) {
                tzPos = t.lastIndexOf(43);
                if (tzPos < 0) {
                    String err = "Unable to parse time zone information from the provided timestamp {}.";
                    err = StringTemplates.formatWithPlaceholder(err, t);
                    throw new ParseException(err, 0);
                }
            }

            tz = TimeZone.getTimeZone("GMT" + t.substring(tzPos));
            if (tz.getRawOffset() == 0 && !t.endsWith("+0000") && !t.endsWith("-0000")) {
                String err = "Unable to parse time zone information from the provided timestamp {}.";
                err = StringTemplates.formatWithPlaceholder(err, t);
                throw new ParseException(err, tzPos);
            }
        }

        int periodPos = t.lastIndexOf(46, tzPos);
        String subSecFormatStr;
        String trimmedTimestamp;
        if (periodPos > 0) {
            int subSecondLength = tzPos - periodPos - 1;
            switch (subSecondLength) {
                case 0:
                    subSecFormatStr = "";
                    trimmedTimestamp = t.substring(0, periodPos);
                    break;
                case 1:
                    subSecFormatStr = ".SSS";
                    trimmedTimestamp = t.substring(0, periodPos + 2) + "00";
                    break;
                case 2:
                    subSecFormatStr = ".SSS";
                    trimmedTimestamp = t.substring(0, periodPos + 3) + '0';
                    break;
                default:
                    subSecFormatStr = ".SSS";
                    trimmedTimestamp = t.substring(0, periodPos + 4);
            }
        } else {
            subSecFormatStr = "";
            periodPos = tzPos;
            trimmedTimestamp = t.substring(0, tzPos);
        }

        String formatStr;
        switch (periodPos) {
            case 10:
                formatStr = "yyyyMMddHH" + subSecFormatStr;
                break;
            case 11:
            case 13:
            default:
                String err = "Unable to parse the provided timestamp {} because it had an invalid number of characters before the sub-second and/or time zone portion.";
                err = StringTemplates.formatWithPlaceholder(err, t);
                throw new ParseException(err, periodPos);
            case 12:
                formatStr = "yyyyMMddHHmm" + subSecFormatStr;
                break;
            case 14:
                formatStr = "yyyyMMddHHmmss" + subSecFormatStr;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat(formatStr);
        dateFormat.setTimeZone(tz);
        dateFormat.setLenient(false);
        return dateFormat.parse(trimmedTimestamp);
    }
}
