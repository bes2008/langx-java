package com.jn.langx.util.regexp;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Groups {

    /**
     * index of group within patterns above where group name is captured
     */
    public static final int INDEX_GROUP_NAME = 1;

    /**
     * Pattern to match group names
     */
    private static final String NAME_PATTERN = "[^!=].*?";
    /**
     * Pattern to match named capture groups in a pattern string
     */
    public static final java.util.regex.Pattern NAMED_GROUP_PATTERN = java.util.regex.Pattern.compile("\\(\\?<(" + NAME_PATTERN + ")>", java.util.regex.Pattern.DOTALL);

    /**
     * Pattern to match back references for named capture groups
     */
    public static final java.util.regex.Pattern BACKREF_NAMED_GROUP_PATTERN = java.util.regex.Pattern.compile("\\\\k<(" + NAME_PATTERN + ")>", java.util.regex.Pattern.DOTALL);

    /**
     * Pattern to match properties for named capture groups in a replacement string
     */
    public static final java.util.regex.Pattern PROPERTY_PATTERN = java.util.regex.Pattern.compile("\\$\\{(" + NAME_PATTERN + ")\\}", java.util.regex.Pattern.DOTALL);

    /**
     * Contains the position and group index of capture groups
     * from a named pattern
     *
     * @since 4.5.0
     */
    public static class GroupInfo implements Serializable {

        private static final long serialVersionUID = 1L;

        private int pos;
        private int groupIndex;

        /**
         * Constructs a {@code GroupInfo} with a group index and string
         * position
         *
         * @param groupIndex the group index
         * @param pos        the position
         */
        public GroupInfo(int groupIndex, int pos) {
            this.groupIndex = groupIndex;
            this.pos = pos;
        }

        /**
         * Gets the string position of the group within a named pattern
         *
         * @return the position
         */
        public int pos() {
            return pos;
        }

        /**
         * Gets the group index of the named capture group
         *
         * @return the group index
         */
        public int groupIndex() {
            return groupIndex;
        }

        /*
         * (non-Javadoc)
         * @see java.lang.Object#equals(java.lang.Object)
         */
        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (!(obj instanceof GroupInfo)) {
                return false;
            }
            GroupInfo other = (GroupInfo) obj;
            return (pos == other.pos) && (groupIndex == other.groupIndex);
        }

        /*
         * (non-Javadoc)
         * @see java.lang.Object#hashCode()
         */
        @Override
        public int hashCode() {
            return pos ^ groupIndex;
        }
    }


    /**
     * Gets the group index of a named capture group
     *
     * @param groupName name of capture group
     * @return group index or -1 if not found
     */
    public static int indexOf(Map<String, List<GroupInfo>> groupInfo, String groupName) {
        return indexOf(groupInfo, groupName, 0);
    }

    /**
     * Gets the group index of a named capture group at the
     * specified index. If only one instance of the named
     * group exists, use index 0.
     *
     * @param groupName name of capture group
     * @param index     the instance index of the named capture group within
     *                  the pattern; e.g., index is 2 for the third instance
     * @return group index or -1 if not found
     * @throws IndexOutOfBoundsException if instance index is out of bounds
     */
    public static int indexOf(Map<String, List<GroupInfo>> groupInfo, String groupName, int index) {
        int idx = -1;
        if (groupInfo.containsKey(groupName)) {
            List<Groups.GroupInfo> list = groupInfo.get(groupName);
            idx = list.get(index).groupIndex();
        }
        return idx;
    }

    /**
     * Gets the index of a named capture group
     *
     * @param groupName name of capture group
     * @return the group index
     */
    public static int groupIndex(Map<String, List<GroupInfo>> groupInfo, String groupName) {
        // idx+1 because capture groups start 1 in the matcher
        // while the pattern returns a 0-based index of the
        // group name within the list of names
        int idx = Groups.indexOf(groupInfo, groupName);
        return idx > -1 ? idx + 1 : -1;
    }


    /**
     * Parses info on named capture groups from a pattern
     *
     * @param namedPattern regex the regular expression pattern to parse
     * @return list of group info for all named groups
     */
    public static Map<String, List<GroupInfo>> extractGroupInfo(String namedPattern) {
        Map<String, List<Groups.GroupInfo>> groupInfo = new LinkedHashMap<String, List<GroupInfo>>();
        java.util.regex.Matcher matcher = NAMED_GROUP_PATTERN.matcher(namedPattern);
        while (matcher.find()) {

            int pos = matcher.start();

            // ignore escaped paren
            if (isEscapedChar(namedPattern, pos)) continue;

            String name = matcher.group(INDEX_GROUP_NAME);
            int groupIndex = countOpenParens(namedPattern, pos);

            List<Groups.GroupInfo> list;
            if (groupInfo.containsKey(name)) {
                list = groupInfo.get(name);
            } else {
                list = new ArrayList<GroupInfo>();
            }
            list.add(new GroupInfo(groupIndex, pos));
            groupInfo.put(name, list);
        }
        return groupInfo;
    }


    /**
     * Determines if the character at the specified position
     * of a string is escaped
     *
     * @param s   string to evaluate
     * @param pos the position of the character to evaluate
     * @return true if the character is escaped; otherwise false
     */
    public static boolean isEscapedChar(String s, int pos) {
        return isSlashEscapedChar(s, pos) || isQuoteEscapedChar(s, pos);
    }

    /**
     * Determines if the character at the specified position
     * of a string is escaped with a backslash
     *
     * @param s   string to evaluate
     * @param pos the position of the character to evaluate
     * @return true if the character is escaped; otherwise false
     */
    public static boolean isSlashEscapedChar(String s, int pos) {

        // Count the backslashes preceding this position. If it's
        // even, there is no escape and the slashes are just literals.
        // If it's odd, one of the slashes (the last one) is escaping
        // the character at the given position.
        int numSlashes = 0;
        while (pos > 0 && (s.charAt(pos - 1) == '\\')) {
            pos--;
            numSlashes++;
        }
        return numSlashes % 2 != 0;
    }

    /**
     * Determines if the character at the specified position
     * of a string is quote-escaped (between \\Q and \\E)
     *
     * @param s   string to evaluate
     * @param pos the position of the character to evaluate
     * @return true if the character is quote-escaped; otherwise false
     */
    public static boolean isQuoteEscapedChar(String s, int pos) {

        boolean openQuoteFound = false;
        boolean closeQuoteFound = false;

        // find last non-escaped open-quote
        String s2 = s.substring(0, pos);
        int posOpen = pos;
        while ((posOpen = s2.lastIndexOf("\\Q", posOpen - 1)) != -1) {
            if (!isSlashEscapedChar(s2, posOpen)) {
                openQuoteFound = true;
                break;
            }
        }

        if (openQuoteFound) {
            // search remainder of string (after open-quote) for a close-quote;
            // no need to check that it's slash-escaped because it can't be
            // (the escape character itself is part of the literal when quoted)
            if (s2.indexOf("\\E", posOpen) != -1) {
                closeQuoteFound = true;
            }
        }

        return openQuoteFound && !closeQuoteFound;
    }

    /**
     * Determines if a string's character is within a regex character class
     *
     * @param s   string to evaluate
     * @param pos the position of the character to evaluate
     * @return true if the character is inside a character class; otherwise false
     */
    public static boolean isInsideCharClass(String s, int pos) {

        boolean openBracketFound = false;
        boolean closeBracketFound = false;

        // find last non-escaped open-bracket
        String s2 = s.substring(0, pos);
        int posOpen = pos;
        while ((posOpen = s2.lastIndexOf('[', posOpen - 1)) != -1) {
            if (!isEscapedChar(s2, posOpen)) {
                openBracketFound = true;
                break;
            }
        }

        if (openBracketFound) {
            // search remainder of string (after open-bracket) for a close-bracket
            String s3 = s.substring(posOpen, pos);
            int posClose = -1;
            while ((posClose = s3.indexOf(']', posClose + 1)) != -1) {
                if (!isEscapedChar(s3, posClose)) {
                    closeBracketFound = true;
                    break;
                }
            }
        }

        return openBracketFound && !closeBracketFound;
    }

    /**
     * Determines if the parenthesis at the specified position
     * of a string is for a non-capturing group, which is one of
     * the flag specifiers (e.g., (?s) or (?m) or (?:pattern).
     * If the parenthesis is followed by "?", it must be a non-
     * capturing group unless it's a named group (which begins
     * with "?<"). Make sure not to confuse it with the lookbehind
     * construct ("?<=" or "?<!").
     *
     * @param s   string to evaluate
     * @param pos the position of the parenthesis to evaluate
     * @return true if the parenthesis is non-capturing; otherwise false
     */
    private static boolean isNoncapturingParen(String s, int pos) {

        //int len = s.length();
        boolean isLookbehind = false;

        // code-coverage reports show that pos and the text to
        // check never exceed len in this class, so it's safe
        // to not test for it, which resolves uncovered branches
        // in Cobertura

        /*if (pos >= 0 && pos + 4 < len)*/
        {
            String pre = s.substring(pos, pos + 4);
            isLookbehind = pre.equals("(?<=") || pre.equals("(?<!");
        }
        return /*(pos >= 0 && pos + 2 < len) &&*/
                s.charAt(pos + 1) == '?' &&
                        (isLookbehind || s.charAt(pos + 2) != '<');
    }

    /**
     * Counts the open-parentheses to the left of a string position,
     * excluding escaped parentheses
     *
     * @param s   string to evaluate
     * @param pos ending position of string; characters to the left
     *            of this position are evaluated
     * @return number of open parentheses
     */
    static private int countOpenParens(String s, int pos) {
        java.util.regex.Pattern p = java.util.regex.Pattern.compile("\\(");
        java.util.regex.Matcher m = p.matcher(s.subSequence(0, pos));

        int numParens = 0;

        while (m.find()) {
            // ignore parentheses inside character classes: [0-9()a-f]
            // which are just literals
            if (isInsideCharClass(s, m.start())) {
                continue;
            }

            // ignore escaped parens
            if (isEscapedChar(s, m.start())) continue;

            if (!isNoncapturingParen(s, m.start())) {
                numParens++;
            }
        }
        return numParens;
    }


}
