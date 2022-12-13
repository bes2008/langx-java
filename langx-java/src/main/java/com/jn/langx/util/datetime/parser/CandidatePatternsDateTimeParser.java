package com.jn.langx.util.datetime.parser;

import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.PrioritySet;
import com.jn.langx.util.datetime.DateFormatCacheKey;
import com.jn.langx.util.datetime.DateTimeParsedResult;
import com.jn.langx.util.datetime.DateTimeParser;
import com.jn.langx.util.function.Consumer;
import com.jn.langx.util.function.Predicate;
import com.jn.langx.util.struct.Holder;

import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;

public class CandidatePatternsDateTimeParser implements DateTimeParser {
    private Set<String> patterns = Collects.newLinkedHashSet();
    private Set<TimeZone> timeZones = Collects.newLinkedHashSet(TimeZone.getDefault());
    private Set<Locale> locales = Collects.newLinkedHashSet(Locale.getDefault(), Locale.US);

    private PrioritySet<DateFormatCacheKey> formatCacheKeys;

    public CandidatePatternsDateTimeParser(List<String> patterns) {
        this(patterns, null, null);
    }

    public CandidatePatternsDateTimeParser(List<String> patterns, List<TimeZone> timeZones) {
        this(patterns, timeZones, null);
    }

    public CandidatePatternsDateTimeParser(List<String> patterns, List<TimeZone> timeZones, List<Locale> locales) {
        if (patterns != null) {
            this.patterns.addAll(patterns);
        }

        final Set<TimeZone> tzs;
        if (timeZones != null) {
            tzs = Collects.newLinkedHashSet(timeZones);
            tzs.addAll(this.timeZones);
        } else {
            tzs = Collects.newLinkedHashSet(timeZones);
        }

        final Set<Locale> l;
        if (locales != null) {
            l = Collects.newLinkedHashSet(locales);
            l.addAll(this.locales);
        } else {
            l = Collects.newLinkedHashSet(locales);
        }
        this.locales = l;

        final PrioritySet<DateFormatCacheKey> formatCacheKeys = new PrioritySet<DateFormatCacheKey>(10000);
        Collects.forEach(patterns, new Consumer<String>() {
            @Override
            public void accept(final String pattern) {
                Collects.forEach(tzs, new Consumer<TimeZone>() {
                    @Override
                    public void accept(final TimeZone timeZone) {
                        Collects.forEach(l, new Consumer<Locale>() {
                            @Override
                            public void accept(Locale locale) {
                                formatCacheKeys.add(new DateFormatCacheKey(pattern, timeZone, locale));
                            }
                        });
                    }
                });
            }
        });
        this.formatCacheKeys = formatCacheKeys;
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

        Collects.forEach(formatCacheKeys, new Consumer<DateFormatCacheKey>() {
            @Override
            public void accept(DateFormatCacheKey dateFormatCacheKey) {
                DateTimeParsedResult r = new GracefulDateParser(dateFormatCacheKey.pattern, TimeZone.getTimeZone(dateFormatCacheKey.timeZoneId), dateFormatCacheKey.locale).parse(datetimeString);
                if (r != null) {
                    resultHolder.set(r);
                    formatCacheKeys.increment(dateFormatCacheKey);
                }
            }
        }, breakPredicate);
        return resultHolder.get();
    }

    public CandidatePatternsDateTimeParser addTimeZone(final TimeZone timeZone) {
        if (timeZone != null) {
            this.timeZones.add(timeZone);
            Collects.forEach(patterns, new Consumer<String>() {
                @Override
                public void accept(final String pattern) {
                    Collects.forEach(locales, new Consumer<Locale>() {
                        @Override
                        public void accept(Locale locale) {
                            formatCacheKeys.add(new DateFormatCacheKey(pattern, timeZone, locale));
                        }
                    });
                }
            });
        }
        return this;
    }

    public CandidatePatternsDateTimeParser addLocale(final Locale locale) {
        if (locale != null) {
            this.locales.add(locale);
            Collects.forEach(patterns, new Consumer<String>() {
                @Override
                public void accept(final String pattern) {
                    Collects.forEach(timeZones, new Consumer<TimeZone>() {
                        @Override
                        public void accept(final TimeZone timeZone) {
                            formatCacheKeys.add(new DateFormatCacheKey(pattern, timeZone, locale));
                        }
                    });
                }
            });
        }
        return this;
    }
}
