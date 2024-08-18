package com.jn.langx.util.os;

import com.jn.langx.util.JvmConstants;
import com.jn.langx.util.Strings;
import com.jn.langx.util.SystemPropertys;
import com.jn.langx.util.io.file.Files;
import com.jn.langx.util.logging.Loggers;
import com.jn.langx.util.reflect.Reflects;
import org.slf4j.Logger;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import static com.jn.langx.util.SystemPropertys.getJavaIOTmpDir;

public class Platform {
    private static final String OSNAME = SystemPropertys.get("os.name", "").toLowerCase(Locale.US).replaceAll("[^a-z0-9]+", "");

    public static final boolean isWindows = isWindows0();
    public static final int JAVA_VERSION_INT = javaVersion();
    public static final boolean isAndroid = isAndroid0();
    public static final boolean isKaffeJVM = isKaffeJVM();
    private static final boolean IS_IVKVM_DOT_NET = isIkvmDotNet0();
    public static final boolean isGroovyAvailable = isGroovyAvailable0();
    public static final boolean isMaxOS = OS.isMaxOSX();
    public static final boolean isOSX = isOSX();
    public static final String processId = getProcessId0();
    // See https://github.com/oracle/graal/blob/master/sdk/src/org.graalvm.nativeimage/src/org/graalvm/nativeimage/ImageInfo.java
    private static final boolean imageCode = (System.getProperty("org.graalvm.nativeimage.imagecode") != null);

    private Platform() {

    }

    /**
     * Return whether this runtime environment lives within a native image.
     */
    public static boolean inImageCode() {
        return imageCode;
    }


    private static boolean isOSX() {
        return OSNAME.startsWith("macosx") || OSNAME.startsWith("osx");
    }

    private static boolean isWindows0() {
        return System.getProperty("os.name", "").toLowerCase(Locale.US).contains("win");
    }

    private static boolean isIkvmDotNet0() {
        String vmName = System.getProperty("java.vm.name", "").toUpperCase(Locale.US);
        return "IKVM.NET".equals(vmName);
    }

    private static boolean isAndroid0() {
        // Idea: Sometimes java binaries include Android classes on the classpath, even if it isn't actually Android.
        // Rather than check if certain classes are present, just check the VM, which is tied to the JDK.

        // Optional improvement: check if `android.os.Build.VERSION` is >= 24. On later versions of Android, the
        // OpenJDK is used, which means `Unsafe` will actually work as expected.

        // Android sets this property to Dalvik, regardless of whether it actually is.
        String vmName = System.getProperty("java.vm.name");
        boolean isAndroid = "Dalvik".equals(vmName);
        if (!isAndroid) {
            String runtime = System.getProperty("java.runtime.name");
            isAndroid = Strings.getEmptyIfNull(runtime).toLowerCase().contains("android");
        }
        if (isAndroid) {
            Logger logger = Loggers.getLogger(Platform.class);
            logger.debug("Platform: Android");
        }
        return isAndroid;
    }

    private static boolean isKaffeJVM() {
        try {
            Class.forName("kaffe.util.NotImplemented");
            return true;
        } catch (Exception t) {
            // swallow as this simply doesn't seem to be Kaffe
        }
        return false;
    }

    private static int javaVersion() {
        final int majorVersion;

        if (isAndroid0()) {
            majorVersion = 6;
        } else {
            majorVersion = majorVersionFromJavaSpecificationVersion();
        }
        Loggers.getLogger(Platform.class).debug("Java version: {}", majorVersion);

        return majorVersion;
    }

    // Package-private for testing only
    private static int majorVersionFromJavaSpecificationVersion() {
        // http://www.oracle.com/technetwork/java/javase/versioning-naming-139433.html
        // http://openjdk.java.net/jeps/223 "New Version-String Scheme"

        String vm = System.getProperty("java.version"); // JLS 20.18.7
        if (vm == null) {
            vm = System.getProperty("java.runtime.version");
        }
        if (vm == null) {
            vm = System.getProperty("java.specification.version", "1.6");
        }
        return majorVersion(vm);
    }

