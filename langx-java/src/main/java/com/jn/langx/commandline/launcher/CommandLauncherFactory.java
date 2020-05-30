
package com.jn.langx.commandline.launcher;

import com.jn.langx.util.OS;

/**
 * Builds a command launcher for the OS and JVM we are running under.
 *
 * @version $Id: CommandLauncherFactory.java 1556869 2014-01-09 16:51:11Z britter $
 */
public final class CommandLauncherFactory {

    private CommandLauncherFactory() {
    }

    /**
     * Factory method to create an appropriate launcher.
     *
     * @return the command launcher
     */
    public static CommandLauncher createVMLauncher() {
        // Try using a JDK 1.3 launcher
        CommandLauncher launcher;

        if (OS.isFamilyOpenVms()) {
            launcher = new VmsCommandLauncher();
        } else {
            launcher = new Java13CommandLauncher();
        }

        return launcher;
    }
}
