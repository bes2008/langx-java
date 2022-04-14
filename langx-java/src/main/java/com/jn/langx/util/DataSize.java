package com.jn.langx.util;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.text.StringTemplates;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Predicate;
import com.jn.langx.util.regexp.Option;
import com.jn.langx.util.regexp.Regexp;
import com.jn.langx.util.regexp.Regexps;

import static com.jn.langx.util.DataUnit.*;


public final class DataSize {
    public static final DataSize ZERO = new DataSize(0);


    private final long value;
    private final DataUnit unit;


    public DataSize(long value, DataUnit unit) {
        this.value = value;
        this.unit = unit;
    }

    public DataSize(long bytes) {
        this.value = bytes;
        this.unit = B;
    }

    public int toInt() {
        long bytes = getBytes();
        if (bytes > Integer.MAX_VALUE) {
            throw new IllegalStateException("MemSize exceeds Integer.MAX_VALUE: " + bytes);
        }
        return (int) bytes;
    }

    public long getBytes() {
        return value * unit.getBytes();
    }

    @NonNull
    public static DataSize of(long bytes) {
        return new DataSize(bytes);
    }

    public static DataSize of(long value, DataUnit unit) {
        return new DataSize(value, unit);
    }

    @NonNull
    public static DataSize bytes(long bytes) {
        return DataSize.of(bytes);
    }

    @NonNull
    public static DataSize kb(long kb) {
        return of(kb, KB);
    }

    @NonNull
    public static DataSize mb(long mb) {
        return of(mb, MB);
    }

    @NonNull
    public static DataSize gb(long gb) {
        return of(gb, GB);
    }

    @NonNull
    public static DataSize tb(long tb) {
        return of(tb, TB);
    }

    public static DataSize pb(long pb) {
        return of(pb, PB);
    }

    @NonNull
    public static DataSize valueOf(@NonNull String string) {
        return parseMemSize(string);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DataSize memSize = (DataSize) o;

        return getBytes() == memSize.getBytes();
    }

    @Override
    public int hashCode() {
        return unit.hashCode() << 2 + value;
    }

    @NonNull
    @Override
    public String toString() {
        return "" + getBytes();
    }


    private static final Regexp MEM_SIZE_PATTERN = Regexps.createRegexp("\\s*\\d+(\\.\\d+)?\\s*[gmktp]?b\\s*", Option.CASE_INSENSITIVE);


    public static DataSize parseMemSize(String memSizeString) {
        Preconditions.checkNotNull(memSizeString);
        if (!MEM_SIZE_PATTERN.matcher(memSizeString).matches()) {
            throw new IllegalArgumentException(StringTemplates.formatWithPlaceholder("{} is not an illegal data size", memSizeString));
        }
        final String lowerCase = memSizeString.trim().toLowerCase();
        String unit = Collects.findFirst(DataUnit.allSymbols(), new Predicate<String>() {
            @Override
            public boolean test(String unit) {
                return lowerCase.endsWith(unit);
            }
        });
        memSizeString = lowerCase.substring(0, lowerCase.length() - (Strings.isEmpty(unit) ? 0 : unit.length())).trim();
        if (Strings.isEmpty(unit)) {
            unit = "b";
        }

        String[] segments = Strings.split(memSizeString, ".");
        DataUnit dataunit = DataUnit.ofSymbol(unit);
        if (segments.length == 1 || dataunit == B) {
            return of(dataunit.getBytes() * Long.parseLong(segments[0]));
        } else {
            DataUnit nextLevelUnit = DataUnit.ofBytes(dataunit.getBytes() / 1024);
            return of(dataunit.getBytes() * Long.parseLong(segments[0]) + nextLevelUnit.getBytes() * Long.parseLong(segments[1]));
        }
    }
}
