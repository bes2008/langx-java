package com.jn.langx.util;

import com.jn.langx.util.collection.Collects;
import com.jn.langx.text.properties.PropertiesAccessor;
import com.jn.langx.util.collection.StringMap;

import java.util.List;
import java.util.Locale;

/**
 * @author jinuo.fang
 */
public class SystemPropertys {

    public static final String NEWLINE = System.getProperty("line.separator", "\n");

    private static final StringMap javaVersionToClassVersion = new StringMap();

    static {
        javaVersionToClassVersion.put("1.1", "45");
        javaVersionToClassVersion.put("1.2", "46");
        javaVersionToClassVersion.put("1.3", "47");
        javaVersionToClassVersion.put("1.4", "48");
        javaVersionToClassVersion.put("1.5", "49");
        javaVersionToClassVersion.put("1.6", "50");
        javaVersionToClassVersion.put("1.7", "51");
        javaVersionToClassVersion.put("1.8", "52");
        javaVersionToClassVersion.put("9.0", "53");
        javaVersionToClassVersion.put("10.0", "54");
    }

    public static String getClassPathSeparator() {
        return System.getProperty("path.separator");
    }

    public static String getJavaClassPathString() {
        return System.getProperty("java.class.path");
    }

    public static List<String> getJavaClassPath() {
        String pathString = getJavaClassPathString();
        String pathSeparator = getClassPathSeparator();
        return Collects.asList(Strings.split(pathString, pathSeparator));
    }

    public static String getJavaEndorsedDirs() {
        return System.getProperty("java.endorsed.dirs");
    }

    public static String getJavaExtDirs() {
        return System.getProperty("java.ext.dirs");
    }


    public static String getJavaIOTmpDir() {
        return System.getProperty("java.io.tmpdir");
    }

    /**
     * OS PATH environment variable
     */
    public static String getJavaLibraryPath() {
        return System.getProperty("java.library.path");
    }


    /**
     * JRE home directory
     */
    public static String getJREHome() {
        return System.getProperty("java.home");
    }

    /**
     * JRE version
     */
    public static String getJREVersion() {
        return System.getProperty("java.runtime.version");
    }

    public static String getJavaSpecificationVersion() {
        return System.getProperty("java.specification.version");
    }

    public static String getJavaClassVersion() {
        return System.getProperty("java.class.version");
    }

    public static String getJavaVersion() {
        return System.getProperty("java.version");
    }

    public static String getOSArch() {
        return System.getProperty("os.arch");
    }

    public static String getOSName() {
        return System.getProperty("os.name");
    }

    public static String getOSVersion() {
        return System.getProperty("os.version");
    }

    public static String getUserHome() {
        return System.getProperty("user.home");
    }

    public static String getUserName() {
        return System.getProperty("user.name");
    }

    public static Locale getUserLocal() {
        return Locale.getDefault();
    }

    public static String getUserWorkDir() {
        return System.getProperty("user.dir");
    }

    public static String getFileEncoding() {
        return System.getProperty("file.encoding");
    }

    public static String getLineSeparator(){
        return System.getProperty("line.separator");
    }

    public static PropertiesAccessor getAccessor() {
        return new PropertiesAccessor(System.getProperties());
    }
}
