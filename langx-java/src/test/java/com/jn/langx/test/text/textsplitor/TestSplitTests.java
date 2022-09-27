package com.jn.langx.test.text.textsplitor;

import com.jn.langx.io.resource.Resources;
import com.jn.langx.text.textsplitor.MultilineConfig;
import com.jn.langx.text.textsplitor.TextLinesFragment;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Consumer;
import org.junit.Test;

import java.io.IOException;

public class TestSplitTests {

    @Test
    public void test() throws IOException {
        splitFile("/textsplitor/negate_false_match_after", new MultilineConfig("^b", false, MultilineConfig.Match.AFTER, ""));
        splitFile("/textsplitor/negate_false_match_before", new MultilineConfig("^b", false, MultilineConfig.Match.BEFORE, ""));
        splitFile("/textsplitor/negate_true_match_after", new MultilineConfig("^b", true, MultilineConfig.Match.AFTER, ""));
        splitFile("/textsplitor/negate_true_match_before", new MultilineConfig("^b", true, MultilineConfig.Match.BEFORE, ""));
    }

    static void splitFile(String resourcePath, MultilineConfig multilineConfig) throws IOException {
        System.out.println("==================================");
        TextLinesFragment splitor = new TextLinesFragment(Resources.loadClassPathResource(resourcePath));
        splitor.setMultiline(multilineConfig);
        Collects.forEach(splitor, new Consumer<String>() {

            @Override
            public void accept(String s) {
                System.out.println(s);
            }
        });
    }

}
