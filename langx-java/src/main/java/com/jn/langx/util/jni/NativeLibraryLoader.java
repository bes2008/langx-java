package com.jn.langx.util.jni;

import com.jn.langx.util.Strings;
import com.jn.langx.util.SystemPropertys;
import com.jn.langx.util.io.IOs;
import com.jn.langx.util.io.file.Files;
import com.jn.langx.util.logging.Loggers;
import com.jn.langx.util.os.Platform;
import com.jn.langx.util.reflect.Reflects;
import org.slf4j.Logger;

import java.io.*;
import java.lang.reflect.Method;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Arrays;

public final class NativeLibraryLoader {


    private static final String NATIVE_RESOURCE_HOME = "META-INF/native/";
    private static final File WORKDIR;
    private static final boolean DELETE_NATIVE_LIB_AFTER_LOADING;
    private static final String NATIVE_LIBRARY_KEY_PREFIX = "com.jn.langx.native";
    private static final String NATIVE_LIBRARY_WORK_DIR_KEY = NATIVE_LIBRARY_KEY_PREFIX + ".workdir";
    private static final String NATIVE_LIBRARY_DELETE_AFTER_LOADING_KEY = NATIVE_LIBRARY_KEY_PREFIX + ".deleteLibAfterLoading";

    static {
        File WORKDIR1 = null;
        String workdir = SystemPropertys.get(NATIVE_LIBRARY_WORK_DIR_KEY);
        Logger logger = Loggers.getLogger(NativeLibraryLoader.class);

        boolean assignWorkDirFailed = false;
        if (Strings.isNotEmpty(workdir)) {
            try {
                File f = Files.newFile(workdir);
                if (!f.exists()) {
                    f.mkdirs();
                }

                f = f.getAbsoluteFile();
                WORKDIR1 = f;
                logger.debug("-D{}: {}", NATIVE_LIBRARY_WORK_DIR_KEY, WORKDIR1);
            } catch (Throwable e) {
                assignWorkDirFailed = true;
            }
        }
        if (assignWorkDirFailed) {
            WORKDIR1 = tmpdir();
            logger.debug("-D{}: {} ", NATIVE_LIBRARY_WORK_DIR_KEY, WORKDIR1);
        }
        WORKDIR = WORKDIR1;
        DELETE_NATIVE_LIB_AFTER_LOADING = SystemPropertys.getAccessor().getBoolean(NATIVE_LIBRARY_DELETE_AFTER_LOADING_KEY, true);
    }

    private static File tmpdir() {
        File f;
        try {
            f = toDirectory(SystemPropertys.getJavaIOTmpDir());
            if (f != null) {
                return f;
            }

            // This shouldn't happen, but just in case ..
            if (Platform.isWindows) {
                f = toDirectory(System.getenv("TEMP"));
                if (f != null) {
                    return f;
                }

                String userprofile = System.getenv("USERPROFILE");
                if (userprofile != null) {
                    f = toDirectory(userprofile + "\\AppData\\Local\\Temp");
                    if (f != null) {
                        return f;
                    }

                    f = toDirectory(userprofile + "\\Local Settings\\Temp");
                    if (f != null) {
                        return f;
                    }
                }
            } else {
                f = toDirectory(System.getenv("TMPDIR"));
                if (f != null) {
                    return f;
                }
            }
        } catch (Exception ignored) {
            // Environment variable inaccessible
        }

        // Last resort.
        if (Platform.isWindows) {
            f = new File("C:\\Windows\\Temp");
        } else {
            f = new File("/tmp");
        }
        Logger logger = Loggers.getLogger(NativeLibraryLoader.class);
        logger.warn("Failed to get the temporary directory; falling back to: {} ", f);
        return f;
    }

    private static File toDirectory(String path) {
        if (path == null) {
            return null;
        }
        File f = null;
        try {
            f = Files.newFile(path);
            if(!f.exists()) {
                f.mkdirs();
            }
            if (!f.exists() || !f.isDirectory()) {
                return null;
            }
            return f.getAbsoluteFile();
        } catch (Exception ignored) {
            return f;
        }
    }


    /**
     * Loads the first available library in the collection with the specified
     * {@link ClassLoader}.
     *
     * @throws IllegalArgumentException if none of the given libraries load successfully.
     */
    public static void loadFirstAvailable(ClassLoader loader, String... names) {
        Logger logger = Loggers.getLogger(NativeLibraryLoader.class);
        for (String name : names) {
            try {
                load(name, loader);
                logger.debug("Successfully loaded the library: {}", name);
                return;
            } catch (Exception t) {
                logger.debug("Unable to load the library '{}', trying next name...", name, t);
            }
        }
        throw new IllegalArgumentException("Failed to load any of the given libraries: "
                + Arrays.toString(names));
    }

