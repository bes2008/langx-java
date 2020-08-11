package com.jn.langx.test.text.ini;

import com.jn.langx.io.resource.Resource;
import com.jn.langx.io.resource.Resources;
import com.jn.langx.text.ini.Ini;
import com.jn.langx.text.ini.IniPlaceholderHandler;
import com.jn.langx.text.ini.IniPlaceholderParser;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Consumer;
import com.jn.langx.util.function.Consumer2;
import com.jn.langx.util.io.IOs;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

public class IniTests {
    private static final Logger logger = LoggerFactory.getLogger(IniTests.class);


    public void test() {
        Ini ini = buildFromClasspath("classpath:/ini/a.ini");
        System.out.println(ini);
    }

    @Test
    public void testVariableParse() {
        final Ini ini = buildFromClasspath("classpath:/ini/identify.ini");
        System.out.println(ini);
        final IniPlaceholderHandler iniPlaceholderHandler = new IniPlaceholderHandler("${", "}");
        final IniPlaceholderParser placeholderParser = new IniPlaceholderParser(ini);
        Collects.forEach(ini.getSectionNames(), new Consumer<String>() {
            @Override
            public void accept(final String sectionName) {
                final Ini.Section section = ini.getSection(sectionName);
                Collects.forEach(section, new Consumer2<String, String>() {
                    @Override
                    public void accept(String key, String value) {
                        String resolvedValue = iniPlaceholderHandler.replacePlaceholders(value, sectionName, placeholderParser);
                        System.out.println(key + ".VALUE=" + value);
                        System.out.println(key + ".ResolvedVALUE=" + resolvedValue);
                    }
                });
            }
        });

    }

    private Ini buildFromClasspath(String filepath) {
        Ini ini = new Ini();
        InputStream inputStream = null;
        try {
            Resource resource = Resources.loadClassPathResource(filepath);
            inputStream = resource.getInputStream();
            ini.load(inputStream);
        } catch (IOException ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
            IOs.close(inputStream);
        }
        return ini;
    }
}
