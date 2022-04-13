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

    @Override
    public int start(String groupName) {
        return 0;
    }

    @Override
    public int end(String groupName) {
        return 0;
    }

    // tested ok
    @Override
    public boolean matches() {
        boolean found = search(0);
        return found && this.end() >= this.input.length;
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

    public boolean find(int start) {
        return search(start);
    }


    /**
     * 基于指定位置开始搜索
     *
     * @param start 开始位置
     * @return 返回是否找到
     */
    private boolean search(int start) {
        if (start >= 0 && start <= this.input.length) {
            /**
             * joniMatcher.search 返回值是匹配到时的开始索引
             */
            int stIdx = this.joniMatcher.search(start, this.input.length, Option.NONE);
            boolean found = stIdx > -1;
            // 搜索完毕后跟上次的位置一样
            if (stIdx == this.lastBeg && this.lastEnd == this.end()) {
                found = false;
            }
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