    /**
     * The shading prefix added to this class's full name.
     *
     * @throws UnsatisfiedLinkError if the shader used something other than a prefix
     */
    private static String calculatePackagePrefix() {
        String maybeShaded = NativeLibraryLoader.class.getName();
        // Use ! instead of . to avoid shading utilities from modifying the string
        String expected = NativeLibraryLoader.class.getName();
        if (!maybeShaded.endsWith(expected)) {
            throw new UnsatisfiedLinkError(String.format("Could not find prefix added to %s to get %s. When shading, only adding a package prefix is supported", expected, maybeShaded));
        }
        return maybeShaded.substring(0, maybeShaded.length() - expected.length());
    }

    /**
     * Load the given library with the specified {@link ClassLoader}
     */
    public static void load(String originalName, ClassLoader loader) {
        // Adjust expected name to support shading of native libraries.
        String implicitPackagePrefix = calculatePackagePrefix();
        // The system property should not be necessary; it can be removed in the future.
        String packagePrefix = SystemPropertys.get("com.jn.langx.packagePrefix", implicitPackagePrefix);
        String name = packagePrefix.replace('.', '-') + originalName;

        String libname = System.mapLibraryName(name);
        String path = NATIVE_RESOURCE_HOME + libname;

        URL url = loader.getResource(path);
        if (url == null && Platform.isOSX) {
            if (path.endsWith(".jnilib")) {
                url = loader.getResource(NATIVE_RESOURCE_HOME + "lib" + name + ".dynlib");
            } else {
                url = loader.getResource(NATIVE_RESOURCE_HOME + "lib" + name + ".jnilib");
            }
        }

        if (url == null) {
            // Fall back to normal loading of JNI stuff
            loadLibrary(loader, name, false);
            return;
        }

        int index = libname.lastIndexOf('.');
        String prefix = libname.substring(0, index);
        String suffix = libname.substring(index);
        InputStream in = null;
        OutputStream out = null;
        File tmpFile = null;
        Logger logger = Loggers.getLogger(NativeLibraryLoader.class);
        try {
            tmpFile = Files.createTempFile(WORKDIR, prefix, suffix);
            in = url.openStream();
            out = new FileOutputStream(tmpFile);

            byte[] buffer = new byte[8192];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
            out.flush();

            // Close the output stream before loading the unpacked library,
            // because otherwise Windows will refuse to load it when it's in use by other process.
            IOs.close(out);
            out = null;

            loadLibrary(loader, tmpFile.getPath(), true);
        } catch (UnsatisfiedLinkError e) {
            try {
                if (tmpFile != null && tmpFile.isFile() && tmpFile.canRead() &&
                        !NoexecVolumeDetector.canExecuteExecutable(tmpFile)) {
                    logger.info("{} exists but cannot be executed even when execute permissions set; " +
                                    "check volume for \"noexec\" flag; use -D{}=[path] " +
                                    "to set native working directory separately.",
                            tmpFile.getPath(), NATIVE_LIBRARY_WORK_DIR_KEY);
                }
            } catch (Exception t) {
                logger.debug("Error checking if {} is on a file store mounted with noexec", tmpFile, t);
            }
            // Re-throw to fail the load
            throw e;
        } catch (Exception e) {
            throw (UnsatisfiedLinkError) new UnsatisfiedLinkError("could not load a native library: " + name).initCause(e);
        } finally {
            IOs.close(in);
            IOs.close(out);
            // After we load the library it is safe to delete the file.
            // We delete the file immediately to free up resources as soon as possible,
            // and if this fails fallback to deleting on JVM exit.
            if (tmpFile != null && (!DELETE_NATIVE_LIB_AFTER_LOADING || !tmpFile.delete())) {
                tmpFile.deleteOnExit();
            }
        }
    }

    /**
     * Loading the native library into the specified {@link ClassLoader}.
     *
     * @param loader   - The {@link ClassLoader} where the native library will be loaded into
     * @param name     - The native library path or name
     * @param absolute - Whether the native library will be loaded by path or by name
     */
    private static void loadLibrary(final ClassLoader loader, final String name, final boolean absolute) {
        Logger logger = Loggers.getLogger(NativeLibraryLoader.class);
        try {
            // Make sure the helper is belong to the target ClassLoader.
            final Class<?> newHelper = tryToLoadClass(loader, NativeLibraryUtil.class);
            loadLibraryByHelper(newHelper, name, absolute);
            return;
        } catch (UnsatisfiedLinkError e) { // Should by pass the UnsatisfiedLinkError here!
            logger.debug("Unable to load the library '{}', trying other loading mechanism.", name, e);
        } catch (Exception e) {
            logger.debug("Unable to load the library '{}', trying other loading mechanism.", name, e);
        }
        NativeLibraryUtil.loadLibrary(name, absolute);  // Fallback to local helper class.
    }

