package com.jn.langx.security.crypto.digest;

import com.jn.langx.annotation.Nullable;
import com.jn.langx.codec.hex.Hex;
import com.jn.langx.util.Emptys;
import com.jn.langx.util.Throwables;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.function.Consumer2;
import com.jn.langx.util.function.Function;
import com.jn.langx.util.io.IOs;
import com.jn.langx.util.io.file.Files;
import com.jn.langx.util.logging.Loggers;
import com.jn.langx.util.struct.Holder;

import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.util.Iterator;
import java.util.List;

public class FileDigestGenerator {
    private boolean lowercase = true;

    public String generate(String filePath, String algorithm) {
        return generate(filePath, null, algorithm);
    }

    public String generate(String filePath, FileReader reader, String algorithm) {
        return generate(filePath, reader, new String[]{algorithm}).get(0);
    }


    public List<String> generate(String filePath, FileReader reader, String... algorithms) {
        try {
            List<String> digests = getFileDigestStrings(filePath, reader, algorithms);
            if (!lowercase) {
                return Pipeline.of(digests).map(new Function<String, String>() {
                    @Override
                    public String apply(String input) {
                        return input.toUpperCase();
                    }
                }).asList();
            }
            return digests;
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

    public static String getFileDigest(String filePath, String algorithm, @Nullable FileReader reader) throws FileNotFoundException {
        List<byte[]> hashBytesList = getFileDigest(filePath, reader, algorithm);
        if (Emptys.isEmpty(hashBytesList)) {
            throw new IllegalArgumentException();
        }
        return Hex.encodeHexString(hashBytesList.get(0));
    }

    public static List<String> getFileDigestStrings(String filePath, @Nullable FileReader reader, String... algorithms) throws FileNotFoundException {
        return Pipeline.of(getFileDigest(filePath, reader, Pipeline.of(algorithms).map(new Function<String, MessageDigest>() {
            @Override
            public MessageDigest apply(String algorithm) {
                return MessageDigests.newDigest(algorithm);
            }
        }).asList())).map(new Function<byte[], String>() {
            @Override
            public String apply(byte[] bytes) {
                return Hex.encodeHexString(bytes);
            }
        }).asList();
    }

    public static List<byte[]> getFileDigest(String filePath, @Nullable FileReader reader, String... algorithms) throws FileNotFoundException {
        return getFileDigest(filePath, reader, Pipeline.of(algorithms).map(new Function<String, MessageDigest>() {
            @Override
            public MessageDigest apply(String algorithm) {
                return MessageDigests.newDigest(algorithm);
            }
        }).asList());
    }

    public static List<String> getFileDigestStrings(String filePath, @Nullable FileReader reader, MessageDigest... messageDigests) throws FileNotFoundException {
        return Pipeline.of(getFileDigest(filePath, reader, Collects.asList(messageDigests))).map(new Function<byte[], String>() {
            @Override
            public String apply(byte[] bytes) {
                return Hex.encodeHexString(bytes);
            }
        }).asList();
    }

    public static List<byte[]> getFileDigest(String filePath, @Nullable FileReader reader, MessageDigest... messageDigests) throws FileNotFoundException {
        return getFileDigest(filePath, reader, Collects.asList(messageDigests));
    }

    public static List<byte[]> getFileDigest(String filePath, @Nullable FileReader reader, List<MessageDigest> messageDigests) throws FileNotFoundException {
        if (Emptys.isEmpty(messageDigests)) {
            return Collects.newArrayList();
        }

        File file = new File(filePath);
        if (!file.exists() || !file.canRead()) {
            throw new FileNotFoundException(" can't find file [" + filePath + "] or it is not readable");
        }
        // step 1 find file
        reader = reader == null ? FileReaderFactory.getFileReader(filePath) : reader;
        try {
            reader.setFile(file);

            while (reader.hasNext()) {
                final Holder<byte[]> bytes = new Holder<byte[]>(reader.next());
                if (!bytes.isEmpty()) {
                    Collects.forEach(messageDigests, new Consumer2<Integer, MessageDigest>() {
                        @Override
                        public void accept(Integer index, MessageDigest messageDigest) {
                            messageDigest.update(bytes.get());
                        }
                    });
                }
            }
            return Pipeline.of(messageDigests).map(new Function<MessageDigest, byte[]>() {
                @Override
                public byte[] apply(MessageDigest messageDigest) {
                    return messageDigest.digest();
                }
            }).asList();

        } finally {
            IOs.close(reader);
        }
    }

    public static String getFileDigest(String filePath, String algorithm) throws FileNotFoundException {
        return getFileDigest(filePath, algorithm, null);
    }


    public static abstract class FileReader<INPUT extends Closeable> implements Iterator<byte[]>, Iterable<byte[]>, Closeable {
        protected INPUT input;
        protected long fileLength;
        protected long readedLength = 0L;

        public void setFile(File file) {
            fileLength = file.length();
        }

        public final boolean hasNext() {
            boolean hasNext = input != null && readedLength < fileLength;
            if (!hasNext) {
                close();
            }
            return hasNext;
        }

        public final void close() {
            IOs.close(input);
        }

        @Override
        public abstract byte[] next();

        @Override
        public Iterator<byte[]> iterator() {
            return this;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public static class BufferedFileReader extends FileReader<BufferedInputStream> {
        @Override
        public void setFile(File file) {
            FileInputStream fileInput = Files.openInputStream(file);
            if (fileInput != null) {
                input = new BufferedInputStream(fileInput);
            }
            super.setFile(file);
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
                byte[] ret;
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

    public static class MMapedFileReader extends FileReader<FileChannel> {
        private MappedByteBuffer currentMMapBuffer;
        private long nextMapStartPosition = 0L;
        private long remainingLength = 0L;

        @Override
        public void setFile(File file) {
            super.setFile(file);

            FileInputStream fileInput = Files.openInputStream(file);
            if (fileInput != null) {
                input = fileInput.getChannel();
            }
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
            int mmapsize;
            if (remainingLength >= Integer.MAX_VALUE) {
                mmapsize = Integer.MAX_VALUE;
            } else {
                mmapsize = Integer.parseInt(remainingLength + "");
            }

            try {
                currentMMapBuffer = input.map(FileChannel.MapMode.READ_ONLY, nextMapStartPosition, mmapsize);
            } catch (IOException e) {
                Loggers.getLogger(FileDigestGenerator.class).error(e.getMessage(), e);
            }
            nextMapStartPosition = nextMapStartPosition + mmapsize;
            return mmapsize;
        }
    }

    static class FileReaderFactory {
        private static final long SIZE_10M = 10 * 1024 * 1024;

        private FileReaderFactory() {

        }

        public static FileReader getFileReader(String filePath) {
            File file = new File(filePath);

            if (file.length() > SIZE_10M) {
                return new MMapedFileReader();
            } else {
                return new BufferedFileReader();
            }

        }
    }
}
