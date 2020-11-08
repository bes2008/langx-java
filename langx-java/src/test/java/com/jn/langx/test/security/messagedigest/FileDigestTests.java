package com.jn.langx.test.security.messagedigest;

import com.jn.langx.security.FileDigestGenerator;
import org.junit.Test;

public class FileDigestTests {
    @Test
    public void test(){
        System.out.println(new FileDigestGenerator().generate("E:/迅雷下载/AILINK-OPS-1.1.5208.012.T002.tar.gz","MD5"));
    }
}
