package com.jn.langx.util.os;

import com.jn.langx.util.os.hardware.cpu.Cpus;

public class Infrastructures {
    private Infrastructures() {
    }

    public static String getCpuId() {
        return Cpus.getCpuId();
    }
}
