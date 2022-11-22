package com.jn.langx.navigation;

import com.jn.langx.util.Objs;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.function.Function;

import java.util.List;

public class Navigators {

    public static String getParentPath(String pathExpression, String separator) {
        return getParentPath(pathExpression, null, separator);
    }


    public static String getParentPath(String pathExpression, final String prefix, final String suffix) {
        Preconditions.checkNotNull(suffix);
        String[] segments = getPathSegments(pathExpression, prefix, suffix);
        if (Objs.length(segments) <= 1) {
            return null;
        }
        List<String> parentPath = Collects.asList(segments).subList(0, segments.length - 1);
        parentPath = Pipeline.of(parentPath).map(new Function<String, String>() {
            @Override
            public String apply(String input) {
                return Strings.useValueIfNull(prefix, "") + input + Strings.useValueIfNull(suffix, "");
            }
        }).asList();
        return Strings.join("", parentPath);
    }

    public static String getLeaf(String pathExpression, final String separator) {
        return getLeaf(pathExpression, null, separator);
    }

    public static String getLeaf(String pathExpression, final String prefix, String suffix) {
        Preconditions.checkNotNull(suffix);
        String[] segments = getPathSegments(pathExpression, prefix, suffix);
        if (Objs.length(segments) < 1) {
            return null;
        }
        String leaf = segments[segments.length - 1];
        return leaf;
    }

    public static String[] getPathSegments(String expression, String separator) {
        return getPathSegments(expression, null, separator);
    }

    public static String[] getPathSegments(String expression, final String prefix, String suffix) {
        String[] segments = Strings.split(expression, suffix);
        if (Strings.isNotEmpty(prefix)) {
            segments = Pipeline.of(segments)
                    .map(new Function<String, String>() {
                        @Override
                        public String apply(String input) {
                            return Strings.substring(input, prefix.length());
                        }
                    }).toArray(String[].class);
        }
        return segments;
    }


}
