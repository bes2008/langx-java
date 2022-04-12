package com.jn.langx.regexp.joni;

import com.jn.langx.util.bit.BitVector;
import com.jn.langx.util.io.Charsets;
import com.jn.langx.util.regexp.Groups;
import com.jn.langx.util.regexp.RegexpMatcher;
import org.joni.Matcher;
import org.joni.Option;
import org.joni.Regex;
import org.joni.Region;

import java.util.*;

final class JoniRegexpMatcher implements RegexpMatcher {
    final byte[] input;
    final Matcher joniMatcher;
    private List<String> groupNames;
    private Map<String, List<Groups.GroupInfo>> groupInfo;
    private BitVector groupsInNegativeLookahead;

    /**
     * 最后一次匹配到时的索引
     */
    int nextSearchIdx = 0;

    JoniRegexpMatcher(Regex regex, CharSequence input, BitVector groupsInNegativeLookahead, Map<String, List<Groups.GroupInfo>> groupInfo) {
        this.input = input.toString().getBytes(Charsets.UTF_8);
        this.joniMatcher = regex.matcher(this.input);
        this.groupInfo = groupInfo;
        this.groupsInNegativeLookahead = groupsInNegativeLookahead;
    }

    // tested ok
    public int start() {
        return this.joniMatcher.getBegin();
    }

    // tested ok
    public int start(int group) {
        return group == 0 ? this.start() : this.joniMatcher.getRegion().beg[group];
    }

    // tested ok
    public int end() {
        return this.joniMatcher.getEnd();
    }

    // tested ok
    public int end(int group) {
        return group == 0 ? this.end() : this.joniMatcher.getRegion().end[group];
    }

    // tested ok
    public String group() {
        return subBytesAsString(start(), end());
    }

    // tested ok
    private String subBytesAsString(int start, int end) {
        int length = end - start;
        byte[] bytes = new byte[length];
        System.arraycopy(this.input, start, bytes, 0, length);
        return new String(bytes, Charsets.UTF_8);
    }


    public String group(int group) {
        if (group < 0 || group > groupCount()) {
            throw new IndexOutOfBoundsException("No group " + group);
        }
        if (group == 0) {
            return this.group();
        } else {
            Region region = this.joniMatcher.getRegion();
            return subBytesAsString(region.beg[group], region.end[group]);
        }
    }

    public int groupCount() {
        Region region = this.joniMatcher.getRegion();
        return region == null ? 0 : region.numRegs - 1;
    }

    @Override
    public String group(String groupName) {
        this.groupInfo.get(groupName);
        int idx = Groups.groupIndex(this.groupInfo, groupName);
        return group(idx);
    }

    // tested ok
    @Override
    public boolean matches() {
        return find(false);
    }

    // tested ok
    @Override
    public boolean find() {
        return find(true);
    }

    // tested ok
    private boolean find(boolean updateIndexAfterFound) {
        if (this.nextSearchIdx >= 0 && this.nextSearchIdx < this.input.length) {
            boolean found = this.joniMatcher.search(this.nextSearchIdx, this.input.length, Option.NONE) > -1;
            if (!found) {
                this.nextSearchIdx = -1;
            } else {
                if (updateIndexAfterFound) {
                    this.nextSearchIdx = this.end();
                }
            }
            return found;
        }
        return false;
    }

    @Override
    public RegexpMatcher appendReplacement(StringBuffer b, String replacement) {
        return null;
    }

    @Override
    public void appendTail(StringBuffer b) {

    }
}