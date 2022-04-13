package com.jn.langx.regexp.joni;

import com.jn.langx.util.Objs;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.collection.NonAbsentHashMap;
import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.function.Predicate;
import com.jn.langx.util.function.Supplier;
import com.jn.langx.util.io.Charsets;
import com.jn.langx.util.regexp.RegexpMatcher;
import com.jn.langx.util.struct.Holder;
import org.joni.*;

import java.util.Map;

final class JoniRegexpMatcher implements RegexpMatcher {
    final byte[] input;
    final Matcher matcher;
    private Regex regex;
    private Map<String, Holder<NameEntry>> namedGroupMap;

    /**
     * 最后一次匹配到时的索引
     */
    int lastBeg = -1;
    int lastEnd = 0;

    JoniRegexpMatcher(Regex regex, CharSequence input) {
        this.regex = regex;
        this.input = input.toString().getBytes(Charsets.UTF_8);
        this.matcher = regex.matcher(this.input);
        init();
    }

    private void init() {
        namedGroupMap = new NonAbsentHashMap<String, Holder<NameEntry>>(new Supplier<String, Holder<NameEntry>>() {
            @Override
            public Holder<NameEntry> get(final String groupName) {
                NameEntry nameEntry = Pipeline.<NameEntry>of(regex.namedBackrefIterator())
                        .findFirst(new Predicate<NameEntry>() {
                            @Override
                            public boolean test(NameEntry entry) {
                                String name = new String(entry.name, entry.nameP, entry.nameEnd - entry.nameP, Charsets.UTF_8);
                                return Objs.equals(name, groupName);
                            }
                        });
                return new Holder<NameEntry>(nameEntry);
            }
        });
    }

    // tested ok
    public int start() {
        // 若被重置后, 不能直接执行 start(), end()，这个要跟JDK保持一致
        if (this.lastBeg == -1 && this.lastEnd == 0) {
            throw new IllegalStateException("No match available");
        }
        return this.matcher.getBegin();
    }

    // tested ok
    public int start(int group) {
        return group == 0 ? this.start() : this.matcher.getRegion().beg[group];
    }

    // tested ok
    public int end() {
        // 若被重置后, 不能直接执行 start(), end()，这个要跟JDK保持一致
        if (this.lastBeg == -1 && this.lastEnd == 0) {
            throw new IllegalStateException("No match available");
        }
        return this.matcher.getEnd();
    }

    // tested ok
    public int end(int group) {
        return group == 0 ? this.end() : this.matcher.getRegion().end[group];
    }

    // tested ok
    public String group() {
        return subBytesAsString(start(), end());
    }

    // tested ok
    private String subBytesAsString(int start, int end) {
        Preconditions.checkState(end >= 0 && end <= this.input.length && start >= 0 && start <= this.input.length);
        int length = end - start;
        byte[] bytes = new byte[length];
        System.arraycopy(this.input, start, bytes, 0, length);
        return new String(bytes, Charsets.UTF_8);
    }

    /**
     * @return 返回是否是初始状态， 所谓的初始状态，即还没有开始 匹配，或者被 reset()
     */
    private boolean isInitialStatus() {
        return this.lastBeg == -1;
    }

    /**
     * @param group the group index, based-1
     * @return group value
     */
    public String group(int group) {
        if (isInitialStatus()) {
            throw new IllegalStateException("No match found");
        }
        if (group < 0 || group > groupCount()) {
            throw new IndexOutOfBoundsException("No group " + group);
        }
        if (group == 0) {
            return this.group();
        } else {
            Region region = this.matcher.getRegion();
            int beg = region.beg[group];
            int end = region.end[group];
            if (beg < 0 || end < 0) {
                return null;
            }
            String groupValue = subBytesAsString(beg, end);
            return groupValue;
        }
    }

    // tested ok
    public int groupCount() {
        Region region = this.matcher.getRegion();
        int count = region == null ? 0 : region.numRegs - 1;
        if (count < 0) {
            count = 0;
        }
        return count;
    }

    // tested ok
    @Override
    public String group(final String groupName) {
        int[] begEnd = getGroupBeginEnd(groupName, true);
        if (begEnd == null || begEnd[0] < 0 || begEnd[1] < 0) {
            return null;
        }
        return subBytesAsString(begEnd[0], begEnd[1]);
    }

    // tested ok
    @Override
    public int start(String groupName) {
        return getGroupBeginEnd(groupName, false)[0];
    }

    // tested ok
    @Override
    public int end(String groupName) {
        return getGroupBeginEnd(groupName, false)[1];
    }

    private int[] getGroupBeginEnd(String groupName, boolean forGetGroupValue) {
        if (isInitialStatus()) {
            throw new IllegalStateException("No match found");
        }
        int groupIndex = getGroupIndex(groupName);
        if (groupIndex < 0 || groupIndex > groupCount()) {
            // 不存在该 group
            if (forGetGroupValue) {
                return null;
            } else {
                throw new IllegalArgumentException("No group with name <" + groupName + ">");
            }
        }
        int beg;
        int end;
        if (groupIndex == 0) {
            beg = this.start();
            end = this.end();
        } else {
            Region region = this.matcher.getRegion();
            beg = region.beg[groupIndex];
            end = region.end[groupIndex];
        }
        return new int[]{beg, end};
    }

    private int getGroupIndex(final String groupName) {
        Holder<NameEntry> nameEntryHolder = this.namedGroupMap.get(groupName);
        NameEntry nameEntry = nameEntryHolder.get();
        if (nameEntry == null) {
            return -1;
        }
        int[] backRefs = nameEntry.getBackRefs();
        if (backRefs.length > 0) {
            int groupIndex = backRefs[0];
            return groupIndex;
        }
        return -1;
    }

    // tested ok
    @Override
    public boolean matches() {
        boolean found = search(0);
        return found && this.end() >= this.input.length;
    }

    // tested ok
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

    // tested ok
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
            // joniMatcher.search 返回值是匹配到时的开始索引
            int stIdx = this.matcher.search(start, this.input.length, Option.DEFAULT);
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