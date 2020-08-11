package com.jn.langx.test.text.ini;

import com.jn.langx.io.resource.Resource;
import com.jn.langx.io.resource.Resources;
import com.jn.langx.text.ini.Ini;
import com.jn.langx.util.io.IOs;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

public class IniTests {
    private static final Logger logger = LoggerFactory.getLogger(IniTests.class);

    @Test
    public void test() {
        Ini ini = new Ini();
        InputStream inputStream = null;
        try {
            Resource resource = Resources.loadClassPathResource("classpath:/ini/a.ini");
            inputStream = resource.getInputStream();
            ini.load(inputStream);
        } catch (IOException ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
            IOs.close(inputStream);
        }

        System.out.println(ini);
    }
}
