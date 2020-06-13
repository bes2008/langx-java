package com.jn.langx.commandline.launcher;

import com.jn.langx.commandline.CommandLine;
import com.jn.langx.commandline.environment.EnvironmentUtils;

import java.io.File;
import java.io.IOException;
import java.util.Map;


/**
 * A command launcher for a particular JVM/OS platform. This class is a general
 * purpose command launcher which can only launch commands in the current
 * working directory.
 *
 * @version $Id: CommandLauncherImpl.java 1557338 2014-01-11 10:34:22Z sebb $
 */
public abstract class CommandLauncherImpl implements CommandLauncher {

    public Process exec(final CommandLine cmd, final Map<String, String> env)
            throws IOException {
        final String[] envVar = EnvironmentUtils.toStrings(env);
        return Runtime.getRuntime().exec(cmd.toStrings(), envVar);
    }

    public abstract Process exec(final CommandLine cmd, final Map<String, String> env,
            final File workingDir) throws IOException;

    /** @see com.jn.langx.commandline.launcher.CommandLauncher#isFailure(int) */
    public boolean isFailure(final int exitValue)
    {
        // non zero exit value signals failure
        return exitValue != 0;
    }
}
