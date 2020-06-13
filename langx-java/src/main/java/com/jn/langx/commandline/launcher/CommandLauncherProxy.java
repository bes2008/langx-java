package com.jn.langx.commandline.launcher;

import com.jn.langx.commandline.CommandLine;

import java.io.IOException;
import java.util.Map;


/**
 * A command launcher that proxies another command launcher. Sub-classes
 * override exec(args, env, workdir)
 *
 * @version $Id: CommandLauncherProxy.java 1557338 2014-01-11 10:34:22Z sebb $
 */
public abstract class CommandLauncherProxy extends CommandLauncherImpl {

    public CommandLauncherProxy(final CommandLauncher launcher) {
        myLauncher = launcher;
    }

    private final CommandLauncher myLauncher;

    /**
     * Launches the given command in a new process. Delegates this method to the
     * proxied launcher
     * 
     * @param cmd
     *            the command line to execute as an array of strings
     * @param env
     *            the environment to set as an array of strings
     * @throws IOException
     *             forwarded from the exec method of the command launcher
     */
    @Override
    public Process exec(final CommandLine cmd, final Map<String, String> env)
            throws IOException {
        return myLauncher.exec(cmd, env);
    }
}
