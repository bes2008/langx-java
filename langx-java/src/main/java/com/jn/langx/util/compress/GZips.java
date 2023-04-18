package com.jn.langx.util.compress;


import com.jn.langx.util.function.Supplier0;
import com.jn.langx.util.io.IOs;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * @author jinuo.fang
 */
public class GZips {


    /**
     * Tests if an inputStream is GZipped.
     * <p>
     * <b>Note:</b> the stream is not closed and is reset.
     *
     * @param inputStream inputStream to test
     * @return {@code true} if inputStream is gzipped, {@code false} otherwise
     * @throws IOException if any io errors while reading inputStream
     * @since 3.0
     */
    public static boolean isGzipStream(InputStream inputStream) throws IOException {

        inputStream.mark(2);
        // read header to see if is compressed file
        int b = inputStream.read();
        // redundant cast : int magic = ((int) in.read() << 8) | b;
        int magic = inputStream.read() << 8 | b;
        inputStream.reset();
        return magic == GZIPInputStream.GZIP_MAGIC;

    }

    /**
     * @return Retourne la string decompressee
     */
    public static StringBuffer bytesToStringBuffer(byte[] in) {
        return bytesToStringBuffer(in, new Supplier0<StringBuffer>() {
            @Override
            public StringBuffer get() {
                return new StringBuffer();
            }
        });
    }

    /**
     * @return Retourne la string decompressee
     */
    public static StringBuilder bytesToStringBuilder(byte[] in) {
        return bytesToStringBuffer(in, new Supplier0<StringBuilder>() {
            @Override
            public StringBuilder get() {
                return new StringBuilder();
            }
        });
    }

    /**
     * @return Retourne la string decompressee
     */
    private static <T extends Appendable> T bytesToStringBuffer(byte[] in, Supplier0<T> supplier) {
        T sb = supplier.get();
        if (in == null || in.length == 0) {
            return sb;
        }
        GZIPInputStream gz = null;
        try {
            gz = new GZIPInputStream(new BufferedInputStream(new ByteArrayInputStream(in)));
            int c;
            while ((c = gz.read()) != -1) {
                sb.append((char) c);
            }
        } catch (IOException e) {
            throw new RuntimeException("Can't decompress", e);
        } finally {
            IOs.close(gz);
        }
        return sb;

    }

    /**
     * @return la string decompressee
     */
    public static String bytesToString(byte[] in) {
        return bytesToStringBuffer(in).toString();
    }

    /**
     * @return Retourne la string compressee
     */
    public static byte[] stringBufferToBytes(StringBuffer elem) {
        return stringToBytes(elem.toString());
    }

    /**
     * @return Retourne la string compressee
     */
    public static byte[] stringToBytes(String elem) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            GZIPOutputStream gz = new GZIPOutputStream(new BufferedOutputStream(baos));
            Reader sr = new BufferedReader(new StringReader(elem));
            int c;
            while ((c = sr.read()) != -1) {
                gz.write((char) c);
            }
            gz.close();

            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Can't compress", e);
        }
    }

    private GZips(){

    }

}
