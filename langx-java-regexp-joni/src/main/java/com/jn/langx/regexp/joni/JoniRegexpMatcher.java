package com.jn.langx.regexp.joni;

import com.jn.langx.util.bit.BitVector;
import com.jn.langx.util.io.Charsets;
import com.jn.langx.util.regexp.Groups;
import com.jn.langx.util.regexp.RegexpMatcher;
import org.joni.Matcher;
import org.joni.Option;
import org.joni.Regex;
import org.joni.Region;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

final class JoniRegexpMatcher implements RegexpMatcher {
    final String input;
    final Matcher joniMatcher;
    private List<String> groupNames;
    private Map<String, List<Groups.GroupInfo>> groupInfo;
    private BitVector groupsInNegativeLookahead;

    JoniRegexpMatcher(Regex regex, CharSequence input, BitVector groupsInNegativeLookahead, Map<String, List<Groups.GroupInfo>> groupInfo) {
        this.input = input.toString();
        this.joniMatcher = regex.matcher(input.toString().getBytes(Charsets.UTF_8));
        this.groupInfo = groupInfo;
        this.groupsInNegativeLookahead = groupsInNegativeLookahead;
    }

    public boolean search(int start) {
        return this.joniMatcher.search(start, this.input.length(), 0, Option.NONE) > -1;
    }

    public String getInput() {
        return this.input;
    }

    public int start() {
        return this.joniMatcher.getBegin();
    }

    public int start(int group) {
        return group == 0 ? this.start() : this.joniMatcher.getRegion().beg[group];
    }

    public int end() {
        return this.joniMatcher.getEnd();
    }

    public int end(int group) {
        return group == 0 ? this.end() : this.joniMatcher.getRegion().end[group];
    }

    public String group() {
        return this.input.substring(start(), end());
    }

    public String group(int group) {
        if (group < 0 || group > groupCount()) {
            throw new IndexOutOfBoundsException("No group " + group);
        }
        if (group == 0) {
            return this.group();
        } else {
            Region region = this.joniMatcher.getRegion();
            return this.input.substring(region.beg[group], region.end[group]);
        }
    }

    public int groupCount() {
        Region region = this.joniMatcher.getRegion();
        return region == null ? 0 : region.numRegs - 1;
    }

    @Override
    public String group(String groupName) {
        this.groupInfo.get(groupName);
        int idx = Groups.groupIndex(this.groupInfo,groupName);
        return null;
    }


    private List<String> groupNames() {
        if (groupNames == null) {
            groupNames = new ArrayList<String>(groupInfo.keySet());
        }
        return Collections.unmodifiableList(groupNames);
    }

    @Override
    public boolean matches() {
        Region region = this.joniMatcher.getEagerRegion();
        return region != null;
    }

    @Override
    public boolean find() {
        return this.joniMatcher.getEagerRegion() != null;
    }

    @Override
    public RegexpMatcher appendReplacement(StringBuffer b, String replacement) {
        return null;
    }

    @Override
    public void appendTail(StringBuffer b) {

    }
}