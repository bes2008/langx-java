package com.jn.langx.util.timing.cron;

import com.jn.langx.text.StringTemplates;
import com.jn.langx.util.Calendars;
import com.jn.langx.util.Dates;
import com.jn.langx.util.Emptys;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Consumer2;
import com.jn.langx.util.function.Predicate;
import com.jn.langx.util.function.Predicate2;

import java.util.*;

public class CronExpressions {
    public static Date nextTime(String cron, CronExpressionType type, Date time) {
        return nextTime(new CronExpressionBuilder().type(type).expression(cron).build(), time);
    }

    /**
     * 根据cron来获取下一个时间
     *
     * @param cron
     * @param time
     * @return 获取 time参数的下一个时间
     */
    public static Date nextTime(CronExpression cron, Date time) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        int year = Calendars.getYears(calendar);
        int dayOfWeek = Calendars.getField(calendar, Calendar.DAY_OF_WEEK);
        int month = Calendars.getMonths(calendar, true);
        int dayOfMonth = Calendars.getField(calendar, Calendar.DAY_OF_MONTH);
        int hour = Calendars.getField(calendar, Calendars.DateField.HOUR);
        int minute = Calendars.getField(calendar, Calendars.MINUTE);
        int second = Calendars.getField(calendar, Calendars.SECOND);

        final List<Integer> fieldValues = Collects.asList(
                second,
                minute,
                hour,
                dayOfMonth,
                month,
                dayOfWeek,
                year
        );

        final Map<Integer, Integer> quartzFieldFlagToCalendarFieldMap = new HashMap<Integer, Integer>();
        quartzFieldFlagToCalendarFieldMap.put(CronExpression.SECOND, Calendars.SECOND.getField());
        quartzFieldFlagToCalendarFieldMap.put(CronExpression.MINUTE, Calendars.MINUTE.getField());
        quartzFieldFlagToCalendarFieldMap.put(CronExpression.HOUR, Calendars.HOUR.getField());
        quartzFieldFlagToCalendarFieldMap.put(CronExpression.DAY_OF_MONTH, Calendar.DAY_OF_MONTH);
        quartzFieldFlagToCalendarFieldMap.put(CronExpression.MONTH, Calendars.MONTH.getField());
        quartzFieldFlagToCalendarFieldMap.put(CronExpression.DAY_OF_WEEK, Calendar.DAY_OF_MONTH);
        quartzFieldFlagToCalendarFieldMap.put(CronExpression.YEAR, Calendars.YEAR.getField());

        final Map<Integer, List<Integer>> fieldScopeMap = new HashMap<Integer, List<Integer>>();
        fieldScopeMap.put(CronExpression.SECOND, Collects.asList(cron.getSet(CronExpression.SECOND)));
        fieldScopeMap.put(CronExpression.MINUTE, Collects.asList(cron.getSet(CronExpression.MINUTE)));
        fieldScopeMap.put(CronExpression.HOUR, Collects.asList(cron.getSet(CronExpression.HOUR)));
        fieldScopeMap.put(CronExpression.DAY_OF_MONTH, Collects.asList(cron.getSet(CronExpression.DAY_OF_MONTH)));
        fieldScopeMap.put(CronExpression.MONTH, Collects.asList(cron.getSet(CronExpression.MONTH)));
        fieldScopeMap.put(CronExpression.DAY_OF_WEEK, Collects.asList(cron.getSet(CronExpression.DAY_OF_WEEK)));
        fieldScopeMap.put(CronExpression.YEAR, Collects.asList(cron.getSet(CronExpression.YEAR)));

        // 把每一个字段的值都校正到下一个时间点上该字段的正确值
        List<Cursor> cursors = Collects.asList(
                correctingFieldCursor(second, fieldScopeMap.get(CronExpression.SECOND)),
                correctingFieldCursor(minute, fieldScopeMap.get(CronExpression.MINUTE)),
                correctingFieldCursor(hour, fieldScopeMap.get(CronExpression.HOUR)),
                correctingFieldCursor(dayOfMonth, fieldScopeMap.get(CronExpression.DAY_OF_MONTH)),
                correctingFieldCursor(month, fieldScopeMap.get(CronExpression.MONTH)),
                correctingFieldCursor(dayOfWeek, fieldScopeMap.get(CronExpression.DAY_OF_WEEK)),
                correctingFieldCursor(year, fieldScopeMap.get(CronExpression.YEAR))
        );

        // 找到第一个需要清零的索引后，进行低位字段清零操作
        List<Cursor> reverseCursors = Collects.reverse(cursors, true);
        final int firstLessThanExpectValueField = Collects.firstOccurrence(reverseCursors, new Predicate2<Integer, Cursor>() {
            @Override
            public boolean test(Integer field, Cursor cursor) {
                if (!cursor.isInvalid()) {
                    if (field == (6 - CronExpression.DAY_OF_WEEK)) {
                        if (cursor.value == 98) {
                            return false;
                        }
                    }
                    if (field == (6 - CronExpression.DAY_OF_MONTH)) {
                        if (cursor.value > 98) {
                            return false;
                        }
                    }
                    return Collects.reverse(fieldValues, true).get(field) < cursor.value;
                }
                return false;
            }
        });
        if (firstLessThanExpectValueField != -1) {
            // 执行清零
            Collects.forEach(reverseCursors, new Predicate2<Integer, Cursor>() {
                @Override
                public boolean test(Integer field, Cursor cursor) {
                    return field > firstLessThanExpectValueField;
                }
            }, new Consumer2<Integer, Cursor>() {
                @Override
                public void accept(Integer field, Cursor cursor) {
                    if (!cursor.isInvalid()) {
                        if (field == (6 - CronExpression.DAY_OF_WEEK)) {
                            if (cursor.value == 98) {
                                return;
                            }
                        }
                        if (field == (6 - CronExpression.DAY_OF_MONTH)) {
                            if (cursor.value > 98) {
                                return;
                            }
                        }
                        cursor.pos = 0;
                        cursor.value = fieldScopeMap.get(6 - field).get(cursor.pos);

                    }
                }
            });
        }


