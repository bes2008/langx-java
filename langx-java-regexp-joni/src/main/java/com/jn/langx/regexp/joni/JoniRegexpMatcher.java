package com.jn.langx.regexp.joni;

import com.jn.langx.util.Chars;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.io.Charsets;
import com.jn.langx.util.regexp.RegexpMatcher;
import org.joni.Matcher;
import org.joni.NameEntry;
import org.joni.Option;
import org.joni.Region;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * @since 4.5.0
 */
final class JoniRegexpMatcher implements RegexpMatcher {
    /**
     * 原始字符串， 此处记录了两种形态：
     */
    private byte[] input;


    private Matcher matcher;
    private JoniRegexp regexp;


    /**
     * 最后一次匹配到时的索引
     * 这个是 byte[] input 中的 index，不是 text中的索引
     */
    int lastBeg = -1;
    int lastEnd = 0;

    /**
     * The index of the last position appended in a substitution.
     * 这个是 byte[] input 中的 index，不是 text中的索引
     * <p>
     * 为了 appendReplacement方法提供
     */
    int lastAppendPosition = 0;

    JoniRegexpMatcher(JoniRegexp regexp, CharSequence input) {
        this.regexp = regexp;
        this.init(input);
    }

    void init(CharSequence input){
        this.input = input.toString().getBytes(Charsets.UTF_8);
        this.matcher = regexp.regex.matcher(this.input);
    }

    private int toBytesIndex(int charIndex) {
        if (charIndex < 0) {
            return -1;
        }
        if (charIndex == 0) {
            return 0;
        }
        String substring = new String(this.input, Charsets.UTF_8).substring(0, charIndex);
        return substring.getBytes(Charsets.UTF_8).length;
    }

    private int toCharIndex(int bytesIndex) {
        if (bytesIndex < 0) {
            return -1;
        }
        if (bytesIndex == 0) {
            return 0;
        }
        return new String(input, 0, bytesIndex).length();
    }

    /**
     * {@inheritDoc}
     */
    public int start() {
        return toCharIndex(bytesStart());
    }


