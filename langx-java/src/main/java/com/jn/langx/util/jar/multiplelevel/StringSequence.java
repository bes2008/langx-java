package com.jn.langx.util.jar.multiplelevel;


import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.NotEmpty;
import com.jn.langx.util.Preconditions;

/**
 * A {@link CharSequence} backed by a single shared {@link String}. Unlike a regular
 * {@link String}, {@link #subSequence(int, int)} operations will not copy the underlying
 * character array.
 */
final class StringSequence implements CharSequence {

    private final String source;

    private final int start;

    private final int end;

    private int hash;

    StringSequence(@NotEmpty String source) {
        this(source == null ? "" : source, 0, source == null ? 0 : source.length());
    }

    StringSequence(@NonNull String source, int start, int end) {
        Preconditions.checkNotNull(source, "Source must not be null");
        if (start < 0) {
            throw new StringIndexOutOfBoundsException(start);
        }
        if (end > source.length()) {
            throw new StringIndexOutOfBoundsException(end);
        }
        this.source = source;
        this.start = start;
        this.end = end;
    }

    StringSequence subSequence(int start) {
        return subSequence(start, length());
    }

    @Override
    public StringSequence subSequence(int start, int end) {
        int subSequenceStart = this.start + start;
        int subSequenceEnd = this.start + end;
        if (subSequenceStart > this.end) {
            throw new StringIndexOutOfBoundsException(start);
        }
        if (subSequenceEnd > this.end) {
            throw new StringIndexOutOfBoundsException(end);
        }
        if (start == 0 && subSequenceEnd == this.end) {
            return this;
        }
        return new StringSequence(this.source, subSequenceStart, subSequenceEnd);
    }

    boolean isEmpty() {
        return length() == 0;
    }

    @Override
    public int length() {
        return this.end - this.start;
    }

    @Override
    public char charAt(int index) {
        return this.source.charAt(this.start + index);
    }

    int indexOf(char ch) {
        return this.source.indexOf(ch, this.start) - this.start;
    }

    int indexOf(String str) {
        return this.source.indexOf(str, this.start) - this.start;
    }

    int indexOf(String str, int fromIndex) {
        return this.source.indexOf(str, this.start + fromIndex) - this.start;
    }

    boolean startsWith(CharSequence prefix) {
        return startsWith(prefix, 0);
    }

    boolean startsWith(CharSequence prefix, int offset) {
        int prefixLength = prefix.length();
        if (length() - prefixLength - offset < 0) {
            return false;
        }
        int prefixOffset = 0;
        int sourceOffset = offset;
        while (prefixLength-- != 0) {
            if (charAt(sourceOffset++) != prefix.charAt(prefixOffset++)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof CharSequence)) {
            return false;
        }
        CharSequence other = (CharSequence) obj;
        int n = length();
        if (n != other.length()) {
            return false;
        }
        int i = 0;
        while (n-- != 0) {
            if (charAt(i) != other.charAt(i)) {
                return false;
            }
            i++;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int h = this.hash;
        if (h == 0 && length() > 0) {
            for (int i = this.start; i < this.end; i++) {
                h = 31 * h + this.source.charAt(i);
            }
            this.hash = h;
        }
        return h;
    }

    @Override
    public String toString() {
        return this.source.substring(this.start, this.end);
    }

}
