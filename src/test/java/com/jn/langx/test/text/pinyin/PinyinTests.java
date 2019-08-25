package com.jn.langx.test.text.pinyin;

import com.jn.langx.text.pinyin.Pinyin;
import org.junit.Test;

public class PinyinTests {
    @Test
    public void test() {
        String a = "爱家违反了宿a舍的丰盛饭店撒b发生了按理说d发发了俺发阿拉丁发";
        System.out.println(Pinyin.toPinyin(a));
    }
}
