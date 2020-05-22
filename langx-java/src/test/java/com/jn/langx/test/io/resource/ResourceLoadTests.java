package com.jn.langx.test.io.resource;

import com.jn.langx.io.resource.*;
import com.jn.langx.util.SystemPropertys;
import com.jn.langx.util.net.URLs;
import com.jn.langx.util.reflect.Reflects;
import org.junit.Assert;
import org.junit.Test;

public class ResourceLoadTests {

    @Test
    public void testFileResource() {
        System.out.println("================FILE start==================");
        String workDir = SystemPropertys.getUserWorkDir();
        System.out.println(workDir);
        String currentFilePath = workDir + "/src/test/java/" + Reflects.getFQNClassName(ResourceLoadTests.class).replaceAll("\\.", "/") + ".java";
        FileResource fileResource = Resources.loadFileResource(currentFilePath);
        showFileResource(fileResource);
        FileResource fileResource2 = Resources.loadResource(FileResource.PREFIX + currentFilePath);
        showFileResource(fileResource2);
        Assert.assertEquals(fileResource, fileResource2);
        System.out.println("================FILE end==================");
    }

    private void showFileResource(AbstractLocatableResource fileResource) {
        System.out.println("===========");
        System.out.println("location:" + fileResource.getLocation());
        System.out.println("path:" + fileResource.getPath());
        System.out.println("prefix:"+ fileResource.getPrefix());
        System.out.println("exists:" + fileResource.exists());
        System.out.println("isReadable:" + fileResource.isReadable());
        System.out.println("absolutePath:" + fileResource.getAbsolutePath());
        System.out.println("contentLength:" + fileResource.contentLength());
        System.out.println("toString:" + fileResource.toString());
    }

    @Test
    public void testUrlResource_fileProtocol() {
        System.out.println("================URL FILE Protocol start==================");
        String workDir = SystemPropertys.getUserWorkDir();
        System.out.println(workDir);
        String currentFilePath = workDir + "/src/test/java/" + Reflects.getFQNClassName(ResourceLoadTests.class).replaceAll("\\.", "/") + ".java";
        UrlResource urlResource = Resources.loadUrlResource(URLs.URL_PREFIX_FILE + currentFilePath);
        showFileResource(urlResource);
        UrlResource urlResource2 = Resources.loadResource(URLs.URL_PREFIX_FILE + currentFilePath);
        showFileResource(urlResource2);
        Assert.assertEquals(urlResource, urlResource2);

        System.out.println("================URL FILE Protocol end==================");
    }

    @Test
    public void testUrlResource_jarProtocol() {
        System.out.println("================URL Jar Protocol start==================");
        String workDir = SystemPropertys.getUserWorkDir();
        System.out.println(workDir);
        String currentFilePath = workDir + "/src/test/java/" + Reflects.getPackageName(ResourceLoadTests.class).replaceAll("\\.", "/") + "/sqlhelper-examples-2.2.0.jar" + URLs.JAR_URL_SEPARATOR + "BOOT-INF/lib/langx-java-1.0.0.jar" + URLs.JAR_URL_SEPARATOR + "com/jn/langx/util/collection/Collects.class";
        UrlResource urlResource = Resources.loadUrlResource(URLs.URL_PREFIX_JAR_FILE + currentFilePath);
        showFileResource(urlResource);
        UrlResource urlResource2 = Resources.loadResource(URLs.URL_PREFIX_JAR_FILE + currentFilePath);
        showFileResource(urlResource2);
        Assert.assertEquals(urlResource, urlResource2);
        System.out.println("================URL Jar Protocol end==================");
    }

    @Test
    public void testClassPathResource() {
        System.out.println("================Class Path start==================");
        String currentFilePath = Reflects.getPackageName(ResourceLoadTests.class).replaceAll("\\.", "/") + "/ph-json-9.3.4.jar";
        ClassPathResource cpResource = Resources.loadClassPathResource(ClassPathResource.PREFIX + currentFilePath);
        showFileResource(cpResource);
        ClassPathResource cpResource2 = Resources.loadResource(ClassPathResource.PREFIX + currentFilePath);
        showFileResource(cpResource2);
        Assert.assertEquals(cpResource, cpResource2);
        ClassPathResource cpResource3 = Resources.loadClassPathResource("ph-json-9.3.4.jar", ResourceLoadTests.class);
        showFileResource(cpResource3);
        Assert.assertEquals(cpResource2, cpResource3);

        System.out.println("================Class Path end==================");
    }
}
