package com.jn.langx.test.commandline;

import com.jn.langx.util.os.hardware.Hardwares;
import org.junit.Test;

public class HardwaresTests {

    @Test
    public void testGetCpuId() throws Throwable{
        String id = Hardwares.getCpuId();
        System.out.println(id);
    }

}
