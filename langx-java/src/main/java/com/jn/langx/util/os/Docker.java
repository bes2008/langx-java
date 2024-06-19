package com.jn.langx.util.os;


import com.jn.langx.util.io.file.Files;

import java.io.File;

class Docker {
    private static Boolean docker = null;

    private Docker() {
    }

    public static boolean isDocker() {
        if (docker == null) {
            docker = hasDockerEnv() || hasDockerCGroup();
        }

        return docker;
    }

    private static boolean hasDockerEnv() {
        return (new File("/.dockerenv")).exists();
    }

    private static boolean hasDockerCGroup() {
        File cgroup = new File("/proc/self/cgroup");

        try {
            return Files.readAsText(cgroup).contains("docker");
        } catch (Throwable var2) {
            return false;
        }
    }
}
