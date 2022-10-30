package com.jn.langx.test.text.pinyin;

import com.jn.langx.text.pinyin.OutputStyle;
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
                , "一二三四五六七⑦⑥⑤④③②① 器起其期",
                "把你们用的开发公共环境机器列表发我一下我申请明天不关机用"
        );
        final OutputStyle style = new OutputStyle();
        style.setSegmentSeparator(" | ");
        Pipeline.of(strs).forEach(new Consumer<String>() {
            @Override
            public void accept(String s) {
                System.out.println(Pinyins.getPinyin(s, style));
            }
        });
    }

    @Test
    public void personNameTest() {
        String name = "查良庸,冒顿,盖楼,南宫婉，司徒南、司马南,仓颉,李颉, 颉冉；";
        final OutputStyle style = new OutputStyle();
        style.setSegmentSeparator("\n");
        System.out.println(Pinyins.getPersonName(name, style));
    }

}