    // Package-private for testing only
    private static int majorVersion(String javaVersion) {
        int index = Strings.indexOf(javaVersion, "_", 0);
        if (index != -1) {
            javaVersion = javaVersion.substring(0, index);
        }
        final String[] components = javaVersion.split("\\.");
        final int[] version = new int[components.length];
        for (int i = 0; i < components.length; i++) {
            try {
                version[i] = Integer.parseInt(components[i]);
            } catch (Exception ex) {
                // ignore it
            }
        }

        if (version[0] == 1) {
            return version[1];
        } else {
            return version[0];
        }
    }


    public static boolean is3VMOrGreater() {
        return JAVA_VERSION_INT >= 3;
    }

    public static boolean is4VMOrGreater() {
        return JAVA_VERSION_INT >= 4;
    }

    public static boolean is5VMOrGreater() {
        return JAVA_VERSION_INT >= 5;
    }

    public static boolean is6VMOrGreater() {
        return JAVA_VERSION_INT >= 6;
    }

    public static boolean is7VMOrGreater() {
        return JAVA_VERSION_INT >= 7;
    }

    public static boolean is8VMOrGreater() {
        return JAVA_VERSION_INT >= 8;
    }

    public static boolean is9VMOrGreater() {
        return JAVA_VERSION_INT >= 9;
    }

    public static boolean is10VMOrGreater() {
        return JAVA_VERSION_INT >= 10;
    }

    public static boolean is11VMOrGreater() {
        return JAVA_VERSION_INT >= 11;
    }

    public static boolean is12VMOrGreater() {
        return JAVA_VERSION_INT >= 12;
    }

    public static boolean is13VMOrGreater() {
        return JAVA_VERSION_INT >= 13;
    }
    public static boolean is15VMOrGreater() {
        return JAVA_VERSION_INT >= 15;
    }
    public static boolean is17VMOrGreater() {
        return JAVA_VERSION_INT >= 17;
    }

    public static boolean is21VMOrGreater() {
        return JAVA_VERSION_INT >= 21;
    }
    public static boolean is22VMOrGreater() {
        return JAVA_VERSION_INT >= 22;
    }

    private static final Map<Integer, Integer> classMajorVersionToJdkVersion = new LinkedHashMap<Integer, Integer>();

    static {
        // jdk 版本发布矩阵：https://www.java.com/en/releases/matrix/
        classMajorVersionToJdkVersion.put((int) JvmConstants.MAJOR_1_1, 1);
        classMajorVersionToJdkVersion.put((int) JvmConstants.MAJOR_1_2, 2);
        classMajorVersionToJdkVersion.put((int) JvmConstants.MAJOR_1_3, 3);
        classMajorVersionToJdkVersion.put((int) JvmConstants.MAJOR_1_4, 4);
        classMajorVersionToJdkVersion.put((int) JvmConstants.MAJOR_1_5, 5);
        classMajorVersionToJdkVersion.put((int) JvmConstants.MAJOR_1_6, 6);
        classMajorVersionToJdkVersion.put((int) JvmConstants.MAJOR_1_7, 7);
        classMajorVersionToJdkVersion.put((int) JvmConstants.MAJOR_1_8, 8);
        classMajorVersionToJdkVersion.put((int) JvmConstants.MAJOR_1_9, 9);
        classMajorVersionToJdkVersion.put((int) JvmConstants.MAJOR_10, 10);
        classMajorVersionToJdkVersion.put((int) JvmConstants.MAJOR_11, 11);
        classMajorVersionToJdkVersion.put((int) JvmConstants.MAJOR_12, 12);
        classMajorVersionToJdkVersion.put((int) JvmConstants.MAJOR_13, 14);
        classMajorVersionToJdkVersion.put((int) JvmConstants.MAJOR_14, 14);
        classMajorVersionToJdkVersion.put((int) JvmConstants.MAJOR_15, 15);
        classMajorVersionToJdkVersion.put((int) JvmConstants.MAJOR_16, 16);
        classMajorVersionToJdkVersion.put((int) JvmConstants.MAJOR_17, 17);
        classMajorVersionToJdkVersion.put((int) JvmConstants.MAJOR_18, 18);
        classMajorVersionToJdkVersion.put((int) JvmConstants.MAJOR_19, 19);
        classMajorVersionToJdkVersion.put((int) JvmConstants.MAJOR_20, 20);
        classMajorVersionToJdkVersion.put((int) JvmConstants.MAJOR_21, 21);
        classMajorVersionToJdkVersion.put((int) JvmConstants.MAJOR_22, 22);
    }

