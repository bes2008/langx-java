package com.jn.langx.java8.tests.util.regexp;

import com.jn.langx.util.collection.MapAccessor;
import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.function.Consumer;
import com.jn.langx.util.regexp.Regexp;
import com.jn.langx.util.regexp.RegexpMatcher;
import com.jn.langx.util.regexp.Regexps;
import com.jn.langx.util.regexp.named.NamedRegexp;

import java.util.HashMap;
import java.util.Map;

public class PythonVersionSpecifiersJava8 {

    // https://www.python.org/dev/peps/pep-0440/#version-scheme
    // https://www.pythonheidong.com/blog/article/187997/31fe90bd992afcd027a1/
    private static final String PUBLIC_VERSION_SEG_EPOCH = "(?:(?<epoch>\\d+)!)?";
    private static final String PUBLIC_VERSION_SEG_RELEASE = "(?<release>\\d+(\\.\\d+)*)";
    private static final String PUBLIC_VERSION_SEG_PRE = "(?<pre>[-_.]?(?<preLabel>(a|alpha|b|beta|rc|c|pre|preview))([-_.]?(?<preN>\\d+))?)?";
    private static final String PUBLIC_VERSION_SEG_POST = "(?<post>[-_.]?(?<postLabel>(post|rev|r))([-_.]?(?<postN>\\d+))?)?";
    private static final String PUBLIC_VERSION_SEG_DEV = "(?<dev>[-_.]?dev([-_.]?(?<devN>\\d+))?)?";
    private static final String LOCAL_VERSION_SEG = "(?:\\+(?<local>[a-zA-Z0-9]+([-_.][a-zA-Z0-9]+)*))?";
    public static final String VERSION_PATTERN_STR = PUBLIC_VERSION_SEG_EPOCH
            + PUBLIC_VERSION_SEG_RELEASE
            + PUBLIC_VERSION_SEG_PRE
            + PUBLIC_VERSION_SEG_POST
            + PUBLIC_VERSION_SEG_DEV
            + LOCAL_VERSION_SEG;
    public static final Regexp VERSION_PATTERN = Regexps.compile(VERSION_PATTERN_STR);
    public static final NamedRegexp VERSION_PATTERN_NAMED = NamedRegexp.compile(VERSION_PATTERN_STR);


    public static final MapAccessor extractVersionSegments(String version) {
        final RegexpMatcher matcher = VERSION_PATTERN.matcher(version);
        if (matcher.matches()) {
            final Map<String, Object> map = new HashMap<>();
            Pipeline.of("epoch", "release", "pre", "preLabel", "preN", "post", "postLabel", "postN", "dev", "devN", "local")
                    .forEach(new Consumer<String>() {
                        @Override
                        public void accept(String groupName) {
                            map.put(groupName, matcher.group(groupName));
                        }
                    });
            return new MapAccessor(map);
        }
        return null;
    }
}
