package com.jn.langx.util.os.hardware.cpu;

import com.jn.langx.commandline.CommandLine;
import com.jn.langx.commandline.DaemonCommandLineExecutor;
import com.jn.langx.commandline.DefaultCommandLineExecutor;
import com.jn.langx.util.os.OS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CPUs {
    private static final Logger logger = LoggerFactory.getLogger(CPUs.class);
    private static String cpuId;

    private CPUs() {
    }

    public static final String getCpuId() {
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
            try {
                executor.execute(commandLine);
            } catch (Throwable ex) {
                logger.error(ex.getMessage(), ex);
            }
            cpuId = handler.getCpuId();

            return cpuId;
        } else {
            return "";
        }
    }
}
