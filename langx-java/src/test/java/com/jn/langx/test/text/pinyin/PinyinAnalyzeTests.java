package com.jn.langx.test.text.pinyin;

import com.jn.langx.text.pinyin.Pinyins;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.function.Consumer;
import org.junit.Test;

import java.util.List;

public class PinyinAnalyzeTests {
    @Test
    public void test() {
        List<String> strs = Collects.newArrayList(
                "你好呀，混蛋！！！"
                , "hello, 你好呀，he人山人海xx00,仇千丈，world"
                , "一二三四五六七⑦⑥⑤④③②① 器起其期"
        );
        Pipeline.of(strs).forEach(new Consumer<String>() {
            @Override
            public void accept(String s) {
                System.out.println(Pinyins.getPinyin(null, s, 5));
            }
        });
    }
}