package com.jn.langx.test.util.io.file;

import com.jn.langx.io.resource.Resource;
import com.jn.langx.io.resource.Resources;
import com.jn.langx.util.io.IOs;
import com.jn.langx.util.io.file.FileTypes;
import com.jn.langx.util.io.file.Files;
import org.junit.Test;

import java.io.File;
import java.io.InputStream;

public class FileTypesTests {
    @Test
    public void test() throws Throwable {
        InputStream in = null;
        try {
            Resource r = Resources.loadClassPathResource("FileTypesTests.class", FileTypesTests.class);
            in =  r.getInputStream();
            String h = FileTypes.readFileMagic(in);
            System.out.println(FileTypes.getType(h));
        } catch (Throwable ex) {
            ex.printStackTrace();
        }finally {
            IOs.close(in);
        }
    }

    @Test
    public void test2() throws Throwable {
        try {
            File file = Files.newFile("D:/water_img.png");
            String h = FileTypes.readFileMagic(file);
            System.out.println(FileTypes.getType(h));
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }
}
