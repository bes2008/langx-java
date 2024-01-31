package com.jn.langx.util.regexp.jdk;

import com.jn.langx.util.Emptys;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.Lists;
import com.jn.langx.util.comparator.OrderedComparator;
import com.jn.langx.util.function.Supplier;
import com.jn.langx.util.logging.Loggers;
import com.jn.langx.util.reflect.Reflects;
import com.jn.langx.util.regexp.Groups;
import com.jn.langx.util.regexp.Option;
import com.jn.langx.util.regexp.Regexp;
import com.jn.langx.util.regexp.RegexpMatcher;
import com.jn.langx.util.struct.Holder;
import org.slf4j.Logger;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.regex.Pattern;

/**
 * @since 4.5.0
 */
public class JdkRegexp implements Regexp {
    private static final Logger logger = Loggers.getLogger(JdkRegexp.class);
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
            List<String> groups;
            // key: group name, value: index
            Map<String, Integer> _map=null;
            try {
                // 优先使用 JDK 中 方式获取组
                if (NAMED_GROUP_MAP_METHOD!=null) {
                    _map = Reflects.invoke(NAMED_GROUP_MAP_METHOD, this.pattern, Emptys.EMPTY_OBJECTS, true, true);
                }
            }catch (Throwable e){
                // ignore it
            }
            final Map<String, Integer> groupInfoByReflect =_map;
            if (groupInfoByReflect != null) {
                TreeSet<String> set = new TreeSet<String>(new OrderedComparator<String>(new Supplier<String, Integer>() {
                    @Override
                    public Integer get(String groupName) {
                        return groupInfoByReflect.get(groupName);
                    }
                }));
                set.addAll(groupInfoByReflect.keySet());
                groups = Lists.immutableList(set);
            } else {
                // 通过自己的扫描来获取组信息
                Map<String, List<Groups.GroupCoordinate>> scannedGroupInfo=Groups.extractGroupInfo(this.pattern.pattern());
                groups = Lists.newArrayList(scannedGroupInfo.keySet());
            }
            namedGroups = new Holder<List<String>>(groups);
        }
        return namedGroups.get();
    }
}
