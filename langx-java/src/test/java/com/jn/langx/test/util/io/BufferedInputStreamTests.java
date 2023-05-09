package com.jn.langx.test.util.io;

import com.jn.langx.io.resource.FileResource;
import com.jn.langx.io.resource.Resource;
import com.jn.langx.io.resource.Resources;
import com.jn.langx.io.stream.*;
import com.jn.langx.util.SystemPropertys;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Consumer;
import com.jn.langx.util.io.Charsets;
import com.jn.langx.util.io.IOs;
import com.jn.langx.util.io.LineDelimiter;
import com.jn.langx.util.progress.ProgressSource;
import com.jn.langx.util.reflect.Reflects;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.util.Iterator;

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
                if(length>0) {
                    System.out.print(new String(bytes, 0, length, Charsets.UTF_8.name()));
                }
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
            ByteBuffer bf;
            while (true) {
                bf = bin.readAsByteBuffer();
                if (bf.hasRemaining()) {
                    byte[] bytes = new byte[8192];
                    int positionBeforeRead = bf.position();
                    while (bf.hasRemaining()) {
                        bf.get(bytes, 0, bf.remaining());
                        int endPosition = bf.position();
                        int length = endPosition - positionBeforeRead;
                        positionBeforeRead = endPosition;
                        if(length>0) {
                            System.out.print(new String(bytes, 0, length, Charsets.UTF_8.name()));
                        }
                    }
                } else {
                    break;
                }
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            IOs.close(bin);
        }

    }

    @Test
    public void test3() {
        FileResource file = getCurrentFileResource();
        BufferedInputStream bin = null;
        try {
            bin = new BufferedInputStream(file.getInputStream());
            DelimiterBasedReadableByteChannel channel = new DelimiterBasedReadableByteChannel(Channels.newChannel(bin), "\n");
            for (byte[] bytes : channel) {
                System.out.println(new String(bytes, 0, bytes.length, Charsets.UTF_8.name()));
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            IOs.close(bin);
        }

    }

    @Test
    public void test4() {
        FileResource file = getCurrentFileResource();
        BufferedInputStream bin = null;
        try {
            bin = new BufferedInputStream(file.getInputStream());
            DelimiterBasedReadableByteChannel channel = new DelimiterBasedReadableByteChannel(Channels.newChannel(bin), ";");
            Iterator<byte[]> iter = channel.iterator();
            int i =0;
            while (iter.hasNext()) {
                if(i>0){
                    System.out.print(";");
                }

                byte[] bytes = iter.next();
                System.out.print(new String(bytes,0,bytes.length, Charsets.UTF_8.name()));
                i++;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            IOs.close(bin);
        }

    }

    @Test
    public void test5() {
        FileResource file = getCurrentFileResource();
        BufferedInputStream bin = null;
        try {
            bin = new BufferedInputStream(file.getInputStream());

            DelimiterBasedReadableByteChannel channel = new DelimiterBasedReadableByteChannel(Channels.newChannel(bin), LineDelimiter.DEFAULT.getValue());
            Iterator<byte[]> iter = channel.iterator();
            int i =0;
            while (iter.hasNext()) {
                if(i>0){
                    System.out.print(LineDelimiter.DEFAULT.getValue());
                }

                byte[] bytes = iter.next();
                System.out.print(new String(bytes,0,bytes.length, Charsets.UTF_8.name()));
                i++;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            IOs.close(bin);
        }

    }


    @Test
    public void test6() throws IOException{
        FileResource file = getCurrentFileResource();
        InputStream fileInputStream = file.getInputStream();
        ProgressSource progressSource = new ProgressSource(file.getPath(), file.getRealResource().length());
        ProgressTracedInputStream inputStream = new ProgressTracedInputStream(fileInputStream, progressSource);
        progressSource.start();
        String str = IOs.readAsString(inputStream);
        progressSource.finish();

        System.out.println(progressSource);
        System.out.println("===========================================");

        byte[] bytes = str.getBytes(Charsets.UTF_8);
        ProgressSource ps2 = new ProgressSource(file.getPath(), bytes.length);
        OutputStream out = new ProgressTracedOutputStream(System.out, ps2);
        IOs.write(bytes,out);
        ps2.finished();

        System.out.println(progressSource);
    }

    public FileResource getCurrentFileResource() {
        String workDir = SystemPropertys.getUserWorkDir();
        System.out.println(workDir);
        String currentFilePath = workDir + "/src/main/java/" + Reflects.getFQNClassName(Collects.class).replaceAll("\\.", "/") + ".java";
        return Resources.loadFileResource(currentFilePath);
    }


    @Test
    public void test001() {
        Resource res = Resources.loadClassPathResource("00DE8E3E-95E3-4885-A760-A269A6836AE2.txt", BufferedInputStreamTests.class);
        Resources.readLines(res, Charsets.UTF_8, new Consumer<String>() {
            @Override
            public void accept(String line) {
                System.out.println(line);
            }
        });
    }
}