        Cursor secondCursor = cursors.get(CronExpression.SECOND);
        List<Integer> secondScope = fieldScopeMap.get(CronExpression.SECOND);
        // 如果游标正好在当前秒，或者 满格
        if (secondCursor.value == second || secondCursor.pos == secondScope.size() - 1) {
            int value;
            Cursor cursor;
            int field = CronExpression.SECOND;
            List<Integer> fieldScope;

            boolean isNextFieldNeedMoveCursor = true;
            while (isNextFieldNeedMoveCursor && field < cursors.size()) {
                value = fieldValues.get(field);
                cursor = cursors.get(field);
                fieldScope = fieldScopeMap.get(field);
                while (cursor.isInvalid() && (field + 1) < cursors.size()) {
                    value = fieldValues.get(field);
                    cursor = cursors.get(field);
                    fieldScope = fieldScopeMap.get(field);
                }
                if (field < cursors.size()) {
                    isNextFieldNeedMoveCursor = toNextPosition(value, cursor, fieldScope);
                    field++;
                }
            }
        }

        Map<String,Integer> variableMap = new HashMap<String, Integer>();
        variableMap.put("yyyy",cursors.get(CronExpression.YEAR).value);
        variableMap.put("MM",cursors.get(CronExpression.MONTH).value);
        variableMap.put("dd",cursors.get(CronExpression.DAY_OF_MONTH).value);
        variableMap.put("HH",cursors.get(CronExpression.HOUR).value);
        variableMap.put("mm",cursors.get(CronExpression.MINUTE).value);
        variableMap.put("ss",cursors.get(CronExpression.SECOND).value);

        String dateString = StringTemplates.formatWithMap("${yyyy}-${MM}-${dd} ${HH}:${mm}:${ss}", variableMap);

        return Dates.parse(dateString, Dates.yyyy_MM_dd_HH_mm_ss);
    }

    /**
     * 适应于 year 字段外的其他字段
     * Quartz 解析器，会在集合最后 加上 98或者99
     *
     * @param fieldScope
     * @return
     */
    private static List<Integer> filterField(TreeSet<Integer> fieldScope) {
        return Collects.asList(Collects.filter(fieldScope, new Predicate<Integer>() {
            @Override
            public boolean test(Integer value) {
                return value >= 0 && value < 98;
            }
        }));
    }

    /**
     * 校正字段，使得游标指向正确的位置。
     * <pre>
     *
     * 范围:    |=============================|==============:
     *         开始值                         结束值          满格标记
     *
     * 例如：scope = [2, 3, 7, 13, 15, 99]，其中99是满格标记
     *  如果currentValue = 0，那么游标要指向2
     *  如果currentValue = 5，那么游标要指向7
     *  如果currentValue = 7，那么游标要指向7
     *  如果currentValue = 8，那么游标要指向13
     *  如果currentValue = 59，那么游标要指向99
     * </pre>
     *
     * @param currentValue
     * @param scope
     * @return
     */
    private static Cursor correctingFieldCursor(final int currentValue, List<Integer> scope) {
        if (Emptys.isEmpty(scope)) {
            return new Cursor(-1, -1);
        }

        final Cursor cursor = new Cursor(currentValue, -1);

        Collects.forEach(scope, new Predicate2<Integer, Integer>() {
                    @Override
                    public boolean test(Integer index, Integer value) {
                        return value >= currentValue;
                    }
                },
                new Consumer2<Integer, Integer>() {
                    @Override
                    public void accept(Integer index, Integer value) {
                        cursor.value = value;
                        cursor.pos = index;
                    }
                },
                new Predicate2<Integer, Integer>() {
                    @Override
                    public boolean test(Integer key, Integer value) {
                        return cursor.pos != -1;
                    }
                });

        return cursor;

    }

    /**
     * 移动指定field的游标到下一个位置，并返回下一个 field 是否需要进 1
     *
     * @param currentCursor 指定字段的游标
     * @param scope         指定字段的游标
     * @return 返回的是 下一个 field 是否需要进 1
     */
    private static boolean toNextPosition(int currentValue, Cursor currentCursor, List<Integer> scope) {
        Preconditions.checkNotEmpty(scope);
        Preconditions.checkTrue(!currentCursor.isInvalid());

        if (currentCursor.value > currentValue) {
            return false;
        }
        // 接下来是处理 currentPosition.value <= currentValue 的情况
        int nextPosition = currentCursor.pos + 1;
        if (nextPosition >= scope.size() - 1) {
            // 当前field 从来
            currentCursor.pos = 0;
            currentCursor.value = scope.get(currentCursor.pos);
            // 该field满了，该让高位 进 1 了
            return true;
        } else {
            currentCursor.pos = nextPosition;
            currentCursor.value = scope.get(currentCursor.pos);
        }
        return false;
    }

    public Date nextTime(String cron, Date time) {
        return nextTime(cron, CronExpressionType.QUARTZ, time);
    }

    private static class Cursor {
        public int value;
        public int pos;
        public Cursor(){}
        public Cursor(int value, int pos){
            this.value =value;
            this.pos =pos;
        }

        public boolean isInvalid(){
            return this.pos ==-1 && value==-1;
        }

    }
}
