package com.jn.langx.util.os.hardware.cpuid;

import com.jn.langx.util.Emptys;
import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Predicate;
import com.jn.langx.util.io.IOs;
import com.jn.langx.util.struct.Holder;

import java.io.IOException;
import java.util.List;

public class LinuxGetCpuIdStreamHandler extends GetCpuIdStreamHandler {
    private final Holder<String> result = new Holder<String>();

    @Override
    public void start() throws IOException {
        result.reset();
        if (this.subProcessOutputStream != null) {
            List<String> lines = IOs.readLines(this.subProcessOutputStream);
            String cpuId = Collects.findFirst(lines, new Predicate<String>() {
                @Override
                public boolean test(String line) {
                    return Strings.isNotBlank(line) && Strings.indexOf(line, "ProcessorId", true) < 0;
                }
            });
            if (Emptys.isNotEmpty(cpuId)) {
                result.set(cpuId.trim());
            }
        }
    }

    @Override
    public String getCpuId() {
        return null;
    }
}
