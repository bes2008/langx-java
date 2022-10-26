package com.jn.langx.classpath;

import com.jn.langx.Matcher;
import com.jn.langx.util.Objs;
import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.function.Function;
import com.jn.langx.util.pattern.patternset.AntPathMatcher;

import java.util.List;

public class ClasspathMatcher implements Matcher<String, Boolean> {
    private AntPathMatcher matcher;

    /**
     * @param classpath 可以是报名，类名
     * @return 是否匹配
     */
    @Override
    public Boolean matches(String classpath) {
        if (Strings.endsWith(classpath, ".class")) {
            classpath = Strings.substring(classpath, 0, classpath.length() - ".class".length());
        }
        classpath = Strings.replace(classpath, ".", "/");
        return matcher.matches(classpath);
    }

    public ClasspathMatcher(List<String> classPaths) {
        this(buildAntPathMatcher(classPaths));
    }

    public ClasspathMatcher(AntPathMatcher matcher) {
        this.matcher = matcher;
    }

    public ClasspathMatcher() {
    }

    public void setMatcher(AntPathMatcher matcher) {
        this.matcher = matcher;
    }

    public static AntPathMatcher buildAntPathMatcher(List<String> classPaths) {
        if (Objs.isEmpty(classPaths)) {
            return null;
        }
        String expression = Strings.join(";", Pipeline.of(classPaths)
                .map(new Function<String, String>() {
                    @Override
                    public String apply(String classpath) {
                        classpath = Strings.replace(classpath, ".", "/");
                        if (Strings.startsWith(classpath, "~")) {
                            classpath = "!" + classpath.substring(1);
                        }
                        return classpath;
                    }
                })
                .asList()
        );

        AntPathMatcher antPathMatcher = new AntPathMatcher();
        antPathMatcher.setPatternExpression(expression);
        antPathMatcher.setGlobal(true);
        return antPathMatcher;
    }


}
