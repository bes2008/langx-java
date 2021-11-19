package com.jn.langx.util;

import com.jn.langx.text.properties.PropertiesAccessor;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.StringMap;
import com.jn.langx.util.function.Functions;
import com.jn.langx.util.function.Predicate;
import com.jn.langx.util.logging.Loggers;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.List;
import java.util.Locale;

/**
 * 提供当前进程的系统属性值
 *
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

    /**
     * @return 返回系统属性：java.endorsed.dirs
     */
    public static String getJavaEndorsedDirs() {
        return System.getProperty("java.endorsed.dirs");
    }

    /**
     * @return 返回系统属性：java.ext.dirs
     */
    public static String getJavaExtDirs() {
        return System.getProperty("java.ext.dirs");
    }

    /**
     * @return 返回系统属性：java.io.tmpdir
     */
    public static String getJavaIOTmpDir() {
        return System.getProperty("java.io.tmpdir");
    }

    /**
     * OS PATH environment variable
     *
     * @return 返回系统属性：java.library.path
     */
    public static String getJavaLibraryPath() {
        return System.getProperty("java.library.path");
    }


    /**
     * JRE home directory
     *
     * @return 返回系统属性：java.home
     */
    public static String getJREHome() {
        return System.getProperty("java.home");
    }

    /**
     * JRE version
     *
     * @return 返回系统属性：java.runtime.version
     */
    public static String getJREVersion() {
        return System.getProperty("java.runtime.version");
    }

    /**
     * @return 返回系统属性：java.specification.version
     */
    public static String getJavaSpecificationVersion() {
        return System.getProperty("java.specification.version");
    }

    /**
     * @return 返回系统属性：java.class.version
     */
    public static String getJavaClassVersion() {
        return System.getProperty("java.class.version");
    }

    /**
     * @return 返回系统属性：java.version
     */
    public static String getJavaVersion() {
        return System.getProperty("java.version");
    }

    /**
     * @return 返回系统属性：os.arch
     */
    public static String getOSArch() {
        return System.getProperty("os.arch");
    }

    /**
     * @return 返回系统属性：os.name
     */
    public static String getOSName() {
        return System.getProperty("os.name");
    }

    /**
     * @return 返回系统属性：os.version
     */
    public static String getOSVersion() {
        return System.getProperty("os.version");
    }

    /**
     * @return 返回系统属性：user.home
     */
    public static String getUserHome() {
        return System.getProperty("user.home");
    }

    /**
     * @return 返回系统属性：user.name
     */
    public static String getUserName() {
        return System.getProperty("user.name");
    }

    /**
     * @return 返回：Locale.getDefault()
     */
    public static Locale getUserLocal() {
        return Locale.getDefault();
    }

    /**
     * @return 返回系统属性：user.dir
     */
    public static String getUserWorkDir() {
        return System.getProperty("user.dir");
    }

    /**
     * @return 返回系统属性：file.encoding
     */
    public static String getFileEncoding() {
        return System.getProperty("file.encoding");
    }

    /**
     * @return 返回系统属性：line.separator
     */
    public static String getLineSeparator() {
        return System.getProperty("line.separator");
    }

    public static String get(String key) {
        return get(key, null);
    }
    /**
     * Returns the value of the Java system property with the specified
     * {@code key}, while falling back to the specified default value if
     * the property access fails.
     *
     * @return the property value.
     *         {@code def} if there's no such property or if an access to the
     *         specified property is not allowed.
     */
    public static String get(final String key, String def) {
        if (key == null) {
            throw new NullPointerException("key");
        }
        if (key.isEmpty()) {
            throw new IllegalArgumentException("key must not be empty.");
        }

        String value = null;
        try {
            if (System.getSecurityManager() == null) {
                value = System.getProperty(key);
            } else {
                value = AccessController.doPrivileged(new PrivilegedAction<String>() {
                    @Override
                    public String run() {
                        return System.getProperty(key);
                    }
                });
            }
        } catch (SecurityException e) {
            Loggers.getLogger(SystemPropertys.class).warn("Unable to retrieve a system property '{}'; default values will be used.", key, e);
        }

        if (value == null) {
            return def;
        }

        return value;
    }


    public static PropertiesAccessor getAccessor() {
        return new PropertiesAccessor(System.getProperties());
    }

    /**
     * 如果不存在 指定的key,则设置为 value
     *
     * @param key
     * @param value
     * @since 2.10.2
     */
    public static void setPropertyIfAbsent(String key, String value) {
        setPropertyIfMatched(key, Functions.<String>nullPredicate(), value);
    }

    /**
     * 如果根据key获取到的值满足 predicate, 则将值设置为value
     *
     * @param key
     * @param value
     * @since 2.10.2
     */
    public static void setPropertyIfMatched(String key, Predicate<String> predicate, String value) {
        String v = System.getProperty(key);
        if (predicate.test(v)) {
            System.setProperty(key, value);
        }
    }

    /**
     * 如果根据key获取到的值满足 predicate, 则将值设置为value
     *
     * @param key
     * @param value
     * @since 3.5.6
     */
    public static void setProperty(String key, String value){
        System.setProperty(key, value);
    }
}
