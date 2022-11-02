package com.jn.langx.util;

import com.jn.langx.text.tokenizer.CommonTokenizer;
import com.jn.langx.text.tokenizer.TokenFactory;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.function.Functions;
import com.jn.langx.util.function.Predicate;

import java.util.List;

/**
 * @since 5.1.0
 */
public class StrTokenizer extends CommonTokenizer<String> {
    private static List<String> DEFAULT_DELIMITERS = Strings.WHITESPACE_CHAR;
    private List<String> delimiters = DEFAULT_DELIMITERS;

    public StrTokenizer(String str) {
        this(str, null);
    }

    public StrTokenizer(String str, String... delimiters) {
        this(str, false, delimiters);
    }

    public StrTokenizer(String str, boolean returnDelimiter, String... delimiters) {
        super(str, returnDelimiter);
        setDelimiters(Collects.asList(delimiters));
        this.tokenFactory = new TokenFactory<String>() {
            @Override
            public String get(String tokenContent, Boolean isDelimiter) {
                return tokenContent;
            }
        };
    }

    public void setDelimiters(List<String> delimiters) {
        this.delimiters = Objs.useValueIfEmpty(Pipeline.of(delimiters).filter(Functions.<String>notEmptyPredicate()).asList(), this.delimiters);
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
