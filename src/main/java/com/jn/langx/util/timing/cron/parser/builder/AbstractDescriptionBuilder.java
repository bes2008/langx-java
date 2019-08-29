package com.jn.langx.util.timing.cron.parser.builder;


import com.jn.langx.util.Strings;
import com.jn.langx.util.timing.cron.parser.CronI18nMessages;
import com.jn.langx.util.timing.cron.parser.Options;

import java.text.MessageFormat;

/**
 * @author grhodes
 * @since 10 Dec 2012 14:00:49
 */
public abstract class AbstractDescriptionBuilder {

    protected final char[] SpecialCharsMinusStar = new char[]{'/', '-', ','};

    public String getSegmentDescription(String expression, String allDescription) {
        String description = "";
        if (Strings.isEmpty(expression)) {
            description = "";
        } else if ("*".equals(expression)) {
            description = allDescription;
        } else if (!Strings.containsAny(expression, SpecialCharsMinusStar)) {
            description = MessageFormat.format(getDescriptionFormat(expression), getSingleItemDescription(expression));
        } else if (expression.contains("/")) {
            String[] segments = expression.split("/");
            description = MessageFormat.format(getIntervalDescriptionFormat(segments[1]), getSingleItemDescription(segments[1]));
            // interval contains 'between' piece (e.g. 2-59/3)
            if (segments[0].contains("-")) {
                String betweenSegmentOfInterval = segments[0];
                String[] betweenSegments = betweenSegmentOfInterval.split("-");
                description += ", " + MessageFormat.format(getBetweenDescriptionFormat(betweenSegmentOfInterval, false), getSingleItemDescription(betweenSegments[0]), getSingleItemDescription(betweenSegments[1]));
            }
        } else if (expression.contains(",")) {
            String[] segments = expression.split(",");
            StringBuilder descriptionContent = new StringBuilder();
            for (int i = 0; i < segments.length; i++) {
                if ((i > 0) && (segments.length > 2)) {
                    if (i < (segments.length - 1)) {
                        descriptionContent.append(", ");
                    }
                }
                if ((i > 0) && (segments.length > 1) && ((i == (segments.length - 1)) || (segments.length == 2))) {
                    if (needSpaceBetweenWords()) {
                        descriptionContent.append(" ");
                    }
                    descriptionContent.append(CronI18nMessages.get("and"));
                    if (needSpaceBetweenWords()) {
                        descriptionContent.append(" ");
                    }
                }
                if (segments[i].contains("-")) {
                    String[] betweenSegments = segments[i].split("-");
                    descriptionContent.append(MessageFormat.format(getBetweenDescriptionFormat(expression, true), getSingleItemDescription(betweenSegments[0]), getSingleItemDescription(betweenSegments[1])));
                } else {
                    descriptionContent.append(getSingleItemDescription(segments[i]));
                }
            }
            description = MessageFormat.format(getDescriptionFormat(expression), descriptionContent.toString());
        } else if (expression.contains("-")) {
            String[] segments = expression.split("-");
            description = MessageFormat.format(getBetweenDescriptionFormat(expression, false), getSingleItemDescription(segments[0]), getSingleItemDescription(segments[1]));
        }
        return description;
    }

    protected abstract String getBetweenDescriptionFormat(String expression, boolean omitSeparator);

    protected abstract String getIntervalDescriptionFormat(String expression);

    protected abstract String getSingleItemDescription(String expression);

    protected abstract String getDescriptionFormat(String expression);

    protected abstract Boolean needSpaceBetweenWords();

    /**
     * @deprecated Use plural(String, String, String) instead
     */
    @Deprecated
    protected String plural(int num, String singular, String plural) {
        return plural(String.valueOf(num), singular, plural);
    }

    /**
     * @since https://github.com/RedHogs/cron-parser/issues/2
     */
    protected String plural(String expression, String singular, String plural) {
        if (Strings.isNumeric(expression) && (Integer.parseInt(expression) > 1)) {
            return plural;
        } else if (expression.contains(",")) {
            return plural;
        }
        return singular;
    }

    /**
     * @param options
     * @return
     * @since https://github.com/grahamar/cron-parser/issues/48
     */
    protected String getSpace(Options options) {
        return options.isNeedSpaceBetweenWords() ? " " : "";
    }
}
