package com.jn.langx.util.jni;

public class NativeLibraryUtil {
    /**
     * Delegate the calling to {@link System#load(String)} or {@link System#loadLibrary(String)}.
     * @param libName - The native library path or name
     * @param absolute - Whether the native library will be loaded by path or by name
     */
    public static void loadLibrary(String libName, boolean absolute) {
        if (absolute) {
            System.load(libName);
        } else {
            System.loadLibrary(libName);
        }
    }

    private NativeLibraryUtil() {
        // Utility
    }
}