    /**
     * {@inheritDoc}
     */
    public String group() {
        return subBytesAsString(this.bytesStart(), this.bytesEnd());
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
     * {@inheritDoc}
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

    /**
     * {@inheritDoc}
     */
    public int groupCount() {
        Region region = this.matcher.getRegion();
        int count = region == null ? 0 : region.numRegs - 1;
        if (count < 0) {
            count = 0;
        }
        return count;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String group(final String groupName) {
        int[] begEnd = getGroupBeginEnd(groupName, true);
        if (begEnd == null || begEnd[0] < 0 || begEnd[1] < 0) {
            return null;
        }
        return subBytesAsString(begEnd[0], begEnd[1]);
    }

    /**
     * @return 返回的是 byte[] 中的 索引, JDK Regexp 返回的是 String的索引
     */
    private int bytesStart() {
        // 若被重置后, 不能直接执行 start(), end()，这个要跟JDK保持一致
        if (this.lastBeg == -1 && this.lastEnd == 0) {
            throw new IllegalStateException("No match available");
        }
        return this.matcher.getBegin();
    }

    /**
     * {@inheritDoc}
     */
    public int start(int group) {
        return toCharIndex(bytesStart(group));
    }

    /**
     * @return 返回的是 byte[] 中的 索引, JDK Regexp 返回的是 String的索引
     */
    private int bytesStart(int group) {
        return group == 0 ? this.bytesStart() : this.matcher.getRegion().beg[group];
    }

    /**
     * {@inheritDoc}
     */
    public int end() {
        return toCharIndex(bytesEnd());
    }

    /**
     * @return 返回的是 byte[] 中的 索引, JDK Regexp 返回的是 String的索引
     */
    private int bytesEnd() {
        // 若被重置后, 不能直接执行 start(), end()，这个要跟JDK保持一致
        if (this.lastBeg == -1 && this.lastEnd == 0) {
            throw new IllegalStateException("No match available");
        }
        return this.matcher.getEnd();
    }

    /**
     * {@inheritDoc}
     */
    public int end(int group) {
        return toCharIndex(bytesEnd(group));
    }

    /**
     * @return 返回的是 byte[] 中的 索引, JDK Regexp 返回的是 String的索引
     */
    private int bytesEnd(int group) {
        return group == 0 ? this.bytesEnd() : this.matcher.getRegion().end[group];
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int start(String groupName) {
        return toCharIndex(bytesStart(groupName));
    }

    /**
     * @return 返回的是 byte[] 中的 索引, JDK Regexp 返回的是 String的索引
     */
    private int bytesStart(String groupName) {
        return getGroupBeginEnd(groupName, false)[0];
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int end(String groupName) {
        return toCharIndex(bytesEnd(groupName));
    }

    /**
     * @return 返回的是 byte[] 中的 索引, JDK Regexp 返回的是 String的索引
     */
    private int bytesEnd(String groupName) {
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
            beg = this.bytesStart();
            end = this.bytesEnd();
        } else {
            Region region = this.matcher.getRegion();
            beg = region.beg[groupIndex];
            end = region.end[groupIndex];
        }
        return new int[]{beg, end};
    }

    private boolean hasGroup(String groupName) {
        NameEntry nameEntry = this.regexp.getNamedGroupMap().get(groupName);
        return nameEntry != null;
    }

    private int getGroupIndex(final String groupName) {
        NameEntry nameEntry = this.regexp.getNamedGroupMap().get(groupName);
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

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean matches() {
        boolean found = search(0);
        return found && this.bytesEnd() >= this.input.length;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RegexpMatcher reset() {
        this.lastBeg = -1;
        this.lastEnd = 0;
        this.lastAppendPosition = 0;
        return this;
    }

    @Override
    public RegexpMatcher reset(CharSequence content) {
        this.reset();
        this.init(content);
        return this;
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
    public boolean find(int start) {
        int bytesStart = toBytesIndex(start);
        if (bytesStart < 0 || bytesStart > this.input.length) {
            throw new IndexOutOfBoundsException("Illegal start index");
        }
        return search(start);
    }


    /**
     * 基于指定位置开始搜索
     *
     * @param start 开始位置, bytesIndex
     * @return 返回是否找到
     */
    private boolean search(int start) {
        if (start >= 0 && start <= this.input.length) {
            // joniMatcher.search 返回值是匹配到时的开始索引
            int stIdx = this.matcher.search(start, this.input.length, Option.DEFAULT);
            boolean found = stIdx > -1;
            // 搜索完毕后跟上次的位置一样
            try {
                if (stIdx == this.lastBeg && this.lastEnd == this.bytesEnd()) {
                    found = false;
                }
            }catch (IllegalStateException ex){
                return false;
            }
            this.lastBeg = start;
            this.lastEnd = this.bytesEnd();
            return found;
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RegexpMatcher appendReplacement(StringBuilder sb, String replacement) {
        this.appendReplacement(sb, this.group(), replacement);
        return this;
    }

    private void appendReplacement(StringBuilder sb, String matched, String replacement) {
        // If no match, return error
        if (isInitialStatus()) {
            throw new IllegalStateException("No match available");
        }
        // Process substitution string to replace group references with groups
        int cursor = 0;
        StringBuilder result = new StringBuilder();

        while (cursor < replacement.length()) {
            char nextChar = replacement.charAt(cursor);
            if (nextChar == '\\') {
                cursor++;
                if (cursor == replacement.length()) {
                    throw new IllegalArgumentException("character to be escaped is missing");
                }
                nextChar = replacement.charAt(cursor);
                result.append(nextChar);
                cursor++;
            } else if (nextChar == '$') {
                // Skip past $
                cursor++;
                // Throw IAE if this "$" is the last character in replacement
                if (cursor == replacement.length()) {
                    throw new IllegalArgumentException("Illegal group reference: group index is missing");
                }
                nextChar = replacement.charAt(cursor);
                int refNum = -1;
                if (nextChar == '{') {
                    cursor++;
                    StringBuilder gsb = new StringBuilder();
                    while (cursor < replacement.length()) {
                        nextChar = replacement.charAt(cursor);
                        if (Chars.isLowerCase(nextChar) || Chars.isUpperCase(nextChar) || Chars.isDigit(nextChar)) {
                            gsb.append(nextChar);
                            cursor++;
                        } else {
                            break;
                        }
                    }
                    if (gsb.length() == 0)
                        throw new IllegalArgumentException("named capturing group has 0 length name");
                    if (nextChar != '}')
                        throw new IllegalArgumentException("named capturing group is missing trailing '}'");
                    String gname = gsb.toString();
                    if (Chars.isDigit(gname.charAt(0)))
                        throw new IllegalArgumentException("capturing group name {" + gname + "} starts with digit character");
                    if (!hasGroup(gname))
                        throw new IllegalArgumentException("No group with name {" + gname + "}");
                    refNum = getGroupIndex(gname);

                    cursor++;
                } else {
                    // The first number is always a group
                    refNum = (int) nextChar - '0';
                    if ((refNum < 0) || (refNum > 9)) {
                        throw new IllegalArgumentException("Illegal group reference");
                    }
                    cursor++;
                    // Capture the largest legal group string
                    boolean done = false;
                    while (!done) {
                        if (cursor >= replacement.length()) {
                            break;
                        }
                        int nextDigit = replacement.charAt(cursor) - '0';
                        if ((nextDigit < 0) || (nextDigit > 9)) { // not a number
                            break;
                        }
                        int newRefNum = (refNum * 10) + nextDigit;
                        if (groupCount() < newRefNum) {
                            done = true;
                        } else {
                            refNum = newRefNum;
                            cursor++;
                        }
                    }
                }
                // Append group
                if (this.bytesStart(refNum) != -1 && this.bytesEnd(refNum) != -1) {
                    result.append(subBytesAsString(this.bytesStart(refNum), this.bytesEnd(refNum)));
                }
            } else {
                result.append(nextChar);
                cursor++;
            }
        }
        // Append the intervening text
        String a = subBytesAsString(lastAppendPosition, this.bytesStart());
        sb.append(a);
        // Append the match substitution
        sb.append(result);

        lastAppendPosition = this.bytesEnd();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void appendTail(StringBuilder sb) {
        sb.append(subBytesAsString(lastAppendPosition, input.length));
    }

    @Override
    public List<Map<String, String>> namedGroups() {
        List<Map<String, String>> result = new ArrayList<Map<String, String>>();
        List<String> groupNames = names();

        if (groupNames.isEmpty()) {
            return result;
        }

        int nextIndex = 0;
        while (this.find(nextIndex)) {
            Map<String, String> matches = new LinkedHashMap<String, String>();

            for (String groupName : groupNames) {
                String groupValue = this.group(groupName);
                matches.put(groupName, groupValue);
                nextIndex = this.end();
            }

            result.add(matches);
            if(matcher.getEnd()>= this.input.length){
                break;
            }
        }
        return result;
    }

    @Override
    public List<String> names() {
        Map<String, NameEntry> named = this.regexp.getNamedGroupMap();
        return Collects.asList(named.keySet());
    }

    @Override
    public void interrupt() {
        this.matcher.interrupt();
    }
}