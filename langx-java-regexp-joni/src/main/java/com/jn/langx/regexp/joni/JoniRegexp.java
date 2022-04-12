package com.jn.langx.regexp.joni;


import java.util.List;
import java.util.Map;
import java.util.regex.PatternSyntaxException;

import com.jn.langx.exception.ParseException;
import com.jn.langx.text.StringTemplates;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.bit.BitVector;
import com.jn.langx.util.io.Charsets;
import com.jn.langx.util.regexp.Groups;
import com.jn.langx.util.regexp.Option;
import com.jn.langx.util.regexp.Regexp;
import com.jn.langx.util.regexp.RegexpMatcher;
import org.jcodings.specific.UTF8Encoding;
import org.joni.*;
import org.joni.exception.JOniException;

public class JoniRegexp implements Regexp {
    private final String pattern;
    private Option option;
    private BitVector groupsInNegativeLookahead;


    private Regex regex;

    public JoniRegexp(String pattern) {
        this(pattern, Option.DEFAULT);
    }

    public JoniRegexp(String pattern, Option option) {
        this.pattern = pattern.length() == 0 ? "(?:)" : pattern;
        this.option = option;

        try {
            JoniRegexpScanner parsed;
            try {
                parsed = JoniRegexpScanner.scan(pattern);
            } catch (PatternSyntaxException ex) {
                throw ex;
            }

            String javaPattern = parsed.getJavaPattern();
            byte[] patternBytes = javaPattern.getBytes(Charsets.UTF_8);
            this.regex = new Regex(patternBytes, 0, patternBytes.length, toJoniFlags(option), UTF8Encoding.INSTANCE, Syntax.Java);
            this.groupsInNegativeLookahead = parsed.getGroupsInNegativeLookahead();
        } catch (JOniException ex1) {
            throwParseException("syntax", ex1.getMessage());
        } catch (PatternSyntaxException ex2) {
            throwParseException("syntax", ex2.getMessage());
        } catch (StackOverflowError var8) {
            throw new RuntimeException(var8);
        }
    }

    /**
     * 类似于 JavaScript 的 flags
     *
     * @param pattern 正则
     * @param flags   igm
     */
    public JoniRegexp(String pattern, String flags) {
        this(pattern, Option.fromJavaScriptFlags(flags));
    }


    public String getPattern() {
        return this.pattern;
    }


    public BitVector getGroupsInNegativeLookahead() {
        return this.groupsInNegativeLookahead;
    }


    protected static void throwParseException(String key, String str) throws ParseException {
        throw new ParseException(StringTemplates.formatWithPlaceholder("joni regexp parse error: key: {}, error: {}", key, str));
    }


    public RegexpMatcher matcher(CharSequence input) {
        Preconditions.checkNotNull(regex);
        Map<String, List<Groups.GroupInfo>> groupInfo = Groups.extractGroupInfo(this.pattern);
        return new JoniRegexpMatcher(this.regex, input, groupInfo);
    }

    @Override
    public String[] split(CharSequence text) {
        return new String[0];
    }

    @Override
    public com.jn.langx.util.regexp.Option getOption() {
        return this.option;
    }


    public static int toJoniFlags(Option opt) {
        int option = 8;
        if (opt.isIgnoreCase()) {
            option |= 1;
        }

        if (opt.isMultiline()) {
            option &= -9;
            option |= 64;
        }
        return option;
    }

}