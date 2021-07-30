package com.jn.langx.util.jni;

import com.jn.langx.util.os.Platform;

public class NativeLibraryUtil {
    /**
     * Delegate the calling to {@link System#load(String)} or {@link System#loadLibrary(String)}.
     *
     * @param libName  - The native library path or name
     * @param absolute - Whether the native library will be loaded by path or by name
     */
    public static void loadLibrary(String libName, boolean absolute) {
        if (absolute) {
            System.load(libName);
        } else {
            System.loadLibrary(libName);
        }
    }

    public static String libExtension() {
        return Platform.isWindows ? ".dll" : ".so";
    }

    private NativeLibraryUtil() {
        // Utility
    }
}
