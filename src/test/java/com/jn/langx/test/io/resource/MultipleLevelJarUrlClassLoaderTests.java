package com.jn.langx.test.io.resource;

import com.jn.langx.util.SystemPropertys;
import com.jn.langx.util.URLs;
import com.jn.langx.util.jar.MultipleLevelJarUrlClassLoader;
import com.jn.langx.util.reflect.Reflects;
import org.junit.Test;

import java.net.URL;

public class MultipleLevelJarUrlClassLoaderTests {

    @Test
    public void test() throws Throwable {
        String workDir = SystemPropertys.getUserWorkDir();
        String currentFilePath = workDir + "/src/test/java/" + Reflects.getPackageName(ResourceLoadTests.class).replaceAll("\\.", "/") + "/sqlhelper-examples-2.2.0.jar";// + URLs.JAR_URL_SEPARATOR + "BOOT-INF/lib/langx-java-1.0.0.jar" + URLs.JAR_URL_SEPARATOR + "com/jn/langx/util/collection/Collects.class";
        URL url = new URL(URLs.URL_PREFIX_FILE + currentFilePath);
        ClassLoader classLoader = new MultipleLevelJarUrlClassLoader(new URL[]{url}, Thread.currentThread().getContextClassLoader().getParent());
        try {
            classLoader.loadClass("com.jn.langx.util.collection.Collects");
        } catch (ClassNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
    }

}
