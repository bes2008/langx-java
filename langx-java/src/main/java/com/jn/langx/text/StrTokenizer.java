package com.jn.langx.text;

import com.jn.langx.text.tokenizer.CommonTokenizer;
import com.jn.langx.text.tokenizer.TokenFactory;
import com.jn.langx.util.Objs;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.function.Predicate;

import java.util.List;

/**
 * @since 5.1.0
 */
public class StrTokenizer extends CommonTokenizer<String> {
    private List<String> delimiters = Strings.WHITESPACE_CHAR;
    /**
     * 找到的分隔符最大个数，小于0 代表不限制
     * 找到max个分割符之后，不再进行分割。
     */
    private int max;

    /**
     * 已找到的分隔符的个数
     */
    private int foundDelimiterCount = 0;

    public StrTokenizer(String str) {
        this(str, null);
    }

    public StrTokenizer(String str, String... delimiters) {
        this(str, false, delimiters);
    }

    public StrTokenizer(String str, boolean returnDelimiter, String... delimiters) {
        this(str, returnDelimiter, -1, delimiters);
    }

    public StrTokenizer(String str, boolean returnDelimiter, int max, String... delimiters) {
        super(str, returnDelimiter);
        setDelimiters(Collects.asList(delimiters));
        this.max = max < 0 ? Integer.MAX_VALUE : max;
        this.tokenFactory = new TokenFactory<String>() {
            @Override
            public String get(String tokenContent, Boolean isDelimiter) {
                return tokenContent;
            }
        };
    }

    public void setDelimiters(List<String> delimiters) {
        this.delimiters = Objs.useValueIfEmpty(Pipeline.of(delimiters).clearNulls().asList(), this.delimiters);
    }

    @Override
    protected String getIfDelimiterStart(final long position, char c) {
        if (foundDelimiterCount < max) {
            String delimiter = getIfDelimiterStartInternal(position, c);
            if (delimiter != null) {
                foundDelimiterCount++;
            }
            return delimiter;
        } else {
            return null;
        }
    }

    private String getIfDelimiterStartInternal(final long position, char c) {
        final String s = c + "";
        if (this.delimiters.contains("")) {
            if (this.getBuffer().markValue() < position) {
                return "";
            } else {
                return null;
            }
        } else {
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
    }

    public String next(List<String> delimiters) {
        Preconditions.checkNotEmpty(delimiters);
        this.delimiters = delimiters;
        return getNext();
    }

}