    private static void loadLibraryByHelper(final Class<?> helper, final String name, final boolean absolute)
            throws UnsatisfiedLinkError {
        Object ret = AccessController.doPrivileged(new PrivilegedAction<Object>() {
            @Override
            public Object run() {
                try {
                    // Invoke the helper to load the native library, if succeed, then the native
                    // library belong to the specified ClassLoader.
                    Method method = helper.getMethod("loadLibrary", String.class, boolean.class);
                    Reflects.makeAccessible(method);
                    return method.invoke(null, name, absolute);
                } catch (Exception e) {
                    return e;
                }
            }
        });
        if (ret instanceof Throwable) {
            Throwable error = (Throwable) ret;
            Throwable cause = error.getCause();
            if (cause != null) {
                if (cause instanceof UnsatisfiedLinkError) {
                    throw (UnsatisfiedLinkError) cause;
                } else {
                    throw new UnsatisfiedLinkError(cause.getMessage());
                }
            }
            throw new UnsatisfiedLinkError(error.getMessage());
        }
    }

    /**
     * Try to load the helper {@link Class} into specified {@link ClassLoader}.
     *
     * @param loader - The {@link ClassLoader} where to load the helper {@link Class}
     * @param helper - The helper {@link Class}
     * @return A new helper Class defined in the specified ClassLoader.
     * @throws ClassNotFoundException Helper class not found or loading failed
     */
    private static Class<?> tryToLoadClass(final ClassLoader loader, final Class<?> helper)
            throws ClassNotFoundException {
        try {
            return loader.loadClass(helper.getName());
        } catch (ClassNotFoundException e) {
            // The helper class is NOT found in target ClassLoader, we have to define the helper class.
            final byte[] classBinary = classToByteArray(helper);
            return AccessController.doPrivileged(new PrivilegedAction<Class<?>>() {
                @Override
                public Class<?> run() {
                    try {
                        // Define the helper class in the target ClassLoader,
                        //  then we can call the helper to load the native library.
                        Method defineClass = Reflects.getDeclaredMethod(ClassLoader.class, "defineClass", String.class,
                                byte[].class, int.class, int.class);
                        Reflects.makeAccessible(defineClass);
                        return (Class<?>) defineClass.invoke(loader, helper.getName(), classBinary, 0,
                                classBinary.length);
                    } catch (Exception e) {
                        throw new IllegalStateException("Define class failed!", e);
                    }
                }
            });
        }
    }

    /**
     * Load the helper {@link Class} as a byte array, to be redefined in specified {@link ClassLoader}.
     *
     * @param clazz - The helper {@link Class} provided by this bundle
     * @return The binary content of helper {@link Class}.
     * @throws ClassNotFoundException Helper class not found or loading failed
     */
    private static byte[] classToByteArray(Class<?> clazz) throws ClassNotFoundException {
        String fileName = clazz.getName();
        int lastDot = fileName.lastIndexOf('.');
        if (lastDot > 0) {
            fileName = fileName.substring(lastDot + 1);
        }
        URL classUrl = clazz.getResource(fileName + ".class");
        if (classUrl == null) {
            throw new ClassNotFoundException(clazz.getName());
        }
        byte[] buf = new byte[1024];
        ByteArrayOutputStream out = new ByteArrayOutputStream(4096);
        InputStream in = null;
        try {
            in = classUrl.openStream();
            for (int r; (r = in.read(buf)) != -1; ) {
                out.write(buf, 0, r);
            }
            return out.toByteArray();
        } catch (IOException ex) {
            throw new ClassNotFoundException(clazz.getName(), ex);
        } finally {
            IOs.close(in);
            IOs.close(out);
        }
    }


    private NativeLibraryLoader() {
        // Utility
    }

    private static final class NoexecVolumeDetector {

        private static boolean canExecuteExecutable(File file) {
            if (Platform.JAVA_VERSION_INT < 7) {
                // Pre-JDK7, the Java API did not directly support POSIX permissions; instead of implementing a custom
                // work-around, assume true, which disables the check.
                return true;
            }

            // If we can already execute, there is nothing to do.
            file.canExecute();

            // On volumes, with noexec set, even files with the executable POSIX permissions will fail to execute.
            // The File#canExecute() method honors this behavior, probaby via parsing the noexec flag when initializing
            // the UnixFileStore, though the flag is not exposed via a public API.  To find out if library is being
            // loaded off a volume with noexec, confirm or add executalbe permissions, then check File#canExecute().

            // Note: We use FQCN to not break when netty is used in java6
            /*
            Set<java.nio.file.attribute.PosixFilePermission> existingFilePermissions =
                    java.nio.file.Files.getPosixFilePermissions(file.toPath());
            Set<java.nio.file.attribute.PosixFilePermission> executePermissions =
                    EnumSet.of(java.nio.file.attribute.PosixFilePermission.OWNER_EXECUTE,
                            java.nio.file.attribute.PosixFilePermission.GROUP_EXECUTE,
                            java.nio.file.attribute.PosixFilePermission.OTHERS_EXECUTE);
            if (existingFilePermissions.containsAll(executePermissions)) {
                return false;
            }

            Set<java.nio.file.attribute.PosixFilePermission> newPermissions = EnumSet.copyOf(existingFilePermissions);
            newPermissions.addAll(executePermissions);
            java.nio.file.Files.setPosixFilePermissions(file.toPath(), newPermissions);
            return file.canExecute();

             */
            return true;
        }

        private NoexecVolumeDetector() {
            // Utility
        }
    }
}
