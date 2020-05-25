package com.jn.langx.test.classpath;

import com.jn.langx.classpath.Classpath;
import com.jn.langx.classpath.cp.ClassClasspath;
import com.jn.langx.classpath.cp.JarFileClasspath;
import com.jn.langx.io.resource.AbstractLocatableResource;
import com.jn.langx.io.resource.ClassPathResource;
import com.jn.langx.io.resource.Resource;
import com.jn.langx.io.resource.Resources;
import com.jn.langx.test.io.resource.ResourceLoadTests;
import com.jn.langx.test.util.StringsTests;
import com.jn.langx.test.util.SystemsTests;
import com.jn.langx.util.reflect.Reflects;
import org.junit.Test;

import java.io.File;
import java.net.URL;

public class ClasspathTests {

    @Test
    public void testClassClasspath() {
        ClassClasspath classpath = new ClassClasspath(SystemsTests.class);

        Resource resource = classpath.findResource(Reflects.getFQNClassName(StringsTests.class), true);
        resource = classpath.findResource("StringsTests", true);
        System.out.println(resource);
    }

    @Test
    public void testJarFileClasspath() throws Throwable {
        System.out.println("================Class Path start==================");
        String jarfilePath = Reflects.getPackageName(ResourceLoadTests.class).replaceAll("\\.", "/") + "/ph-json-9.3.4.jar";
        ClassPathResource classPathResource = Resources.loadClassPathResource(jarfilePath);
        Classpath classpath = new JarFileClasspath(classPathResource.getAbsolutePath());
        Resource resource = classpath.findResource("com.helger.json.IJson", true);
        showFileResource((AbstractLocatableResource)resource);
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

}
