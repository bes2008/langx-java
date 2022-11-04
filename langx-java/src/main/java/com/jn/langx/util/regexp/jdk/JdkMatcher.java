package com.jn.langx.util.regexp.jdk;

import com.jn.langx.util.Objs;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.regexp.RegexpMatcher;

import java.util.*;
import java.util.regex.Matcher;

/**
 * 该类只能在 JDK7 以及更高版本使用
 *
 * @since 4.5.0
 */
class JdkMatcher implements RegexpMatcher {
    private Matcher matcher;
    private JdkRegexp regexp;

    JdkMatcher(JdkRegexp regexp, Matcher matcher) {
        this.regexp = regexp;
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
            if(matcher.hitEnd()){
                break;
            }
        }
        return result;
    }

    @Override
    public List<String> names() {
        return Collects.asList(this.regexp.getNamedGroups());
    }

}
