package com.jn.langx.text;

import com.jn.langx.Accessor;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.collection.MapAccessor;
import com.jn.langx.util.function.Function2;

import java.util.Map;
import java.util.regex.Pattern;

public class MapBasedStringFormatter extends CustomPatternStringFormatter {
    static final Pattern MAP_VARIABLE_PATTERN = Pattern.compile("\\$\\{\\w+\\}");

    public MapBasedStringFormatter() {
        super(MAP_VARIABLE_PATTERN, new MapValueGetter());
    }


    private static class MapValueGetter implements Function2<String, Object[], String> {
        @Override
        public String apply(String matched, Object[] args) {
            Preconditions.checkNotNull(args);
            matched = matched.substring(2, matched.length() - 1);
            Accessor<String, ?> accessor = new MapAccessor((Map) args[0]);
            return accessor.getString(matched);
        }
    }
}
