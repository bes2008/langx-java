package com.jn.langx.util.matchexp;

import com.jn.langx.util.Objs;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Functions;
import com.jn.langx.util.function.Predicate;
import com.jn.langx.util.function.Predicate2;
import com.jn.langx.util.logging.Loggers;
import com.jn.langx.util.reflect.Reflects;
import com.jn.langx.util.regexp.Regexp;
import com.jn.langx.util.regexp.Regexps;

import java.util.ArrayList;
import java.util.List;

public class MatchExp<V> {
    private List<Predicate<V>> predicates = new ArrayList<Predicate<V>>();
    private List<Runnable> actions = new ArrayList<Runnable>();


    public MatchExp<V> addValueEqualsPattern(final V expectedValue, Runnable action) {
        return addPattern(new Predicate<V>() {
            @Override
            public boolean test(V value) {
                return Objs.equals(value, expectedValue);
            }
        }, action);
    }

    public MatchExp<V> addTypePattern(final Class expectedClass, Runnable action) {
        return addPattern(new Predicate<V>() {
            @Override
            public boolean test(V value) {
                return value.getClass() == expectedClass;
            }
        }, action);
    }

    public MatchExp<V> addIsInstancePattern(final Class expectedClass, Runnable action) {
        return addPattern(new Predicate<V>() {
            @Override
            public boolean test(V value) {
                return Reflects.isInstance(value, expectedClass);
            }
        }, action);
    }

    private static final Class[] stringClasses = new Class[]{
            String.class,
            StringBuilder.class,
            StringBuffer.class
    };

    public MatchExp<V> addRegexpPattern(final String regexp, Runnable action) {
        return addRegexpPattern(Regexps.createRegexp(regexp), action);
    }

    public MatchExp<V> addRegexpPattern(final Regexp regexp, Runnable action) {
        return addPattern(new Predicate<V>() {
            @Override
            public boolean test(V value) {
                if (value == null) {
                    return false;
                }
                Class type = value.getClass();

                String strValue = null;
                if (type == char[].class) {
                    char[] chs = (char[]) value;
                    strValue = new String(chs);
                } else if (Collects.contains(stringClasses, type)) {
                    strValue = value.toString();
                }
                if (strValue == null) {
                    return false;
                } else {
                    return Regexps.match(regexp, strValue);
                }
            }
        }, action);
    }


    public MatchExp<V> addTruePattern(Runnable action) {
        return addPattern(Functions.<V>truePredicate(), action);
    }

    public MatchExp<V> addPattern(Predicate<V> predicate, Runnable action) {
        Preconditions.checkNotNull(predicate, "predicate is null");
        Preconditions.checkNotNull(action, "action is null");
        this.predicates.add(predicate);
        this.actions.add(action);
        return this;
    }


    public void match(final V value) {
        int idx = Collects.firstOccurrence(predicates, new Predicate2<Integer, Predicate<V>>() {
            @Override
            public boolean test(Integer index, Predicate<V> predicate) {
                return predicate.test(value);
            }
        });

        if (idx >= 0) {
            actions.get(idx).run();
        } else {
            Loggers.getLogger(MatchExp.class).warn("has not any pattern matched, please check your patterns");
        }
    }
}
