package com.jn.langx.test.text.ini;

import com.jn.langx.io.resource.Resources;
import com.jn.langx.text.ini.Ini;
import org.junit.Test;

public class IniTests {
    @Test
    public void test() {
        Ini ini = new Ini();
        ini.load(Resources.loadClassPathResource("classpath:/ini/a.ini"));
        System.out.println(ini);
    }
}
