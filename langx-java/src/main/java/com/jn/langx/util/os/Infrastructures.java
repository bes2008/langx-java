package com.jn.langx.util.os;

import com.jn.langx.commandline.CommandLine;
import com.jn.langx.commandline.DaemonCommandLineExecutor;
import com.jn.langx.commandline.DefaultCommandLineExecutor;
import com.jn.langx.util.os.hardware.cpuid.GetCpuIdStreamHandler;
import com.jn.langx.util.os.hardware.cpuid.LinuxGetCpuIdStreamHandler;
import com.jn.langx.util.os.hardware.cpuid.WindowsGetCpuIdStreamHandler;

public class Infrastructures {
    private static String cpuId;

    public static final String getCpuId() throws Throwable {
        if (cpuId == null) {

            DefaultCommandLineExecutor executor = new DaemonCommandLineExecutor();
            CommandLine commandLine = null;
            GetCpuIdStreamHandler handler = null;
            if (OS.isFamilyWindows()) {
                commandLine = CommandLine.parse("wmic cpu get ProcessorId");
                handler = new WindowsGetCpuIdStreamHandler();
            } else if (OS.isFamilyUnix()) {
                commandLine = CommandLine.parse("sudo dmidecode -t 4 | grep ID");
                handler = new LinuxGetCpuIdStreamHandler();
            }
            executor.setStreamHandler(handler);
            executor.execute(commandLine);
            cpuId = handler.getCpuId();

            return cpuId;
        } else {
            return "";
        }
    }
}
