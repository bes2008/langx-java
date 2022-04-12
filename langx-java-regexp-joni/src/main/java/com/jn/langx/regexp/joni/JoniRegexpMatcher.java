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

    /**
     * The range of string that last matched the pattern. If the last
     * match failed then first is -1; last initially holds 0 then it
     * holds the index of the end of the last match (which is where the
     * next search starts).
     */
    private int first = 0, last = 0;

    JoniRegexpMatcher(Regex regex, CharSequence input, BitVector groupsInNegativeLookahead, Map<String, List<Groups.GroupInfo>> groupInfo) {
        this.input = input.toString().getBytes(Charsets.UTF_8);
        this.last = this.input.length;
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
        if (first >= 0 && last != 0) {
            boolean found = this.joniMatcher.search(first, last, Option.NONE) > -1;
            if (!found) {
                this.first = -1;
                this.last = 0;
            } else {
                this.first = this.start();
                this.last = this.end();
            }
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