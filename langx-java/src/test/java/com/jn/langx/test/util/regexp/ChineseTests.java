package com.jn.langx.test.util.regexp;

import com.jn.langx.security.crypto.digest.MessageDigests;
import com.jn.langx.util.regexp.RegexpPatterns;
import com.jn.langx.util.regexp.Regexps;
import org.junit.Test;

public class ChineseTests {
    @Test
    public void test() {
        System.out.println(isChinese("你"));
        System.out.println(isChinese("︱"));
        System.out.println(isChinese("\uD840\uDC04"));
    }

    private boolean isChinese(String st) {
        return st.matches(RegexpPatterns.CHINESE_CHAR_PATTERN);
    }

    public static String chineseToMD5String(String str) {
        if (Regexps.contains(str, RegexpPatterns.CHINESE_CHAR)) {
            str = MessageDigests.md5(str);
        }
        return str;
    }

    @Test
    public void test1(){
        String s = "123421中你3人23";
        System.out.println(chineseToMD5String(s));
    }
}
