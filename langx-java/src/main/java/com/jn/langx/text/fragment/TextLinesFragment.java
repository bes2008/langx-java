package com.jn.langx.text.fragment;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;
import com.jn.langx.io.resource.Resource;
import com.jn.langx.util.Emptys;
import com.jn.langx.util.Objs;
import com.jn.langx.util.Strings;
import com.jn.langx.util.Throwables;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.iter.UnmodifiableIterator;
import com.jn.langx.util.io.IOs;
import com.jn.langx.util.io.file.Files;
import com.jn.langx.util.logging.Loggers;
import com.jn.langx.util.regexp.Regexp;
import com.jn.langx.util.regexp.RegexpMatcher;
import com.jn.langx.util.regexp.Regexps;
import org.slf4j.Logger;

import java.io.*;
import java.util.List;

/**
 * 用于单行、多行文本拆分
 *
 * @since 5.0.1
 */
public class TextLinesFragment extends UnmodifiableIterator<String> implements Closeable {
    private static final Logger logger = Loggers.getLogger(TextLinesFragment.class);

    @NonNull
    private BufferedReader reader;

    @Nullable
    private MultilineConfig multiline;

    /**
     * 当 multiline 不为 null 时，会有 该属性
     */
    @Nullable
    private Regexp regexp;

    /**
     * end of stream
     */
    private boolean eof = false;

    /**
     * 下一条记录
     */
    private String nextRecord;

    private String flagLine;
    private List<String> nonFlagLines = Collects.emptyArrayList();

    public TextLinesFragment(String filepath) {
        this(Files.openInputStream(new File(filepath)));
    }

    public TextLinesFragment(Resource resource) throws IOException {
        this(resource.getInputStream());
    }


    public TextLinesFragment(InputStream inputStream) {
        this.reader = new BufferedReader(new InputStreamReader(new BufferedInputStream(inputStream)));
    }

    public void setMultiline(MultilineConfig multiline) {
        if (multiline != null) {
            String pattern = multiline.getPattern();
            if (Objs.isNotEmpty(pattern)) {
                this.multiline = multiline;
            } else {
                this.multiline = null;
                this.regexp = null;
                logger.warn("invalid multiline.pattern: {}", multiline);
            }
        }

        if (this.multiline != null) {
            this.regexp = Regexps.createRegexp(this.multiline.getPattern());
        }
    }

    @Override
    public boolean hasNext() {
        if (!eof) {
            this.nextRecord = this.readRecord();
            return Objs.isNotNull(this.nextRecord);
        } else {
            IOs.close(this);
            return false;
        }
    }

    public String next() {
        String r = this.nextRecord;
        this.nextRecord = null;
        return r;
    }

    private String readLine() {
        String line;
        try {
            line = this.reader.readLine();
        } catch (IOException e) {
            throw Throwables.wrapAsRuntimeException(e);
        }
        return line;
    }

    private String readRecord() {
        String record = null;
        if (this.multiline == null) {
            record = readLine();

            if (record == null) {
                this.eof = true;
            }

            return record;
        }

        while (record == null) {
            String line = readLine();

            if (line == null) {
                if (Objs.isNotEmpty(this.nonFlagLines)) {
                    record = concatMultiline();
                    this.nonFlagLines.clear();
                }
                this.eof = true;
                return record;
            }

            RegexpMatcher matcher = this.regexp.matcher(line);
            boolean matches = matcher.matches();
            // 不对 pattern 取反
            if (!this.multiline.isNegate()) {
                // 找到了一个新的不匹配行
                if (!matches) {
                    if (this.multiline.getMatch() == MultilineConfig.Match.BEFORE) {
                        // 加在 下一个 不匹配的行 之前
                        this.flagLine = line;
                        record = concatMultiline();
                        this.nonFlagLines.clear();
                    } else {
                        // 加在 上一个 不匹配的行 之后
                        record = concatMultiline();
                        this.flagLine = line;
                        this.nonFlagLines.clear();
                    }
                } else {
                    this.nonFlagLines.add(line);
                }
            } else {
                // 对pattern 取反


                if (!matches) {
                    this.nonFlagLines.add(line);
                } else {
                    // 找到了一个新的 匹配行
                    if (this.multiline.getMatch() == MultilineConfig.Match.BEFORE) {
                        // 加在 下一个匹配的行之前
                        this.flagLine = line;
                        record = concatMultiline();
                        this.nonFlagLines.clear();
                    } else {
                        // 加在 上一个匹配的行之后
                        record = concatMultiline();
                        this.flagLine = line;
                        this.nonFlagLines.clear();
                    }
                }
            }
        }
        return record;
    }


    private String concatMultiline() {
        if (Emptys.isAllEmpty(this.flagLine, this.nonFlagLines)) {
            return null;
        }
        String noFlagLinesString = Objs.isEmpty(this.nonFlagLines) ? "" : Strings.join(this.multiline.getConcatSeparator(), this.nonFlagLines);

        StringBuilder builder = new StringBuilder(noFlagLinesString.length() + 255);
        if (this.multiline.getMatch() == MultilineConfig.Match.BEFORE) {

            if (Strings.isNotEmpty(noFlagLinesString)) {
                builder.append(noFlagLinesString);
            }

            boolean addSeparator = Emptys.isNoneEmpty(noFlagLinesString, this.flagLine);
            if (addSeparator) {
                builder.append(this.multiline.getConcatSeparator());
            }

            if (Strings.isNotEmpty(this.flagLine)) {
                builder.append(this.flagLine);
            }
        } else {

            if (Strings.isNotEmpty(this.flagLine)) {
                builder.append(this.flagLine);
            }

            boolean addSeparator = Emptys.isNoneEmpty(noFlagLinesString, this.flagLine);
            if (addSeparator) {
                builder.append(this.multiline.getConcatSeparator());
            }

            if (Strings.isNotEmpty(noFlagLinesString)) {
                builder.append(noFlagLinesString);
            }

        }
        return builder.toString();
    }

    @Override
    public void close() throws IOException {
        if (this.reader != null) {
            IOs.close(this.reader);
            this.reader = null;
        }
    }
}
