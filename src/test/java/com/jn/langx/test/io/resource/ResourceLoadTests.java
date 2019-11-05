package com.jn.langx.test.io.resource;

import com.jn.langx.io.resource.FileResource;
import com.jn.langx.io.resource.Resources;
import com.jn.langx.util.SystemPropertys;
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
        FileResource fileResource2 = Resources.loadResource(FileResource.PATTERN + currentFilePath);
        showFileResource(fileResource2);
        Assert.assertEquals(fileResource, fileResource2);
    }

    private void showFileResource(FileResource fileResource) {
        System.out.println("=======================================");
        System.out.println("path:" + fileResource.getPath());
        System.out.println("exists:" + fileResource.exists());
        System.out.println("isReadable:" + fileResource.isReadable());
        System.out.println("absolutePath:" + fileResource.getAbsolutePath());
        System.out.println("contentLength:" + fileResource.contentLength());
        System.out.println("toString:" + fileResource.toString());
    }
}
