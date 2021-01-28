package com.jn.langx.commandline.launcher;

import com.jn.langx.commandline.CommandLine;
import com.jn.langx.commandline.InstructionSequence;
import com.jn.langx.commandline.ProcessAdapter;

import java.io.IOException;
import java.util.Map;


/**
 * A command launcher that proxies another command launcher. Sub-classes
 * override exec(args, env, workdir)
 */
public abstract class CommandLauncherProxy extends CommandLauncherImpl {

    public CommandLauncherProxy(final CommandLauncher<ProcessAdapter> launcher) {
        myLauncher = launcher;
    }

    private final CommandLauncher<ProcessAdapter> myLauncher;

    /**
     * Launches the given command in a new process. Delegates this method to the
     * proxied launcher
     *
     * @param cmd the command line to execute as an array of strings
     * @param env the environment to set as an array of strings
     * @throws IOException forwarded from the exec method of the command launcher
     */
    @Override
    public ProcessAdapter exec(final CommandLine cmd, final Map<String, String> env)
            throws IOException {
        return myLauncher.exec(cmd, env);
    }
}
