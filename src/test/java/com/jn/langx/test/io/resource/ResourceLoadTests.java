package com.jn.langx.test.io.resource;

import com.jn.langx.io.resource.FileResource;
import com.jn.langx.io.resource.Resource;
import com.jn.langx.io.resource.Resources;
import com.jn.langx.util.SystemPropertys;
import com.jn.langx.util.reflect.Reflects;
import org.junit.Test;

import java.io.File;

public class ResourceLoadTests {
    @Test
    public void testFileResource() {
        String workDir = SystemPropertys.getUserWorkDir();
        System.out.println(workDir);
        String currentFilePath = FileResource.PATTERN + workDir + "/src/test/java/" + Reflects.getFQNClassName(ResourceLoadTests.class).replaceAll("\\.", "/") + ".java";
        Resource<File> fileResource = Resources.loadResource(currentFilePath);
        System.out.println(fileResource);
    }
}
