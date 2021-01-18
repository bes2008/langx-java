package com.jn.langx.test.buffer;

import com.jn.langx.io.buffer.BigByteBuffer;
import com.jn.langx.io.resource.Resource;
import com.jn.langx.io.resource.Resources;
import com.jn.langx.util.DataSize;
import com.jn.langx.util.SystemPropertys;
import com.jn.langx.util.io.IOs;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class BigBufferTests {
    @Test
    public void test() throws IOException {

        String userdir = SystemPropertys.getUserWorkDir();
        System.out.println(userdir);
        Resource res = Resources.loadFileResource(new File(userdir + "/src/main/java/com/jn/langx/util/collection/Collects.java"));

        byte[] bytes = IOs.readFully(res.getInputStream(), (int) res.contentLength());
        BigByteBuffer buffer = new BigByteBuffer(bytes, Integer.MAX_VALUE, DataSize.kb(4).toInt());

        buffer.flip();

        buffer.rewind();
        printWithIndex(buffer);
        System.out.println("\n=====================");

        buffer.rewind();
        print(buffer);
        System.out.println("\n=====================");
    }

    private void printWithIndex(BigByteBuffer buffer) {
        int k = -1;

        int lineSize = 64;
        for (int i = 0; i < lineSize; i++) {
            k++;
            if (k >= buffer.limit()) {
                break;
            }
            System.out.print(buffer.get(k));
            if (k % lineSize == lineSize - 1) {
                i = -1;
                System.out.println();
            }
        }
    }

    private void print(BigByteBuffer buffer) {

        int lineSize = 64;
        for (int i = 0; i < lineSize; i++) {
            if (buffer.hasRemaining()) {
                System.out.print(buffer.get());
                if (i % lineSize == lineSize - 1) {
                    i = -1;
                    System.out.println();
                }
            }
        }
    }
}
