package com.jn.langx.test.util.io.file;

import com.jn.langx.io.resource.Resource;
import com.jn.langx.io.resource.Resources;
import com.jn.langx.util.io.file.FileTypes;
import org.junit.Test;

import java.io.File;

public class FileTypesTests {
    @Test
    public void test() throws Throwable {
        try {
            Resource r = Resources.loadClassPathResource("FileTypesTests.class", FileTypesTests.class);
            String h = FileTypes.readFileMagic(r.getInputStream());
            System.out.println(FileTypes.getType(h));
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void test2() throws Throwable {
        try {
            File file = new File("D:/water_img.png");
            String h = FileTypes.readFileMagic(file);
            System.out.println(FileTypes.getType(h));
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }
}