    public static int getJavaVersion(int classMajorVersion) {
        return classMajorVersionToJdkVersion.get(classMajorVersion);
    }

    /**
     * Find java executable File path from java.home system property.
     *
     * @return File associated with the java command, or null if not found.
     */
    public static File getJavaExecutable() {
        String javaHome = null;
        File result = null;
        // java.home
        // java.class.path
        // java.ext.dirs
        try {
            javaHome = System.getProperty("java.home");
        } catch (Exception t) {
            // ignore
        }
        if (null != javaHome) {
            try {
                File binDir = Files.newFile(javaHome, "bin");
                if (binDir.isDirectory() && binDir.canRead()) {
                    String[] execs = new String[]{"java", "java.exe"};
                    for (String exec : execs) {
                        result = new File(binDir, exec);
                        if (result.canRead()) {
                            break;
                        }
                    }
                }
            }catch (Throwable e){
                // ignore ite
            }
        }
        return result;
    }

    private static boolean isGroovyAvailable0() {
        ClassLoader loader = Platform.class.getClassLoader();
        if (loader == null) {
            loader = ClassLoader.getSystemClassLoader();
        }
        try {
            Class bindingClass = loader.loadClass("groovy.lang.Binding");
            return bindingClass != null;
        } catch (ClassNotFoundException ex) {
            return false;
        }
    }

    private static String getProcessId0() {
        try {
            if (isAndroid) {
                Object runtimeMXBean = Reflects.getDeclaredMethod(Class.forName("java.lang.management.ManagementFactory"), "getRuntimeMXBean").invoke(null);
                return Reflects.getDeclaredMethod(Class.forName("java.lang.management.RuntimeMXBean"), "getName").invoke(runtimeMXBean).toString().split("@")[0];
            } else {
                java.lang.management.RuntimeMXBean runtimeMXBean = java.lang.management.ManagementFactory.getRuntimeMXBean();
                return runtimeMXBean.getName().split("@")[0];
            }
        } catch (Exception ex) {
            if (isAndroid) {
                try {
                    return new File("/proc/self").getCanonicalFile().getName();
                } catch (IOException e) {
                    return null;
                }
            }
            return null;
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

    /**
     * Returns the path to the system temporary directory.
     *
     * @return the path to the system temporary directory.
     */
    public static String getTempDirectoryPath() {
        return getJavaIOTmpDir();
    }

    /**
     * Returns a {@link File} representing the system temporary directory.
     *
     * @return the system temporary directory.
     */
    public static File getTempDirectory() {
        return Files.newFile(getTempDirectoryPath());
    }

    /**
     * Returns the path to the user's home directory.
     *
     * @return the path to the user's home directory.
     */
    public static String getUserHomeDirectoryPath() {
        return SystemPropertys.getUserHome();
    }

    /**
     * Returns a {@link File} representing the user's home directory.
     *
     * @return the user's home directory.
     */
    public static File getUserHomeDirectory() {
        return Files.newFile(getUserHomeDirectoryPath());
    }

    public static int cpuCore() {
        return CpuCoreSensor.availableProcessors();
    }

    public static boolean isInDockerEnv(){
        return Docker.isDocker();
    }

    public static long getTotalPhysicalMemory() {
        try {
            MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
            Object attribute = mBeanServer.getAttribute(new ObjectName("java.lang", "type", "OperatingSystem"), "TotalPhysicalMemorySize");
            return (Long)attribute;
        } catch (Exception e) {
            Loggers.getLogger(Platform.class).error(e.getMessage(),e);
            return -1L;
        }
    }
    private static OsArch getCurrentOSArch(){
        String osarchString = SystemPropertys.getOSArch();
        return OsArch.findByName(osarchString);
    }

    /**
     * 这个得到的其实是JVM的情况
     */
    public static final OsArch osArch = getCurrentOSArch();

    private static int getJvmBit(){
        String dataModel = System.getProperty("sun.arch.data.model");
        try {
            return Integer.parseInt(dataModel);
        }catch (Throwable e){
            return osArch.getBit();
        }
    }
    public static int jvmBit = getJvmBit();

}
