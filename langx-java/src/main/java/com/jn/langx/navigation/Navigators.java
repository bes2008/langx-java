package com.jn.langx.navigation;

import com.jn.langx.util.Objs;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.Collects;

import java.util.List;

public class Navigators {

    public static String getParentPath(String pathExpression, String separator) {
        Preconditions.checkNotNull(separator);
        String[] segments = Strings.split(pathExpression, separator);
        if (Objs.length(segments)<=1) {
            return null;
        }
        List<String> parentPath = Collects.asList(segments).subList(0, segments.length - 1);
        return Strings.join(separator, parentPath);
    }

    public static String getLeaf(String pathExpression, String separator) {
        Preconditions.checkNotNull(separator);
        String[] segments = Strings.split(pathExpression, separator);
        if (Objs.length(segments)<1) {
            return null;
        }
        String leaf = segments[segments.length-1];
        return leaf;
    }


}
