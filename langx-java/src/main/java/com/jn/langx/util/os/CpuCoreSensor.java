package com.jn.langx.util.os;


import com.jn.langx.util.Maths;
import com.jn.langx.util.function.Consumer2;
import com.jn.langx.util.function.Functions;
import com.jn.langx.util.function.Predicate2;
import com.jn.langx.util.io.Channels;
import com.jn.langx.util.io.Charsets;
import com.jn.langx.util.io.IOs;
import com.jn.langx.util.struct.Holder;

import java.io.*;
import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * Utility class providing the number of CPU cores available. On Linux, to handle CGroups, it reads and
 * parses the /proc/self/status file.
 * <p>
 * This class derives from https://github.com/wildfly/wildfly-common/blob/master/src/main/java/org/wildfly/common/cpu/ProcessorInfo.java
 * licensed under the Apache Software License 2.0.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 * @since 4.1.0
 */
class CpuCoreSensor {


    private static final String CPUS_ALLOWED = "Cpus_allowed:";
    private static final byte[] BITS = new byte[]{0, 1, 1, 2, 1, 2, 2, 3, 1, 2, 2, 3, 2, 3, 3, 4};

    private CpuCoreSensor() {
    }

    /**
     * Returns the number of processors available to this process.
     * <p>
     * On most operating systems this method simply delegates to {@link Runtime#availableProcessors()}. However, on
     * Linux, this strategy is insufficient, since the JVM does not take into consideration the process' CPU set affinity
     * which is employed by cgroups and numactl. Therefore this method will analyze the Linux proc filesystem
     * to make the determination.
     * <p>
     * Since the CPU affinity of a process can be change at any time, this method does not cache the result.
     * <p>
     * Note that on Linux, both SMT units (Hyper-Threading) and CPU cores are counted as a processor.
     * <p>
     * This method is used to configure the default number of event loops. This settings can be overridden by the user.
     *
     * @return the available processors on this system.
     */
    public static int availableProcessors() {
        if (System.getSecurityManager() != null) {
            return AccessController.doPrivileged(new PrivilegedAction<Integer>() {
                @Override
                public Integer run() {
                    return determineProcessors();
                }
            });
        }

        return determineProcessors();
    }

    private static int determineProcessors() {
        int fromJava = Runtime.getRuntime().availableProcessors();
        int fromProcFile = 0;

        if (!OS.isLinux()) {
            return fromJava;
        }

        try {
            fromProcFile = readCPUMask(new File("/proc/self/status"));
        } catch (Exception e) {
            // We can't do much at this point, we are on linux but using a different /proc format.
        }

        return fromProcFile > 0 ? Maths.min(fromJava, fromProcFile) : fromJava;
    }

    protected static int readCPUMask(File file) throws IOException {
        if (file == null || !file.exists()) {
            return -1;
        }
        FileInputStream stream = null;
        try {
            stream = new FileInputStream(file);

            final Holder<Integer> countHolder = new Holder<Integer>(-1);
            Channels.readLines(stream, Charsets.US_ASCII,
                    Functions.<Integer, String>booleanPredicate2(true),
                    new Consumer2<Integer, String>() {
                        @Override
                        public void accept(Integer index, String line) {
                            if (line.startsWith(CPUS_ALLOWED)) {
                                int count = 0;
                                int start = CPUS_ALLOWED.length();
                                for (int i = start; i < line.length(); i++) {
                                    char ch = line.charAt(i);
                                    if (ch >= '0' && ch <= '9') {
                                        count += BITS[ch - '0'];
                                    } else if (ch >= 'a' && ch <= 'f') {
                                        count += BITS[ch - 'a' + 10];
                                    } else if (ch >= 'A' && ch <= 'F') {
                                        count += BITS[ch - 'A' + 10];
                                    }
                                }
                                countHolder.set(count);
                            }
                        }
                    }, new Predicate2<Integer, String>() {
                        @Override
                        public boolean test(Integer key, String value) {
                            return countHolder.get() >= 0;
                        }
                    });
            return countHolder.get();
        } finally {
            IOs.close(stream);
        }
    }
}
