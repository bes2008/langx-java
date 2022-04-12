package com.jn.langx.langx.regexp.joni.test;

import com.jn.langx.regexp.joni.JoniRegexp;
import com.jn.langx.util.Objs;
import com.jn.langx.util.ThrowableFunction;
import com.jn.langx.util.Throwables;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Consumer;
import com.jn.langx.util.io.Charsets;
import com.jn.langx.util.regexp.Regexp;
import com.jn.langx.util.regexp.RegexpMatcher;
import com.jn.langx.util.regexp.jdk.JdkRegexp;
import org.jcodings.specific.UTF8Encoding;
import org.joni.*;
import org.junit.Test;

import java.util.Iterator;

import static java.util.regex.Matcher.quoteReplacement;

public class JoniRegexpTests {

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

    @Test
    public void test1() {
        String[] strings = new String[]{
                "1.dev0",
                "1.0.dev456",
                "1.0a1",
                "1.0a2.dev456",
                "1.0a12.dev456",
                "1.0a12",
                "1.0b1.dev456",
                "1.0b2",
                "1.0b2.post345.dev456",
                "1.0b2.post345",
                "1.0rc1.dev456",
                "1.0rc1",
                "1.0",
                "1.0+abc.5",
                "1.0+abc.7",
                "1.0+5",
                "1.0.post456.dev34",
                "1.0.post456",
                "1.0.15",
                "1.1.dev1"
        };

        Collects.forEach(strings, new Consumer<String>() {
            @Override
            public void accept(String s) {


            }
        });
    }

    @Test
    public void test2() {
        String str = "a1b2c3d4e5f6g";
        String pattern = "\\d";

        byte[] strBytes = str.getBytes(Charsets.UTF_8);
        byte[] patternBytes = pattern.getBytes(Charsets.UTF_8);

        Regex regex = new Regex(patternBytes, 0, patternBytes.length, Option.NONE, UTF8Encoding.INSTANCE);
        Matcher matcher = regex.matcher(strBytes);
        int result = matcher.search(0, strBytes.length, Option.DEFAULT);
        if (result != -1) {
            Region region = matcher.getEagerRegion();
            if (region.numRegs > 0) {
                for (int i = 0; i < region.numRegs; i++) {
                    int regionBegin = region.beg[i];
                    int regionEnd = region.end[i];
                    System.out.println(str.substring(regionBegin, regionEnd));
                }
            }
            for (Iterator<NameEntry> entry = regex.namedBackrefIterator(); entry.hasNext(); ) {
                NameEntry e = entry.next();
                int number = e.getBackRefs()[0]; // can have many refs per name
                int begin = region.beg[number];
                int end = region.end[number];
            }
        }

        System.out.println();
    }

    @Test
    public void test3() {
        String str = "a1b2c3d4e5f6g";
        String pattern = "\\d";
        Regexp regexp = new JdkRegexp(pattern);
        showMatched(regexp, str);
        regexp = new JoniRegexp(pattern);
        showMatched(regexp, str);
    }

    private void showMatched(Regexp regexp, String str) {
        RegexpMatcher matcher = regexp.matcher(str);
        while (matcher.find()) {
            final String matched = matcher.group();
            System.out.println(matched);
        }
    }
}
