package com.jn.langx.test.commandline;

import com.jn.langx.test.util.os.hardware.cpu.CPUs;
import org.junit.Test;

public class HardwaresTests {

    @Test
    public void testGetCpuId() throws Throwable{
        String id = CPUs.getCpuId();
        System.out.println(id);
    }

}
