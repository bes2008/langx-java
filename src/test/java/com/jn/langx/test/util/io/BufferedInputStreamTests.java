package com.jn.langx.test.util.io;

import com.jn.langx.io.resource.FileResource;
import com.jn.langx.io.resource.Resources;
import com.jn.langx.util.SystemPropertys;
import com.jn.langx.util.io.BufferedInputStream;
import com.jn.langx.util.io.IOs;
import com.jn.langx.util.reflect.Reflects;
import org.junit.Test;

import java.io.IOException;
import java.nio.ByteBuffer;

public class BufferedInputStreamTests {
    @Test
    public void test() {
        FileResource file = getCurrentFileResource();
        BufferedInputStream bin = null;
        try {
            bin = new BufferedInputStream(file.getInputStream());
            byte[] bytes = new byte[8192];
            int length = bytes.length;
            while ((length = bin.read(bytes, 0, bytes.length)) != -1) {
                System.out.println(new String(bytes, 0, length));
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            IOs.close(bin);
        }

    }

    @Test
    public void test2() {
        FileResource file = getCurrentFileResource();
        BufferedInputStream bin = null;
        try {
            bin = new BufferedInputStream(file.getInputStream());
            ByteBuffer bf = bin.readAsByteBuffer();
            byte[] bytes = new byte[8192];
            int positionBeforeRead = bf.position();
            while (bf.hasRemaining()) {
                bf.get(bytes,0, bf.remaining());
                int endPosition = bf.position();
                int length = endPosition - positionBeforeRead;
                positionBeforeRead = endPosition;
                System.out.println(new String(bytes, 0, length));
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            IOs.close(bin);
        }

    }


    public FileResource getCurrentFileResource() {
        String workDir = SystemPropertys.getUserWorkDir();
        System.out.println(workDir);
        String currentFilePath = workDir + "/src/test/java/" + Reflects.getFQNClassName(getClass()).replaceAll("\\.", "/") + ".java";
        return Resources.loadFileResource(currentFilePath);
    }
}
