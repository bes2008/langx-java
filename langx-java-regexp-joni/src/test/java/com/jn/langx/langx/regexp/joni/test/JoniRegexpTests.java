package com.jn.langx.langx.regexp.joni.test;

import com.jn.langx.regexp.joni.JoniRegexp;
import com.jn.langx.text.StringTemplates;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.function.Consumer;
import com.jn.langx.util.io.Charsets;
import com.jn.langx.util.regexp.Regexp;
import com.jn.langx.util.regexp.RegexpMatcher;
import com.jn.langx.util.regexp.Regexps;
import com.jn.langx.util.regexp.jdk.JdkRegexp;
import com.jn.langx.util.regexp.named.NamedRegexp;
import org.jcodings.specific.UTF8Encoding;
import org.joni.*;
import org.junit.Test;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

    private String[] pythonVersions = new String[]{
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
        String str = "a134b2c3d4e5f6g你好，23,b233fs";
        String pattern = "(?:[a-z]\\d{1,})*";
        Regexp regexp = new JoniRegexp(pattern);
        showMatched34(regexp, str);
    }

    @Test
    public void test4() {
        String str = "a134b2c3d4e5f6g你好，23,b233fs";
        String pattern = "(?:[a-z]\\d{1,})*";
        Regexp regexp = new JdkRegexp(pattern);
        showMatched34(regexp, str);
    }


    private void showMatched34(Regexp regexp, String str) {
        RegexpMatcher matcher = regexp.matcher(str);
        System.out.println("before matches(): groupCount(): " + matcher.groupCount());
        System.out.println("matches(): " + matcher.matches());
        System.out.println("after matches(): groupCount(): " + matcher.groupCount());
        System.out.println("while find: ");
        while (matcher.find()) {
            final String matched = matcher.group();
            int start = matcher.start();
            int end = matcher.end();
            System.out.println("[" + start + "," + end + "]: " + matched);
        }
        matcher.reset();
        System.out.println("after reset(): groupCount(): " + matcher.groupCount());
        System.out.println("while find: ");
        while (matcher.find()) {
            final String matched = matcher.group();
            int start = matcher.start();
            int end = matcher.end();
            System.out.println("[" + start + "," + end + "]: " + matched);
        }
    }


    private void groupNameTest(Regexp regexp, String text) {
        final RegexpMatcher matcher = regexp.matcher(text);
        if (matcher.matches()) {
            final Map<String, Object> map = new HashMap<String, Object>();
            Pipeline.of("epoch", "release", "pre", "preLabel", "preN", "post", "postLabel", "postN", "dev", "devN", "local")
                    .forEach(new Consumer<String>() {
                        @Override
                        public void accept(String groupName) {
                            map.put(groupName, matcher.group(groupName));
                        }
                    });
            System.out.println(map);
        }

    }

    @Test
    public void test5() {
        String str = "a134b2c3d4e5f6g";
        String pattern = "(?:[a-z]\\d{1,})*";
        Regexp regexp = new JoniRegexp(pattern);
        showMatched(regexp, str);
    }

    @Test
    public void test6() {
        String str = "a134b2c3d4e5f6g";
        String pattern = "(?:[a-z]\\d{1,})*";
        Regexp regexp = new JdkRegexp(pattern);
        showMatched(regexp, str);
    }

    private void showMatched(Regexp regexp, String str) {
        RegexpMatcher matcher = regexp.matcher(str);
        System.out.println("before matches(): groupCount(): " + matcher.groupCount());
        System.out.println("matches(): " + matcher.matches());
        System.out.println("after matches(): groupCount(): " + matcher.groupCount());
        System.out.println("while find: ");
        while (matcher.find()) {
            final String matched = matcher.group();
            int start = matcher.start();
            int end = matcher.end();
            System.out.println("[" + start + "," + end + "]: " + matched);
        }
        matcher.reset();
        System.out.println("after reset(): groupCount(): " + matcher.groupCount());
        System.out.println("while find: ");
        while (matcher.find()) {
            final String matched = matcher.group();
            int start = matcher.start();
            int end = matcher.end();
            System.out.println("[" + start + "," + end + "]: " + matched);
        }
    }

    @Test
    public void test1() {
        final Regexp regexp = new JdkRegexp(VERSION_PATTERN_STR);
        groupNameTest(regexp, pythonVersions[8]);
        final Regexp regexp2 = new JoniRegexp(VERSION_PATTERN_STR);
        groupNameTest(regexp2, pythonVersions[8]);
    }

    @Test
    public void test7() {
        final Regexp regexp = new JdkRegexp(VERSION_PATTERN_STR);
        Collects.forEach(pythonVersions, new Consumer<String>() {
            @Override
            public void accept(String s) {
                groupNameTest(regexp, s);
            }
        });
    }


    @Test
    public void test8() {
        final Regexp regexp = new JdkRegexp(VERSION_PATTERN_STR);
        Collects.forEach(pythonVersions, new Consumer<String>() {
            @Override
            public void accept(String s) {
                groupNameTest(regexp, s);
            }
        });
    }

    @Test
    public void test9() {
        System.out.println(StringTemplates.formatWithPlaceholder("a {}, b: {}, e: {}", 123, 234, 545));
    }

    @Test
    public void test10() {
        String pattern = "(?:(?<year>\\d{4})\\-(?<month>\\d{2})-(?<dayOfMonth>\\d{2}))";
        List<String> names;
        names = new NamedRegexp(pattern).getNamedGroups();
        System.out.println(names);
        names = new JdkRegexp(pattern).getNamedGroups();
        System.out.println(names);
        names = new JoniRegexp(pattern).getNamedGroups();
        System.out.println(names);
    }

    @Test
    public void test11() {
        String str = "a134b2c3d4e5f6g";
        String pattern = "(?<name>[a-z])(?<number>\\d{1,})";

        System.out.println("------------joni-------------");
        Regexp regexp = new JoniRegexp(pattern);
        RegexpMatcher matcher = regexp.matcher(str);
        List<Map<String, String>> namedGroups = matcher.namedGroups();
        System.out.println(namedGroups);

        System.out.println("------------jdk6-------------");

        regexp = new NamedRegexp(pattern);
        matcher = regexp.matcher(str);
        namedGroups = matcher.namedGroups();
        System.out.println(namedGroups);

        System.out.println("------------jdk8-------------");
        regexp = new JoniRegexp(pattern);
        matcher = regexp.matcher(str);
        namedGroups = matcher.namedGroups();
        System.out.println(namedGroups);
    }

    @Test
    public void test12() {
        String regexp = "(?<ip>[^/]*)(/(?<prefixLength>\\d{1,6})(:(?<port>\\d{1,5}))?)?";
        Regexp IP_SEGMENT_PATTERNS = Regexps.createRegexpWithEngine("joni", regexp);
        String str = "::/12";
        Map<String, String> stringMap = Regexps.findNamedGroup(IP_SEGMENT_PATTERNS, str);
        System.out.println(stringMap);

        IP_SEGMENT_PATTERNS = Regexps.createRegexpWithEngine("jdk", regexp);
        stringMap = Regexps.findNamedGroup(IP_SEGMENT_PATTERNS, str);
        System.out.println(stringMap);

    }

}
