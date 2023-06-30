package com.jn.langx.test.util.io.file;

import com.jn.langx.util.io.file.Files;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class TempFileTests {
    @Test
    public void testCreateTempFile() throws IOException {
        File file = Files.createTempFile(null, null, null);
        if(file.exists()){
            file.deleteOnExit();
        }
    }

    @Test
    public void testCreateTempDirectory() throws IOException {
        File file = Files.createTempDirectory(null, null, null);
        if(file.exists()){
            file.deleteOnExit();
        }
    }
}
