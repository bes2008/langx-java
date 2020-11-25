package com.jn.langx.util.os;

import com.jn.langx.util.os.hardware.cpu.CPUs;

public class Infrastructures {
    private Infrastructures() {
    }

    public static String getCpuId() {
        return CPUs.getCpuId();
    }
}
