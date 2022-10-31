package com.jn.langx.util;

import com.jn.langx.text.tokenizer.CommonTokenizer;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.function.Predicate;

import java.util.List;

/**
 * @since 5.1.0
 */
public class StringTokenizer extends CommonTokenizer {
    private static List<String> DEFAULT_DELIMITERS = Collects.newArrayList(" ", "\n", "\t", "\r");
    private List<String> delimiters;

    public StringTokenizer(String str) {
        this(str, null);
    }

    public StringTokenizer(String str, String... delimiters) {
        this(str, false, delimiters);
    }

    public StringTokenizer(String str, boolean returnDelimiter, String... delimiters) {
        super(str, returnDelimiter);
        this.delimiters = Objs.useValueIfEmpty(Collects.asList(delimiters), DEFAULT_DELIMITERS);
    }

    @Override
    protected String getIfDelimiterStart(final long position, char c) {
        final String s = c + "";
        String delimiter = Pipeline.of(this.delimiters)
                .findFirst(new Predicate<String>() {
                    @Override
                    public boolean test(String delimiter) {
                        if (Strings.startsWith(delimiter, s)) {
                            if (getBuffer().limit() - position > delimiter.length()) {
                                String substring = getBuffer().substring(position, position + delimiter.length());
                                return Objs.equals(substring, delimiter);
                            } else {
                                return false;
                            }
                        } else {
                            return false;
                        }
                    }
                });

        return delimiter;
    }

    public String next(List<String> delimiters) {
        Preconditions.checkNotEmpty(delimiters);
        this.delimiters = delimiters;
        return getNext();
    }

}
