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
    private final int max;

    /**
     * 已找到的分隔符的个数
     */
    private int foundDelimiterCount = 0;
    private long lastDelimiterStartPosition = -1;

    /**
     * 是否忽略大小写
     */
    private boolean ignoreCase = false;

    public StrTokenizer(String str) {
        this(str, (String)null);
    }

    public StrTokenizer(String str, String... delimiters) {
        this(str, false, delimiters);
    }

    public StrTokenizer(String str, boolean returnDelimiter, String... delimiters) {
        this(str, returnDelimiter, false, delimiters);
    }

    public StrTokenizer(String str, boolean returnDelimiter, int max, String... delimiters) {
        this(str, returnDelimiter, false, max, delimiters);
    }

    public StrTokenizer(String str, boolean returnDelimiter, boolean ignoreCase, String... delimiters) {
        this(str, returnDelimiter, ignoreCase, -1, delimiters);
    }

    public StrTokenizer(String str, boolean returnDelimiter, boolean ignoreCase, int max, String... delimiters) {
        super(str, returnDelimiter);
        setDelimiters(Collects.asList(delimiters));
        this.max = max < 0 ? Integer.MAX_VALUE : max;
        this.ignoreCase = ignoreCase;
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
    protected String getDelimiter(long start, long end) {
        String delimiter = super.getDelimiter(start, end);
        foundDelimiterCount++;
        return delimiter;
    }

    @Override
    protected String getIfDelimiterStart(final long position, char c) {
        boolean continueFind = foundDelimiterCount < max;
        if (continueFind) {
            String delimiter = getIfDelimiterStartInternal(position, c);
            if (delimiter != null) {
                if (lastDelimiterStartPosition == position) {
                    // 本次为 获取 delimiter
                } else {
                    // foundDelimiterCount++;
                    lastDelimiterStartPosition = position;
                }
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
                            if (Strings.startsWith(delimiter, s, ignoreCase)) {
                                if (getBuffer().limit() - position >= delimiter.length()) {
                                    String substring = getBuffer().substring(position, position + delimiter.length());
                                    return Strings.equals(substring, delimiter, ignoreCase);
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
