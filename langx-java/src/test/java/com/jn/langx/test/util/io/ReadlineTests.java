package com.jn.langx.test.util.io;

import com.jn.langx.io.resource.ClassPathResource;
import com.jn.langx.io.resource.Resources;
import com.jn.langx.util.function.Consumer;
import com.jn.langx.util.io.Channels;
import com.jn.langx.util.io.IOs;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

public class ReadlineTests {
    @Test
    public void test() throws IOException {
        ClassPathResource resource = Resources.loadClassPathResource("/io/license-banner.txt", ReadlineTests.class);
        InputStream inputStream = resource.getInputStream();
        Channels.readLines(inputStream, new Consumer<String>() {
            @Override
            public void accept(String s) {
                System.out.println(s);
            }
        });
        IOs.close(inputStream);
    }
}
