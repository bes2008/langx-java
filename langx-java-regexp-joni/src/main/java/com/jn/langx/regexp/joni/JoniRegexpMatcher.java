package com.jn.langx.regexp.joni;

import com.jn.langx.util.Bytes;
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

    int nextReadIndex = 0;

    JoniRegexpMatcher(Regex regex, CharSequence input, BitVector groupsInNegativeLookahead, Map<String, List<Groups.GroupInfo>> groupInfo) {
        this.input = input.toString().getBytes(Charsets.UTF_8);
        this.joniMatcher = regex.matcher(this.input);
        this.groupInfo = groupInfo;
        this.groupsInNegativeLookahead = groupsInNegativeLookahead;
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
        return subBytesAsString(start(), end());
    }

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

    @Override
    public boolean matches() {
        Region region = this.joniMatcher.getEagerRegion();
        return region != null;
    }

    @Override
    public boolean find() {
        if (nextReadIndex >= 0) {
            boolean found = this.joniMatcher.search(nextReadIndex, this.input.length, Option.NONE) > -1;
            if (!found) {
                this.nextReadIndex = -1;
            } else {
                this.nextReadIndex = this.end();
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