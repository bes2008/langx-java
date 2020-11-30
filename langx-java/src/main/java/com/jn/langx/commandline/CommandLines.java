package com.jn.langx.commandline;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;
import com.jn.langx.commandline.streamhandler.OutputLinesExecuteStreamHandler;
import com.jn.langx.util.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;
import java.util.Map;

public class CommandLines {
    private static final Logger logger = LoggerFactory.getLogger(CommandLines.class);

    private CommandLines() {
    }

    public static List<String> execute(@NonNull CommandLine commandLine, @Nullable File workDirectory, @Nullable Map<String, String> environment) {
        Preconditions.checkNotEmpty(commandLine, "the command line is null or empty");
        CommandLineExecutor executor = new DefaultCommandLineExecutor();
        if (workDirectory != null) {
            executor.setWorkingDirectory(workDirectory);
        }
        OutputLinesExecuteStreamHandler h = new OutputLinesExecuteStreamHandler();
        executor.setStreamHandler(h);
        try {
            executor.execute(commandLine, environment);
        } catch (Throwable ex) {
            logger.error(ex.getMessage(), ex);
        }
        return h.getOutputContent();
    }
}
