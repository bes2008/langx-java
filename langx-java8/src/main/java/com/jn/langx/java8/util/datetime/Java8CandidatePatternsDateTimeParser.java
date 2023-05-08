package com.jn.langx.java8.util.datetime;

import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.datetime.DateTimeParsedResult;
import com.jn.langx.util.datetime.DateTimeParser;
import com.jn.langx.util.function.Consumer;
import com.jn.langx.util.function.Predicate;
import com.jn.langx.util.struct.Holder;

import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;

public class Java8CandidatePatternsDateTimeParser implements DateTimeParser {
    private Set<String> patterns = Collects.newLinkedHashSet();
    private Set<TimeZone> timeZones = Collects.newLinkedHashSet(TimeZone.getDefault());
    private Set<Locale> locales = Collects.newLinkedHashSet(Locale.getDefault(), Locale.US);

    public Java8CandidatePatternsDateTimeParser(List<String> patterns) {
        this(patterns, null, null);
    }

    public Java8CandidatePatternsDateTimeParser(List<String> patterns, List<TimeZone> timeZones) {
        this(patterns, timeZones, null);
    }

    public Java8CandidatePatternsDateTimeParser(List<String> patterns, List<TimeZone> timeZones, List<Locale> locales) {
        if (patterns != null) {
            this.patterns.addAll(patterns);
        }
        if (timeZones != null) {
            Set<TimeZone> tzs = Collects.newLinkedHashSet(timeZones);
            tzs.addAll(this.timeZones);
            this.timeZones = tzs;
        }
        if (locales != null) {
            Set<Locale> l = Collects.newLinkedHashSet(locales);
            l.addAll(this.locales);
            this.locales = l;
        }
    }

    @Override
    public DateTimeParsedResult parse(final String datetimeString) {
        final Holder<DateTimeParsedResult> resultHolder = new Holder<DateTimeParsedResult>();

        final Predicate breakPredicate = new Predicate() {
            @Override
            public boolean test(Object value) {
                return !resultHolder.isNull();
            }
        };

        Collects.forEach(patterns, new Consumer<String>() {
            @Override
            public void accept(final String pattern) {

                // 先用 tz =null, local =null 去解析，失败时，再用指定的 tz, local 去解析;
                DateTimeParsedResult r = new Java8DateTimeParser(pattern, null, null).parse(datetimeString);
                if (r != null) {
                    resultHolder.set(r);
                } else {
                    // 这里的代码基本上是不会执行了
                    Collects.forEach(timeZones, new Consumer<TimeZone>() {
                        @Override
                        public void accept(final TimeZone timeZone) {
                            Collects.forEach(locales, new Consumer<Locale>() {
                                @Override
                                public void accept(Locale locale) {
                                    DateTimeParsedResult r = new Java8DateTimeParser(pattern, timeZone, locale).parse(datetimeString);
                                    if (r != null) {
                                        resultHolder.set(r);
                                    }
                                }
                            }, breakPredicate);
                        }
                    }, breakPredicate);
                }
            }
        }, breakPredicate);
        return resultHolder.get();
    }

    public Java8CandidatePatternsDateTimeParser addTimeZone(TimeZone timeZone) {
        if (timeZone != null) {
            this.timeZones.add(timeZone);
        }
        return this;
    }

    public Java8CandidatePatternsDateTimeParser addLocale(Locale locale) {
        if (locale != null) {
            this.locales.add(locale);
        }
        return this;
    }
}
