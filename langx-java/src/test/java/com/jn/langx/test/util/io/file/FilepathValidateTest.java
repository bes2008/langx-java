package com.jn.langx.test.util.io.file;

import com.jn.langx.util.io.file.Filenames;
import org.junit.Assert;
import org.junit.Test;

public class FilepathValidateTest {
    @Test
    public void test(){
        Assert.assertTrue(Filenames.checkFilePath("E:\\Program Files (x86)\\SogouWBInput"));
        Filenames.checkFilePath("E:\\Program Files (x86)\\SogouWBInput/hello");
    }
}
