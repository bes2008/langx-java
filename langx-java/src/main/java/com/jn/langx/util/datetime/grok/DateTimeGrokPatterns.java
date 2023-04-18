package com.jn.langx.util.datetime.grok;

import com.jn.langx.text.grok.pattern.PatternDefinition;
import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.function.Consumer2;
import com.jn.langx.util.datetime.Month;
import com.jn.langx.util.datetime.WeekDay;

import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.Map;

class DateTimeGrokPatterns {

    public static final String MONTH_PATTERN;
    public static final PatternDefinition PD_MONTH;

    private static final Map<String, Integer> MONTH_STR_TO_INTEGER = new LinkedHashMap<String, Integer>();

    static {
        /**
         * 初始化 month 相关的正则
         */
        final StringBuilder monthRegexpBuilder = new StringBuilder(300);
        // 0-12
        monthRegexpBuilder.append("(?:(?:0?\\d)|(?:1[012]))");
        Pipeline.of(EnumSet.allOf(Month.class))
                .forEach(new Consumer2<Integer, Month>() {
                    @Override
                    public void accept(Integer key, Month month) {
                        MONTH_STR_TO_INTEGER.put("" + month.getCode(), month.getCode());
                        MONTH_STR_TO_INTEGER.put(month.getFullCode(), month.getCode());
                        MONTH_STR_TO_INTEGER.put(month.name(), month.getCode());
                        MONTH_STR_TO_INTEGER.put(month.name().toUpperCase(), month.getCode());
                        MONTH_STR_TO_INTEGER.put(Strings.upperCaseFirstLetter(month.name()), month.getCode());
                        MONTH_STR_TO_INTEGER.put(month.getFullname(), month.getCode());
                        MONTH_STR_TO_INTEGER.put(Strings.upperCaseFirstLetter(month.getFullname()), month.getCode());
                        MONTH_STR_TO_INTEGER.put(month.getChinese(), month.getCode());

                        monthRegexpBuilder.append("|").append("(?:" + month.name() + "|" + Strings.upperCaseFirstLetter(month.name()) + "|" + month.getFullname() + "|" + Strings.upperCaseFirstLetter(month.getFullname()) + "|" + month.getChinese() + ")");
                    }
                });

        MONTH_PATTERN = "(?" + monthRegexpBuilder.toString() + ")";
        PD_MONTH = new PatternDefinition("MONTH", MONTH_PATTERN);

    }

    // 周
    public static final String WEEK_PATTERN;
    private static final PatternDefinition PD_WEEK;
    private static final Map<String, Integer> WEEK_STR_TO_INTEGER = new LinkedHashMap<String, Integer>();

    static {
        /**
         * 初始化 month 相关的正则
         */
        final StringBuilder weekRegexpBuilder = new StringBuilder(300);
        Pipeline.of(EnumSet.allOf(WeekDay.class))
                .forEach(new Consumer2<Integer, WeekDay>() {
                    @Override
                    public void accept(Integer key, WeekDay week) {
                        WEEK_STR_TO_INTEGER.put(week.name(), week.getCode());
                        WEEK_STR_TO_INTEGER.put(week.name().toUpperCase(), week.getCode());
                        WEEK_STR_TO_INTEGER.put(Strings.upperCaseFirstLetter(week.name()), week.getCode());
                        WEEK_STR_TO_INTEGER.put(week.getFullname(), week.getCode());
                        WEEK_STR_TO_INTEGER.put(Strings.upperCaseFirstLetter(week.getFullname()), week.getCode());
                        WEEK_STR_TO_INTEGER.put(week.getChinese(), week.getCode());

                        weekRegexpBuilder.append("|").append("(?:" + week.name() + "|" + Strings.upperCaseFirstLetter(week.name()) + "|" + week.getFullname() + "|" + Strings.upperCaseFirstLetter(week.getFullname()) + "|" + week.getChinese() + ")");
                    }
                });

        WEEK_PATTERN = "(?:" + weekRegexpBuilder.toString() + ")";
        PD_WEEK = new PatternDefinition("WEEK", WEEK_PATTERN);
    }


    // 年：两位 或者四位整数
    private static final PatternDefinition PD_YEAR = new PatternDefinition("YEAR", "(?:(?:\\d{2}{1,2}))");
    // 一月中某一天： 0-9,12-31
    private static final PatternDefinition PD_DAY_OF_MONTH = new PatternDefinition("DAY_OF_MONTH", "(?:[0-3]?\\d)");
    // 小时
    private static final PatternDefinition PD_HOUR = new PatternDefinition("HOUR", "(?:[0-2]?\\d)");
    // 分钟
    private static final PatternDefinition PD_MINUTE = new PatternDefinition("MINUTE", "(?:[0-5]?\\d)");
    // 秒
    private static final PatternDefinition PD_SECOND = new PatternDefinition("SECOND", "(?:[0-5]?\\d)");

    // 毫秒
    private static final PatternDefinition PD_MILLS = new PatternDefinition("MILLS", "\\d{1,3}");
    // 纳秒
    private static final PatternDefinition PD_NANOS = new PatternDefinition("NANOS", "\\d{9}");
    // 毫秒 或 纳秒
    private static final PatternDefinition PD_MILLS_OR_NANOS = new PatternDefinition("MILLS_NANOS", "(?:\\d{9}|\\d{1,3})");
    // 时区
    private static final PatternDefinition PD_TIMEZONE = new PatternDefinition("TIMEZONE", "(?:[a-zA-Z_-]+(?:(?:/[a-zA-Z_-]+))|(?:[+-]\\d{2}:?\\d{2}))");

    // 上午下午
    private static final PatternDefinition PD_AMPM = new PatternDefinition("AMPM", "(?:(?:[aApP][mM]?)|(?:[上下]午))");


    private static final PatternDefinition PD_TIME = new PatternDefinition("YYYY_MM_DD", "%{YEAR:year}:%{MONTH:month}:%{DAY_OF_MONTH:dayOfMonth}");
    private static final PatternDefinition PD_DATE = new PatternDefinition("YYYY_MM_DD2", "%{YEAR:year}/%{MONTH:month}/%{DAY_OF_MONTH:dayOfMonth}");
    private static final PatternDefinition PD_WEEK_MONTH_DAY = new PatternDefinition("WEEK_MONTH_DAY", "%{WEEK:week} %{MONTH:month} %{DAY_OF_MONTH:dayOfMonth}");

    private static final DateTimeGrokPatternDefinition PD_DATE_TIME = new DateTimeGrokPatternDefinition("yyy","");

    private DateTimeGrokPatterns(){

    }
}
