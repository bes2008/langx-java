package com.jn.langx.test.text.textsplitor;

import com.jn.langx.io.resource.Resources;
import com.jn.langx.text.fragment.MultilineConfig;
import com.jn.langx.text.fragment.TextLinesFragment;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Consumer;
import org.junit.Test;

import java.io.IOException;

public class TestFragmentTests {

    @Test
    public void test() throws IOException {
        fragmentFile("/textsplitor/negate_false_match_after", new MultilineConfig("^b", false, MultilineConfig.Match.AFTER, ""));
        fragmentFile("/textsplitor/negate_false_match_before", new MultilineConfig("^b", false, MultilineConfig.Match.BEFORE, ""));
        fragmentFile("/textsplitor/negate_true_match_after", new MultilineConfig("^b", true, MultilineConfig.Match.AFTER, ""));
        fragmentFile("/textsplitor/negate_true_match_before", new MultilineConfig("^b", true, MultilineConfig.Match.BEFORE, ""));
    }

    static void fragmentFile(String resourcePath, MultilineConfig multilineConfig) throws IOException {
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
