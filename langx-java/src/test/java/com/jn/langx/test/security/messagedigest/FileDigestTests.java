package com.jn.langx.test.security.messagedigest;

import com.jn.langx.security.crypto.digest.FileDigestGenerator;
import org.junit.Test;

public class FileDigestTests {
    @Test
    public void test(){
        System.out.println(new FileDigestGenerator().generate("E:/迅雷下载/AILINK-OPS-1.1.5208.013.T002.tar.gz","MD5"));
    }
    @Test
    public void test2(){
        FileDigestGenerator generator = new FileDigestGenerator();
        long start = System.currentTimeMillis();
        System.out.println("MD5, SHA-1:   " + generator.generate("D:\\mvn_repo\\org\\springframework.zip", null, "MD5", "SHA-1"));
        long t2 = System.currentTimeMillis();
        System.out.println("SHA-1: " + generator.generate("D:\\mvn_repo\\org\\springframework.zip", "SHA-1"));
        long t3 = System.currentTimeMillis();
        System.out.println("MD5 time: " + (t2 - start));
        System.out.println("SHA-1 time: " + (t3 - t2));
    }
}
