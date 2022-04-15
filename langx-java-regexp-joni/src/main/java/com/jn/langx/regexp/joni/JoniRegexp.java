package com.jn.langx.regexp.joni;


import com.jn.langx.exception.ParseException;
import com.jn.langx.text.StringTemplates;
import com.jn.langx.util.Objs;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.NonAbsentHashMap;
import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.function.Consumer2;
import com.jn.langx.util.function.Predicate;
import com.jn.langx.util.function.Supplier;
import com.jn.langx.util.io.Charsets;
import com.jn.langx.util.regexp.Option;
import com.jn.langx.util.regexp.Regexp;
import com.jn.langx.util.regexp.RegexpMatcher;
import com.jn.langx.util.struct.Holder;
import org.jcodings.specific.UTF8Encoding;
import org.joni.NameEntry;
import org.joni.Regex;
import org.joni.Syntax;
import org.joni.exception.JOniException;

import java.util.*;
import java.util.regex.PatternSyntaxException;

/**
 * @since 4.5.0
 */
public class JoniRegexp implements Regexp {
    private final String pattern;
    private Option option;
    Regex regex;

    private Holder<Map<String, NameEntry>> namedGroupMapHolder = null;

    public JoniRegexp(String pattern) {
        this(pattern, Option.DEFAULT);
    }

    public JoniRegexp(String pattern, Option option) {
        this.pattern = pattern.length() == 0 ? "(?:)" : pattern;
        this.option = option;

        try {
            byte[] patternBytes = pattern.getBytes(Charsets.UTF_8);
            this.regex = new Regex(patternBytes, 0, patternBytes.length, toJoniFlags(option), UTF8Encoding.INSTANCE, Syntax.Java);
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

    /**
     * {@inheritDoc}
     */
    public String getPattern() {
        return this.pattern;
    }

    /**
     * {@inheritDoc}
     */
    public RegexpMatcher matcher(CharSequence input) {
        Preconditions.checkNotNull(regex);
        return new JoniRegexpMatcher(this, input);
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

    private static void throwParseException(String key, String str) throws ParseException {
        throw new ParseException(StringTemplates.formatWithPlaceholder("joni regexp parse error: key: {}, error: {}", key, str));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String[] split(CharSequence text) {
        return split(text, 0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String[] split(CharSequence input, int limit) {
        int index = 0;
        boolean matchLimited = limit > 0;
        List<String> matchList = new ArrayList<String>();
        RegexpMatcher m = matcher(input);

        // Add segments before each match found
        while (m.find()) {
            if (!matchLimited || matchList.size() < limit - 1) {
                if (index == 0 && index == m.start() && m.start() == m.end()) {
                    // no empty leading substring included for zero-width match
                    // at the beginning of the input char sequence.
                    continue;
                }
                String match = input.subSequence(index, m.start()).toString();
                matchList.add(match);
                index = m.end();
            } else if (matchList.size() == limit - 1) { // last one
                String match = input.subSequence(index,
                        input.length()).toString();
                matchList.add(match);
                index = m.end();
            }
        }

        // If no match was found, return this
        if (index == 0)
            return new String[]{input.toString()};

        // Add remaining segment
        if (!matchLimited || matchList.size() < limit)
            matchList.add(input.subSequence(index, input.length()).toString());

        // Construct result
        int resultSize = matchList.size();
        if (limit == 0)
            while (resultSize > 0 && matchList.get(resultSize - 1).equals(""))
                resultSize--;
        String[] result = new String[resultSize];
        return matchList.subList(0, resultSize).toArray(result);
    }

    @Override
    public String toString() {
        return getPattern();
    }

    Map<String,NameEntry> getNamedGroupMap() {
        if (namedGroupMapHolder == null) {
            final Map<String, NameEntry> map = new LinkedHashMap<String, NameEntry>();
            Iterator<NameEntry> iter = regex.namedBackrefIterator();
            Collects.forEach(iter, new Consumer2<Integer, NameEntry>() {
                @Override
                public void accept(Integer key, NameEntry entry) {
                    String name = new String(entry.name, entry.nameP, entry.nameEnd - entry.nameP, Charsets.UTF_8);
                    map.put(name, entry);
                }
            });
            this.namedGroupMapHolder = new Holder<Map<String, NameEntry>>(map);
        }
        return this.namedGroupMapHolder.get();
    }

    @Override
    public List<String> getNamedGroups() {
        return Collects.newArrayList(getNamedGroupMap().keySet());
    }
}