package com.jn.langx.text.formatter;

import com.jn.langx.Accessor;
import com.jn.langx.annotation.Nullable;
import com.jn.langx.text.formatter.CustomPatternStringFormatter;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.collection.MapAccessor;
import com.jn.langx.util.function.Function2;

import java.util.Map;
import java.util.regex.Pattern;

public class MapBasedStringFormatter extends CustomPatternStringFormatter {
    public static enum PatternStyle {
        $(Pattern.compile("\\$\\{\\w+(\\.\\w+)*}")),
        PLACE_HOLDER(Pattern.compile("\\{\\w+(\\.\\w+)*}"));

        private Pattern pattern;

        private PatternStyle(Pattern pattern) {
            this.pattern = pattern;
        }

        public Pattern getPattern() {
            return pattern;
        }
    }

    public MapBasedStringFormatter() {
        this(PatternStyle.$);
    }

    public MapBasedStringFormatter(@Nullable PatternStyle patternStyle) {
        super((patternStyle == null ? PatternStyle.$ : patternStyle).getPattern(), new MapValueGetter(patternStyle));
    }


    private static class MapValueGetter implements Function2<String, Object[], String> {
        private PatternStyle style;

        MapValueGetter(PatternStyle style) {
            this.style = style;
        }

        @Override
        @SuppressWarnings("unchecked")
        public String apply(String matched, Object[] args) {
            Preconditions.checkNotNull(args);
            matched = style == PatternStyle.$ ? matched.substring(2, matched.length() - 1) : matched.substring(1, matched.length() - 1);
            Accessor<String, ?> accessor = new MapAccessor((Map) args[0]);
            return accessor.getString(matched);
        }
    }
}
