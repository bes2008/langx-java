package com.jn.langx.commandline.launcher;

import com.jn.langx.commandline.CommandLine;
import com.jn.langx.commandline.ProcessAdapter;
import com.jn.langx.commandline.environment.EnvironmentUtils;

import java.io.File;
import java.io.IOException;
import java.util.Map;


/**
 * A command launcher for a particular JVM/OS platform. This class is a general
 * purpose command launcher which can only launch commands in the current
 * working directory.
 */
public abstract class AbstractLocalCommandLauncher implements CommandLauncher<ProcessAdapter> {

    public final ProcessAdapter exec(final CommandLine cmd, final Map<String, String> env) throws IOException{
         return exec(cmd, env, null);
    }

    public abstract ProcessAdapter exec(final CommandLine cmd, final Map<String, String> env, final File workingDir) throws IOException;

    /**
     * @see com.jn.langx.commandline.launcher.CommandLauncher#isFailure(int)
     */
    public boolean isFailure(final int exitValue) {
        // non zero exit value signals failure
        return exitValue != 0;
    }

}