package com.jn.langx.util.os.virtualization;

import com.jn.langx.commandline.CommandLine;
import com.jn.langx.commandline.DaemonCommandLineExecutor;
import com.jn.langx.commandline.DefaultCommandLineExecutor;
import com.jn.langx.util.os.OS;
import com.jn.langx.util.struct.Holder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RuntimeContainers {
    private static final Logger logger = LoggerFactory.getLogger(RuntimeContainers.class);

    private static Holder<RuntimeContainer> runtimeContainer;

    public static RuntimeContainer getRuntimeContainer() {
        if (runtimeContainer == null) {
            DefaultCommandLineExecutor executor = new DaemonCommandLineExecutor();
            CommandLine commandLine = null;
            GetRuntimeContainerHandler handler = null;
            if (OS.isFamilyWindows()) {
                // commandLine = CommandLine.parse("wmic cpu get ProcessorId");
                handler = null;
            } else if (OS.isFamilyUnix()) {
                commandLine = CommandLine.parse("cat /proc/1/cpuset");
                handler = new LinuxGetRuntimeContainerHandler();
            }
            if (commandLine != null && handler != null) {
                try {
                    executor.setStreamHandler(handler);
                    executor.execute(commandLine);
                } catch (Throwable ex) {
                    logger.error(ex.getMessage(), ex);
                }
            } else {
                logger.error("Unsupported operation for current platform");
            }
            runtimeContainer = new Holder<RuntimeContainer>(handler.getContainer());

        }
        return runtimeContainer.get();
    }


}
