package com.jn.langx.security;

import com.jn.langx.util.Radixs;
import com.jn.langx.util.Throwables;
import com.jn.langx.util.io.IOs;
import com.jn.langx.util.io.file.Files;

import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FileDigestGenerator {
    private boolean lowercase = true;

    public static void main(String[] args) {
        FileDigestGenerator generator = new FileDigestGenerator();
        long strat = System.currentTimeMillis();
        System.out.println("MD5:   " + generator.generate("F:\\迅雷下载\\CentOS-7-x86_64-DVD-1708.iso", "MD5"));
        long t2 = System.currentTimeMillis();
        System.out.println("SHA-1: " + generator.generate("F:\\迅雷下载\\CentOS-7-x86_64-DVD-1708.iso", "SHA-1"));
        long t3 = System.currentTimeMillis();
        System.out.println("MD5 time: " + (t2 - strat));
        System.out.println("SHA-1 time: " + (t3 - t2));
    }

    public String generate(String filePath, String algorithm) {
        try {
            String digest = getFileDigest(filePath, algorithm);
            if (!lowercase) {
                digest = digest.toUpperCase();
            }
            return digest;
        } catch (FileNotFoundException ex) {
            throw Throwables.wrapAsRuntimeException(ex);
        }
    }

    public boolean isLowercase() {
        return lowercase;
    }

    public void setLowercase(boolean lowercase) {
        this.lowercase = lowercase;
    }

    public static String getFileDigest(String filePath, String algorithm) throws FileNotFoundException {
        MessageDigest messageDigest = newDigest(algorithm);
        if (messageDigest == null) {
            throw new FileNotFoundException("Can't find " + algorithm + " algorithm");
        }
        File file = new File(filePath);
        if (!file.exists() || !file.canRead()) {
            throw new FileNotFoundException(" can't find file [" + filePath + "] or it is not readable");
        }
        // step 1 find file
        FileReader reader = FileReaderFactory.getFileReader(filePath);
        try {
            reader.setFile(file);

            while (reader.hasNext()) {
                byte[] bytes = reader.next();
                if (bytes != null && bytes.length > 0) {
                    messageDigest.update(bytes);
                }
            }
            byte[] hashed = messageDigest.digest();
            return Radixs.toHex2(hashed).toUpperCase();
        } finally {
            IOs.close(reader);
        }
    }

    private static MessageDigest newDigest(String algorithm) {
        try {
            if (algorithm == null) {
                algorithm = "MD5";
            }
            return MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }


    public static abstract class FileReader<INPUT extends Closeable> {
        protected INPUT input;
        private File file;
        protected long fileLength;
        protected long readedLength = 0L;

        public void setFile(File file) {
            this.file = file;
            fileLength = file.length();
        }

        public abstract byte[] next();

        public final boolean hasNext() {
            return readedLength < fileLength;
        }

        public final void close() {
            IOs.close(input);
        }

    }

    private static class BufferedFileReader extends FileReader<BufferedInputStream> {
        @Override
        public void setFile(File file) {
            super.setFile(file);
            FileInputStream fileInput = Files.openInputStream(file);
            input = new BufferedInputStream(fileInput);
        }

        @Override
        public byte[] next() {
            int maxLength = 1024;
            byte[] bytes = new byte[maxLength];
            int length = -1;
            try {
                length = input.read(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (length != -1) {
                byte[] ret = null;
                if (length == maxLength) {
                    ret = bytes;
                } else {
                    ret = new byte[length];
                }
                if (length > 0 && length < maxLength) {
                    System.arraycopy(bytes, 0, ret, 0, length);
                }
                return ret;
            } else {
                readedLength = fileLength;
                return new byte[0];
            }
        }

    }

    private static class MMapedFileReader extends FileReader<FileChannel> {
        private MappedByteBuffer currentMMapBuffer;
        private long nextMapStartPosition = 0L;
        private long remainingLength = 0L;

        public void setFile(File file) {
            super.setFile(file);

            FileInputStream fileInput = Files.openInputStream(file);
            input = fileInput.getChannel();
            remainingLength = fileLength;
        }

        @Override
        public byte[] next() {
            if (currentMMapBuffer == null || currentMMapBuffer.remaining() <= 0) {
                mapNextFilePart();
            }
            if (currentMMapBuffer.remaining() > 0) {
                int maxLength = 1024 * 1024;
                byte[] bytes = new byte[Math.min(currentMMapBuffer.remaining(), maxLength)];
                currentMMapBuffer.get(bytes);
                remainingLength = remainingLength - bytes.length;
                readedLength = readedLength + bytes.length;
                return bytes;
            } else {
                return new byte[0];
            }
        }

        private int mapNextFilePart() {
            int mmapsize = 0;
            if (remainingLength >= Integer.MAX_VALUE) {
                mmapsize = Integer.MAX_VALUE;
            } else {
                mmapsize = Integer.parseInt(remainingLength + "");
            }

            try {
                currentMMapBuffer = input.map(FileChannel.MapMode.READ_ONLY, nextMapStartPosition, mmapsize);
            } catch (IOException e) {
                e.printStackTrace();
            }
            nextMapStartPosition = nextMapStartPosition + mmapsize;
            return mmapsize;
        }
    }

    static class FileReaderFactory {
        private static final long SIZE_50M = 50 * 1024 * 1024;

        public static FileReader getFileReader(String filePath) {
            File file = new File(filePath);

            if (file.length() > SIZE_50M) {
                return new MMapedFileReader();
            } else {
                return new BufferedFileReader();
            }

        }
    }
}
