package com.jn.langx.util.regexp.jdk;

import com.jn.langx.util.regexp.RegexpMatcher;

import java.util.regex.Matcher;

/**
 * 该类只能在 JDK7 以及更高版本使用
 * @since 4.5.0
 */
class JdkMatcher implements RegexpMatcher {
    private Matcher matcher;

    JdkMatcher(Matcher matcher){
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
    public boolean find() {
        return matcher.find();
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
}
