package com.jn.langx.test.io.resource;

import com.jn.langx.io.resource.*;
import com.jn.langx.util.SystemPropertys;
import com.jn.langx.util.URLs;
import com.jn.langx.util.reflect.Reflects;
import org.junit.Assert;
import org.junit.Test;

public class ResourceLoadTests {

    @Test
    public void testFileResource() {
        String workDir = SystemPropertys.getUserWorkDir();
        System.out.println(workDir);
        String currentFilePath = workDir + "/src/test/java/" + Reflects.getFQNClassName(ResourceLoadTests.class).replaceAll("\\.", "/") + ".java";
        FileResource fileResource = Resources.loadFileResource(currentFilePath);
        showFileResource(fileResource);
        FileResource fileResource2 = Resources.loadResource(FileResource.PREFIX + currentFilePath);
        showFileResource(fileResource2);
        Assert.assertEquals(fileResource, fileResource2);
    }

    private void showFileResource(AbstractPathableResource fileResource) {
        System.out.println("=======================================");
        System.out.println("path:" + fileResource.getPath());
        System.out.println("exists:" + fileResource.exists());
        System.out.println("isReadable:" + fileResource.isReadable());
        System.out.println("absolutePath:" + fileResource.getAbsolutePath());
        System.out.println("contentLength:" + fileResource.contentLength());
        System.out.println("toString:" + fileResource.toString());
    }

    @Test
    public void testUrlResource_fileProtocol() {
        String workDir = SystemPropertys.getUserWorkDir();
        System.out.println(workDir);
        String currentFilePath = workDir + "/src/test/java/" + Reflects.getFQNClassName(ResourceLoadTests.class).replaceAll("\\.", "/") + ".java";
        UrlResource urlResource = Resources.loadUrlResource(URLs.URL_PREFIX_FILE + currentFilePath);
        showFileResource(urlResource);
        UrlResource urlResource2 = Resources.loadResource(URLs.URL_PREFIX_FILE + currentFilePath);
        showFileResource(urlResource2);
        Assert.assertEquals(urlResource, urlResource2);
    }

    @Test
    public void testUrlResource_jarProtocol() {
        String workDir = SystemPropertys.getUserWorkDir();
        System.out.println(workDir);
        String currentFilePath = workDir + "/src/test/java/" + Reflects.getPackageName(ResourceLoadTests.class).replaceAll("\\.", "/") + "/ph-json-9.3.4.jar" + URLs.JAR_URL_SEPARATOR + "com/helger/json/JsonArray.class";
        UrlResource urlResource = Resources.loadUrlResource(URLs.URL_PREFIX_JAR_FILE + currentFilePath);
        showFileResource(urlResource);
        UrlResource urlResource2 = Resources.loadResource(URLs.URL_PREFIX_JAR_FILE + currentFilePath);
        showFileResource(urlResource2);
        Assert.assertEquals(urlResource, urlResource2);
    }

    @Test
    public void testClassPathResource() {
        String currentFilePath = Reflects.getPackageName(ResourceLoadTests.class).replaceAll("\\.", "/") + "/ph-json-9.3.4.jar";
        ClassPathResource cpResource = Resources.loadClassPathResource(ClassPathResource.PREFIX + currentFilePath);
        showFileResource(cpResource);
        ClassPathResource cpResource2 = Resources.loadResource(ClassPathResource.PREFIX + currentFilePath);
        showFileResource(cpResource2);
        Assert.assertEquals(cpResource, cpResource2);
    }
}
