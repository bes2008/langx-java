package com.jn.langx.test.classpath;

import com.jn.langx.classpath.Classpath;
import com.jn.langx.classpath.Classpaths;
import com.jn.langx.classpath.cp.ClassClasspath;
import com.jn.langx.classpath.cp.JarFileClasspath;
import com.jn.langx.classpath.cp.WarFileClasspath;
import com.jn.langx.io.resource.AbstractLocatableResource;
import com.jn.langx.io.resource.ClassPathResource;
import com.jn.langx.io.resource.Resource;
import com.jn.langx.io.resource.Resources;
import com.jn.langx.test.io.resource.ResourceLoadTests;
import com.jn.langx.test.util.StringsTests;
import com.jn.langx.test.util.SystemsTests;
import com.jn.langx.util.reflect.Reflects;
import org.junit.Test;

public class ClasspathTests {

    @Test
    public void testClassClasspath() {
        System.out.println("================Class path test: class classpath start==================");
        ClassClasspath classpath = new ClassClasspath(SystemsTests.class);

        Resource resource = classpath.findResource(Classpaths.classNameToPath(Reflects.getFQNClassName(StringsTests.class)));
        resource = classpath.findResource(Classpaths.classNameToPath("StringsTests"));
        showFileResource((AbstractLocatableResource) resource);

        System.out.println("================Class path test: class classpath end==================");
    }

    @Test
    public void testJarFileClasspath() throws Throwable {
        System.out.println("================Class path test: jar file classpath start==================");
        String jarfilePath = Reflects.getPackageName(ResourceLoadTests.class).replaceAll("\\.", "/") + "/ph-json-9.3.4.jar";
        ClassPathResource classPathResource = Resources.loadClassPathResource(jarfilePath);
        Classpath classpath = new JarFileClasspath(classPathResource.getAbsolutePath());
        Resource resource = classpath.findResource(Classpaths.classNameToPath("com.helger.json.IJson"));
        showFileResource((AbstractLocatableResource) resource);
        System.out.println("================Class path test: jar file classpath end==================");
    }

    @Test
    public void testWarFileClasspath() throws Throwable {
        System.out.println("================Class path test: jar file classpath start==================");
        Classpath classpath = new WarFileClasspath("e:/langx-java_test/webgate-console.war");
        Resource resource = classpath.findResource("/config.properties");
        showFileResource((AbstractLocatableResource) resource);
        System.out.println("================Class path test: jar file classpath end==================");
    }



    private void showFileResource(AbstractLocatableResource fileResource) {
        if(fileResource!=null) {
            System.out.println("===========");
            System.out.println("location:" + fileResource.getLocation());
            System.out.println("path:" + fileResource.getPath());
            System.out.println("prefix:" + fileResource.getPrefix());
            System.out.println("exists:" + fileResource.exists());
            System.out.println("isReadable:" + fileResource.isReadable());
            System.out.println("absolutePath:" + fileResource.getAbsolutePath());
            System.out.println("contentLength:" + fileResource.contentLength());
            System.out.println("toString:" + fileResource.toString());
        }
    }

}
