package com.jn.langx.test.io.resource;

import com.jn.langx.io.resource.FileResource;
import com.jn.langx.io.resource.Resources;
import com.jn.langx.util.SystemPropertys;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Consumer;
import com.jn.langx.util.io.Charsets;
import com.jn.langx.util.reflect.Reflects;
import org.junit.Test;

public class DelimiterResourceTests {
    public FileResource getCurrentFileResource() {
        String workDir = SystemPropertys.getUserWorkDir();
        System.out.println(workDir);
        String currentFilePath = workDir + "/src/main/java/" + Reflects.getFQNClassName(Collects.class).replaceAll("\\.", "/") + ".java";
        return Resources.loadFileResource(currentFilePath);
    }

    @Test
    public void test() {
        FileResource file = getCurrentFileResource();
        Resources.readUsingDelimiter(file, "\r", Charsets.getDefault(), new Consumer<String>() {
            @Override
            public void accept(String s) {
                System.out.println(s);
            }
        });
    }

}
