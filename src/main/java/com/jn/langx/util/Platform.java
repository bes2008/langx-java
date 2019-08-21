package com.jn.langx.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;


public class Platform {
    private static final Logger logger = LoggerFactory.getLogger(Platform.class);
    public static boolean isWindows = isWindows0();
    public static int JAVA_VERSION_INT = javaVersion();
    public static boolean isAndroid = isAndroid0();
    private static final boolean IS_IVKVM_DOT_NET = isIkvmDotNet0();

    private static boolean isWindows0() {
        return System.getProperty("os.name", "").toLowerCase(Locale.US).contains("win");
    }

    private static boolean isIkvmDotNet0() {
        String vmName = System.getProperty("java.vm.name", "").toUpperCase(Locale.US);
        return vmName.equals("IKVM.NET");
    }

    private static boolean isAndroid0() {
        // Idea: Sometimes java binaries include Android classes on the classpath, even if it isn't actually Android.
        // Rather than check if certain classes are present, just check the VM, which is tied to the JDK.

        // Optional improvement: check if `android.os.Build.VERSION` is >= 24. On later versions of Android, the
        // OpenJDK is used, which means `Unsafe` will actually work as expected.

        // Android sets this property to Dalvik, regardless of whether it actually is.
        String vmName = System.getProperty("java.vm.name");
        boolean isAndroid = "Dalvik".equals(vmName);
        if (isAndroid) {
            logger.debug("Platform: Android");
        }
        return isAndroid;
    }

    private static int javaVersion() {
        final int majorVersion;

        if (isAndroid0()) {
            majorVersion = 6;
        } else {
            majorVersion = majorVersionFromJavaSpecificationVersion();
        }

        logger.debug("Java version: {}", majorVersion);

        return majorVersion;
    }

    // Package-private for testing only
    private static int majorVersionFromJavaSpecificationVersion() {
        return majorVersion(System.getProperty("java.specification.version", "1.6"));
    }

    // Package-private for testing only
    private static int majorVersion(final String javaSpecVersion) {
        final String[] components = javaSpecVersion.split("\\.");
        final int[] version = new int[components.length];
        for (int i = 0; i < components.length; i++) {
            version[i] = Integer.parseInt(components[i]);
        }

        if (version[0] == 1) {
            assert version[1] >= 6;
            return version[1];
        } else {
            return version[0];
        }
    }

    public static boolean equals(byte[] bytes1, int startPos1, byte[] bytes2, int startPos2, int length) {
        final int end = startPos1 + length;
        for (; startPos1 < end; ++startPos1, ++startPos2) {
            if (bytes1[startPos1] != bytes2[startPos2]) {
                return false;
            }
        }
        return true;
    }
}
