package com.jn.langx.util.datetime.time;

import com.jn.langx.util.Preconditions;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Consumer;
import com.jn.langx.util.function.Predicate;
import com.jn.langx.util.struct.Holder;

import java.util.List;
import java.util.Locale;
import java.util.Set;

public class CandidatePatternsTimeParser implements TimeParser {
    private final Set<String> patterns = Collects.newLinkedHashSet("HH:mm:ss", "hh:mm:ss a");
    private final Set<Locale> locales = Collects.newLinkedHashSet(Locale.ENGLISH, Locale.getDefault());

    public CandidatePatternsTimeParser(List<String> patterns, List<Locale> locales) {
        if (patterns != null) {
            this.patterns.addAll(patterns);
        }
        if (locales != null) {
            this.locales.addAll(locales);
        }
    }

    @Override
    public TimeParsedResult parse(final String time) {
        Preconditions.checkNotEmpty(time);
        final Holder<TimeParsedResult> resultHolder = new Holder<TimeParsedResult>();

        final Predicate breakPredicate = new Predicate() {
            @Override
            public boolean test(Object value) {
                return !resultHolder.isNull();
            }
        };
        Collects.forEach(patterns, new Consumer<String>() {
            @Override
            public void accept(final String pattern) {
                Collects.forEach(locales, new Consumer<Locale>() {
                    @Override
                    public void accept(Locale locale) {
                        TimeParsedResult r = new SimpleTimeParser(pattern, locale).parse(time);
                        if (r != null) {
                            resultHolder.set(r);
                        }
                    }
                }, breakPredicate);
            }
        }, breakPredicate);
        return resultHolder.get();
    }
}
