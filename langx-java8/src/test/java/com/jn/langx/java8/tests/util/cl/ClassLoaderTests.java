package com.jn.langx.java8.tests.util.cl;

import org.junit.Test;

import java.net.URL;
import java.net.URLClassLoader;

public class ClassLoaderTests {
    @Test
    public void test() {
        URLClassLoader cl = new CL2(new URL[0], ClassLoader.getSystemClassLoader());
        System.out.println(cl.getURLs().length);
    }

    static class CL1 extends URLClassLoader {
        /*
        static {
            ClassLoader.registerAsParallelCapable();
        }
        */
        public CL1(URL[] urls, ClassLoader parent) {
            super(urls, parent);
        }
    }
    static class CL2 extends CL1 {
        static {
            ClassLoader.registerAsParallelCapable();
        }

        public CL2(URL[] urls, ClassLoader parent) {
            super(urls, parent);
        }
    }

}
