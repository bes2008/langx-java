package com.jn.langx.util.io;


import com.jn.langx.annotation.NonNull;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class LineDelimiter {
    public static final char CR = '\r';
    public static final char LF = '\n';
    public static final String CR_LF = "" + CR + LF;

    /**
     * the line delimiter constant of the current O/S.
     */
    public static final LineDelimiter DEFAULT;

    static {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        PrintWriter out = new PrintWriter(new OutputStreamWriter(bout, Charsets.UTF_8), true);
        out.println();
        DEFAULT = new LineDelimiter(new String(bout.toByteArray(),Charsets.UTF_8));
    }

    public static final LineDelimiter CRLF = new LineDelimiter(CR_LF);

    public static final LineDelimiter UNIX = new LineDelimiter("\n");
    public static final LineDelimiter WINDOWS = CRLF;
    public static final LineDelimiter MAC = new LineDelimiter("\r");
    public static final LineDelimiter NUL = new LineDelimiter("\0");

    private final String value;

    public LineDelimiter(@NonNull String delimiter) {
        this.value = delimiter;
    }

    public String getValue() {
        return value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return value.hashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof LineDelimiter)) {
            return false;
        }

        LineDelimiter that = (LineDelimiter) o;

        return this.value.equals(that.value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        if (value.length() == 0) {
            return "delimiter: auto";
        } else {
            StringBuilder buf = new StringBuilder();
            buf.append("delimiter:");

            for (int i = 0; i < value.length(); i++) {
                buf.append(" 0x");
                buf.append(Integer.toHexString(value.charAt(i)));
            }

            return buf.toString();
        }
    }
}