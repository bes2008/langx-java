package com.jn.langx.test.commandline;

import com.jn.langx.util.os.hardware.cpu.Cpus;
import org.junit.Test;

public class HardwaresTests {

    @Test
    public void testGetCpuId() throws Throwable{
        String id = Cpus.getCpuId();
        System.out.println(id);
    }

}
