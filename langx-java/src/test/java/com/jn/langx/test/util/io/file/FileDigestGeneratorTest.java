package com.jn.langx.test.util.io.file;

import com.jn.langx.security.crypto.JCAEStandardName;
import com.jn.langx.security.crypto.digest.FileDigestGenerator;
import com.jn.langx.util.Strings;
import com.jn.langx.util.SystemPropertys;
import com.jn.langx.util.io.file.Files;
import com.jn.langx.util.reflect.Reflects;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;

public class FileDigestGeneratorTest {
    @Test
    public void test() throws FileNotFoundException {
        String workdir = SystemPropertys.getUserWorkDir();
        File file = Files.newFile(workdir, "src/test/java/", Strings.replace(Reflects.getFQNClassName(FilepathValidateTest.class), ".", "/")+".java");
        if (file.exists()) {
            String digest = FileDigestGenerator.getFileDigest(file.getPath(), JCAEStandardName.MD5.getName(), null);
            System.out.println(digest);
        }
    }
}
