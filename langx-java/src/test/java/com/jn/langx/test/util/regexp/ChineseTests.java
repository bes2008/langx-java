package com.jn.langx.test.util.regexp;

import com.jn.langx.util.regexp.RegexpPatterns;
import org.junit.Test;

public class ChineseTests {
    @Test
    public void test(){
        System.out.println(isChinese("你"));
        System.out.println(isChinese("︱"));
        System.out.println(isChinese("\uD840\uDC04"));
    }

    private boolean isChinese(String st){
        return st.matches(RegexpPatterns.CHINESE_CHAR_PATTERN);
    }
}
