package com.jn.langx.regexp.joni;

import com.jn.langx.util.bit.BitVector;
import com.jn.langx.util.io.Charsets;
import com.jn.langx.util.regexp.RegexpMatcher;
import com.jn.langx.util.regexp._Groups;
import org.joni.Matcher;
import org.joni.Option;
import org.joni.Regex;
import org.joni.Region;

import java.util.List;
import java.util.Map;

final class JoniRegexpMatcher implements RegexpMatcher {
    final byte[] input;
    final Matcher joniMatcher;
    private List<String> groupNames;
    private Map<String, List<_Groups.GroupInfo>> groupInfo;
    private BitVector groupsInNegativeLookahead;


    /**
     * 最后一次匹配到时的索引
     */
    int lastBeg = -1;
    int lastEnd = 0;

    JoniRegexpMatcher(Regex regex, CharSequence input, BitVector groupsInNegativeLookahead, Map<String, List<_Groups.GroupInfo>> groupInfo) {
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
        int idx = _Groups.groupIndex(this.groupInfo, groupName);
        return group(idx);
    }

    // tested ok
    @Override
    public boolean matches() {
        search(0);
        return this.end() >= this.input.length;
    }

    @Override
    public RegexpMatcher reset() {
        this.lastBeg = -1;
        this.lastEnd = 0;
        return this;
    }

    // tested ok
    @Override
    public boolean find() {
        if (this.lastBeg >= -1 && this.lastEnd >= this.lastBeg && this.lastEnd < this.input.length) {
            int nextSearchIndex = this.lastEnd;
            if (nextSearchIndex == this.lastBeg) {
                nextSearchIndex++;
            }
            return search(nextSearchIndex);
        }
        return false;
    }

    private boolean search(int start) {
        if (start >= 0 && start < this.input.length) {
            boolean found = this.joniMatcher.search(start, this.input.length, Option.NONE) > -1;
            this.lastBeg = start;
            this.lastEnd = this.end();
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