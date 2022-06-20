package com.jn.langx.util.regexp.jdk;

import com.jn.langx.util.Objs;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.reflect.Reflects;
import com.jn.langx.util.regexp.RegexpMatcher;

import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 该类只能在 JDK7 以及更高版本使用
 *
 * @since 4.5.0
 */
class JdkMatcher implements RegexpMatcher {
    private Matcher matcher;

    JdkMatcher(Matcher matcher) {
        this.matcher = matcher;
    }

    @Override
    public boolean matches() {
        return matcher.matches();
    }

    @Override
    public int start() {
        return matcher.start();
    }

    @Override
    public int start(int group) {
        return matcher.start(group);
    }

    @Override
    public int end() {
        return matcher.end();
    }

    @Override
    public int end(int group) {
        return matcher.end(group);
    }

    @Override
    public String group() {
        return matcher.group();
    }

    @Override
    public String group(int group) {
        return matcher.group(group);
    }

    @Override
    public String group(String groupName) {
        return matcher.group(groupName);
    }

    @Override
    public int start(String groupName) {
        return matcher.start(groupName);
    }

    @Override
    public int end(String groupName) {
        return matcher.end(groupName);
    }

    @Override
    public RegexpMatcher reset() {
        matcher.reset();
        return this;
    }

    @Override
    public boolean find() {
        return matcher.find();
    }

    @Override
    public boolean find(int start) {
        return matcher.find(start);
    }

    @Override
    public RegexpMatcher appendReplacement(StringBuffer b, String replacement) {
        matcher.appendReplacement(b, replacement);
        return this;
    }

    @Override
    public void appendTail(StringBuffer b) {
        matcher.appendTail(b);
    }

    @Override
    public int groupCount() {
        return matcher.groupCount();
    }

    static final Method Pattern_namedGroups = Reflects.getDeclaredMethod(Pattern.class, "namedGroups");

    @Override
    public List<Map<String, String>> namedGroups() {
        List<String> names = this.names();
        if (Objs.isEmpty(names)) {
            return Collects.emptyArrayList();
        }

        List<Map<String, String>> result = new ArrayList<Map<String, String>>();
        int nextIndex = 0;
        while (matcher.find(nextIndex)) {
            Map<String, String> matches = new LinkedHashMap<String, String>();
            for (String groupName : names) {
                String groupValue = matcher.group(groupName);
                matches.put(groupName, groupValue);
                nextIndex = matcher.end();
            }
            result.add(matches);
        }
        return result;
    }

    @Override
    public List<String> names() {
        Map<String, Integer> nameToIndexMap = nameToIndexMap();
        Set<String> names = null;
        if (nameToIndexMap != null) {
            names = nameToIndexMap.keySet();
        }
        return Collects.newArrayList(names);
    }

    private Map<String, Integer> nameToIndexMap() {
        Pattern pattern = matcher.pattern();
        Map<String, Integer> nameToIndexMap = Reflects.invoke(Pattern_namedGroups, pattern, null, true, true);
        return nameToIndexMap;
    }
}
