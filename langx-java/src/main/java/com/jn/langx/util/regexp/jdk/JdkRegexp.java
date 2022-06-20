package com.jn.langx.util.regexp.jdk;

import com.jn.langx.util.Emptys;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.comparator.OrderedComparator;
import com.jn.langx.util.function.Supplier;
import com.jn.langx.util.reflect.Reflects;
import com.jn.langx.util.regexp.Option;
import com.jn.langx.util.regexp.Regexp;
import com.jn.langx.util.regexp.RegexpMatcher;
import com.jn.langx.util.struct.Holder;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.regex.Pattern;

/**
 * @since 4.5.0
 */
public class JdkRegexp implements Regexp {
    private Pattern pattern;
    private Option option;
    private Holder<List<String>> namedGroups;

    public JdkRegexp(Pattern pattern) {
        this.pattern = pattern;
        this.option = Option.buildOption(pattern.flags());
    }

    public JdkRegexp(String pattern) {
        this.pattern = Pattern.compile(pattern, 0);
    }

    public JdkRegexp(String pattern, Option option) {
        this(pattern, option.toFlags());
    }

    public JdkRegexp(String pattern, int flags) {
        this.pattern = Pattern.compile(pattern, flags);
        this.option = Option.buildOption(flags);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Option getOption() {
        return option;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPattern() {
        return this.pattern.pattern();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RegexpMatcher matcher(CharSequence input) {
        return new JdkMatcher(this, this.pattern.matcher(input));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String[] split(CharSequence input) {
        return this.split(input, 0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String[] split(CharSequence input, int limit) {
        return this.pattern.split(input, limit);
    }

    @Override
    public String toString() {
        return getPattern();
    }

    private static final Method NAMED_GROUP_MAP_METHOD;

    static {
        NAMED_GROUP_MAP_METHOD = Reflects.getDeclaredMethod(Pattern.class, "namedGroups", Emptys.EMPTY_CLASSES);
    }

    @Override
    public List<String> getNamedGroups() {
        if (namedGroups == null) {
            List<String> groups = null;
            // key: group name, value: index
            final Map<String, Integer> map = Reflects.invoke(NAMED_GROUP_MAP_METHOD, this.pattern, Emptys.EMPTY_OBJECTS, true, true);
            if (map == null) {
                groups = Collects.unmodifiableArrayList();
            } else {
                TreeSet<String> set = new TreeSet<String>(new OrderedComparator<String>(new Supplier<String, Integer>() {
                    @Override
                    public Integer get(String groupName) {
                        return map.get(groupName);
                    }
                }));
                set.addAll(map.keySet());
                groups = Collects.unmodifiableArrayList(Collects.asList(set));
            }
            namedGroups = new Holder<List<String>>(groups);
        }
        return namedGroups.get();
    }
}
