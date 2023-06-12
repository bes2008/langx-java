package com.jn.langx.util.io;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.io.stream.ByteArrayOutputStream;
import com.jn.langx.io.stream.StringBuilderWriter;
import com.jn.langx.util.*;
import com.jn.langx.util.io.close.ObjectCloser;
import com.jn.langx.util.logging.Loggers;
import com.jn.langx.util.net.URLs;

import java.io.*;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author jinuo.fang
 */
public class IOs {

    /**
     * LF char.
     *
     * @since 5.1.6
     */
    public static final int LF = '\n';


    /**
     * CR char.
     *
     * @since 5.1.6
     */
    public static final int CR = '\r';

    public static final int EOF = -1;
    public static final String LINE_SEPARATOR = LineDelimiter.DEFAULT.getValue();
    /**
     * The default buffer size ({@value}) to use for
     * {@link #copyLarge(InputStream, OutputStream)}
     */
    public static final int DEFAULT_BUFFER_SIZE = 1024 * 4;

    /**
     * The default buffer size to use for the skip() methods.
     */
    private static final int SKIP_BUFFER_SIZE = 2048;


    // Allocated in the relevant skip method if necessary.
    /*
     * These buffers are static and are shared between threads.
     * This is possible because the buffers are write-only - the contents are never read.
     *
     * N.B. there is no need to synchronize when creating these because:
     * - we don't care if the buffer is created multiple times (the data is ignored)
     * - we always use the same size buffer, so if it it is recreated it will still be OK
     * (if the buffer size were variable, we would need to synch. to ensure some other thread
     * did not create a smaller one)
     */
    private static char[] SKIP_CHAR_BUFFER;
    private static byte[] SKIP_BYTE_BUFFER;

    public static void close(final Closeable target) {
        if (target == null) {
            return;
        }
        try {
            target.close();
        } catch (Throwable e) {
            Loggers.getLogger(ObjectCloser.class).warn("close fail: {}", target);
        }
    }

    public static void close(final Object target) {
        if (target == null) {
            return;
        }
        if (target instanceof Closeable) {
            try {
                ((Closeable) target).close();
            } catch (Exception e) {
                Loggers.getLogger(ObjectCloser.class).warn("close fail: {}", target);
            }
        } else {
            ObjectCloser.close(target);
        }
    }

    public static String readAsString(Reader reader) throws IOException {
        StringBuilder stringBuilder = new StringBuilder(256);
        char[] chars = new char[1024];
        int length;
        while ((length = reader.read(chars)) != -1) {
            stringBuilder.append(chars, 0, length);
        }
        return stringBuilder.toString();
    }

    public static String readAsString(InputStream reader) throws IOException {
        StringBuilder stringBuilder = new StringBuilder(256);
        byte[] bytes = new byte[1024];
        int length;
        while ((length = reader.read(bytes)) != -1) {
            stringBuilder.append(new String(bytes, 0, length, Charsets.UTF_8));
        }
        return stringBuilder.toString();
    }

    /**
     * Copies bytes from a large (over 2GB) <code>InputStream</code> to an
     * <code>OutputStream</code>.
     * <p>
     * This method buffers the input internally, so there is no need to use a
     * <code>BufferedInputStream</code>.
     * <p>
     * The buffer size is given by {@link #DEFAULT_BUFFER_SIZE}.
     *
     * @param input  the <code>InputStream</code> to read from
     * @param output the <code>OutputStream</code> to write to
     * @return the number of bytes copied
     * @throws NullPointerException if the input or output is null
     * @throws IOException          if an I/O error occurs
     */
    public static long copyLarge(final InputStream input, final OutputStream output)
            throws IOException {
        return copy(input, output, DEFAULT_BUFFER_SIZE);
    }


    /**
     * Copies bytes from an <code>InputStream</code> to an
     * <code>OutputStream</code>.
     * <p>
     * This method buffers the input internally, so there is no need to use a
     * <code>BufferedInputStream</code>.
     * <p>
     * Large streams (over 2GB) will return a bytes copied value of
     * <code>-1</code> after the copy has completed since the correct
     * number of bytes cannot be returned as an int. For large streams
     * use the <code>copyLarge(InputStream, OutputStream)</code> method.
     *
     * @param input  the <code>InputStream</code> to read from
     * @param output the <code>OutputStream</code> to write to
     * @return the number of bytes copied, or -1 if &gt; Integer.MAX_VALUE
     * @throws NullPointerException if the input or output is null
     * @throws IOException          if an I/O error occurs
     */
    public static int copy(final InputStream input, final OutputStream output) throws IOException {
        final long count = copyLarge(input, output);
        if (count > Integer.MAX_VALUE) {
            return -1;
        }
        return (int) count;
    }

    /**
     * Copies bytes from an <code>InputStream</code> to an <code>OutputStream</code> using an internal buffer of the
     * given size.
     * <p>
     * This method buffers the input internally, so there is no need to use a <code>BufferedInputStream</code>.
     * <p>
     *
     * @param input      the <code>InputStream</code> to read from
     * @param output     the <code>OutputStream</code> to write to
     * @param bufferSize the bufferSize used to copy from the input to the output
     * @return the number of bytes copied
     * @throws NullPointerException if the input or output is null
     * @throws IOException          if an I/O error occurs
     */
    public static long copy(final InputStream input, final OutputStream output, final int bufferSize)
            throws IOException {
        return copyLarge(input, output, new byte[bufferSize]);
    }

    /**
     * Copies bytes from a large (over 2GB) <code>InputStream</code> to an
     * <code>OutputStream</code>.
     * <p>
     * This method uses the provided buffer, so there is no need to use a
     * <code>BufferedInputStream</code>.
     * <p>
     *
     * @param input  the <code>InputStream</code> to read from
     * @param output the <code>OutputStream</code> to write to
     * @param buffer the buffer to use for the copy
     * @return the number of bytes copied
     * @throws NullPointerException if the input or output is null
     * @throws IOException          if an I/O error occurs
     */
    public static long copyLarge(final InputStream input, final OutputStream output, final byte[] buffer)
            throws IOException {
        long count = 0;
        int n;
        while (EOF != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }


    /**
     * Fetches entire contents of an <code>InputStream</code> and represent
     * same data as result InputStream.
     * <p>
     * This method is useful where,
     * <ul>
     * <li>Source InputStream is slow.</li>
     * <li>It has network resources associated, so we cannot keep it open for
     * long time.</li>
     * <li>It has network timeout associated.</li>
     * </ul>
     * It can be used in favor of {@link #toByteArray(InputStream)}, since it
     * avoids unnecessary allocation and copy of byte[].<br>
     * This method buffers the input internally, so there is no need to use a
     * <code>BufferedInputStream</code>.
     *
     * @param input Stream to be fully buffered.
     * @return A fully buffered stream.
     * @throws IOException if an I/O error occurs
     */
    public static InputStream toBufferedInputStream(final InputStream input) throws IOException {
        return com.jn.langx.io.stream.ByteArrayOutputStream.toBufferedInputStream(input);
    }

    /**
     * Fetches entire contents of an <code>InputStream</code> and represent
     * same data as result InputStream.
     * <p>
     * This method is useful where,
     * <ul>
     * <li>Source InputStream is slow.</li>
     * <li>It has network resources associated, so we cannot keep it open for
     * long time.</li>
     * <li>It has network timeout associated.</li>
     * </ul>
     * It can be used in favor of {@link #toByteArray(InputStream)}, since it
     * avoids unnecessary allocation and copy of byte[].<br>
     * This method buffers the input internally, so there is no need to use a
     * <code>BufferedInputStream</code>.
     *
     * @param input Stream to be fully buffered.
     * @param size  the initial buffer size
     * @return A fully buffered stream.
     * @throws IOException if an I/O error occurs
     */
    public static InputStream toBufferedInputStream(final InputStream input, final int size) throws IOException {
        return com.jn.langx.io.stream.ByteArrayOutputStream.toBufferedInputStream(input, size);
    }

    /**
     * Returns the given reader if it is a {@link BufferedReader}, otherwise creates a BufferedReader from the given
     * reader.
     *
     * @param reader the reader to wrap or return (not null)
     * @return the given reader or a new {@link BufferedReader} for the given reader
     * @throws NullPointerException if the input parameter is null
     * @see #buffer(Reader)
     */
    public static BufferedReader toBufferedReader(final Reader reader) {
        return reader instanceof BufferedReader ? (BufferedReader) reader : new BufferedReader(reader);
    }

    /**
     * Returns the given reader if it is a {@link BufferedReader}, otherwise creates a BufferedReader from the given
     * reader.
     *
     * @param reader the reader to wrap or return (not null)
     * @param size   the buffer size, if a new BufferedReader is created.
     * @return the given reader or a new {@link BufferedReader} for the given reader
     * @throws NullPointerException if the input parameter is null
     * @see #buffer(Reader)
     */
    public static BufferedReader toBufferedReader(final Reader reader, final int size) {
        return reader instanceof BufferedReader ? (BufferedReader) reader : new BufferedReader(reader, size);
    }

    /**
     * Returns the given reader if it is already a {@link BufferedReader}, otherwise creates a BufferedReader from
     * the given reader.
     *
     * @param reader the reader to wrap or return (not null)
     * @return the given reader or a new {@link BufferedReader} for the given reader
     * @throws NullPointerException if the input parameter is null
     */
    public static BufferedReader buffer(final Reader reader) {
        return reader instanceof BufferedReader ? (BufferedReader) reader : new BufferedReader(reader);
    }

    /**
     * Returns the given reader if it is already a {@link BufferedReader}, otherwise creates a BufferedReader from the
     * given reader.
     *
     * @param reader the reader to wrap or return (not null)
     * @param size   the buffer size, if a new BufferedReader is created.
     * @return the given reader or a new {@link BufferedReader} for the given reader
     * @throws NullPointerException if the input parameter is null
     */
    public static BufferedReader buffer(final Reader reader, final int size) {
        return reader instanceof BufferedReader ? (BufferedReader) reader : new BufferedReader(reader, size);
    }

    /**
     * Returns the given Writer if it is already a {@link BufferedWriter}, otherwise creates a BufferedWriter from the
     * given Writer.
     *
     * @param writer the Writer to wrap or return (not null)
     * @return the given Writer or a new {@link BufferedWriter} for the given Writer
     * @throws NullPointerException if the input parameter is null
     */
    public static BufferedWriter buffer(final Writer writer) {
        return writer instanceof BufferedWriter ? (BufferedWriter) writer : new BufferedWriter(writer);
    }

    /**
     * Returns the given Writer if it is already a {@link BufferedWriter}, otherwise creates a BufferedWriter from the
     * given Writer.
     *
     * @param writer the Writer to wrap or return (not null)
     * @param size   the buffer size, if a new BufferedWriter is created.
     * @return the given Writer or a new {@link BufferedWriter} for the given Writer
     * @throws NullPointerException if the input parameter is null
     */
    public static BufferedWriter buffer(final Writer writer, final int size) {
        return writer instanceof BufferedWriter ? (BufferedWriter) writer : new BufferedWriter(writer, size);
    }

    /**
     * Returns the given OutputStream if it is already a {@link BufferedOutputStream}, otherwise creates a
     * BufferedOutputStream from the given OutputStream.
     *
     * @param outputStream the OutputStream to wrap or return (not null)
     * @return the given OutputStream or a new {@link BufferedOutputStream} for the given OutputStream
     * @throws NullPointerException if the input parameter is null
     */
    public static BufferedOutputStream buffer(final OutputStream outputStream) {
        // reject null early on rather than waiting for IO operation to fail
        Preconditions.checkNotNull(outputStream);
        return outputStream instanceof BufferedOutputStream ?
                (BufferedOutputStream) outputStream : new BufferedOutputStream(outputStream);
    }

    /**
     * Returns the given OutputStream if it is already a {@link BufferedOutputStream}, otherwise creates a
     * BufferedOutputStream from the given OutputStream.
     *
     * @param outputStream the OutputStream to wrap or return (not null)
     * @param size         the buffer size, if a new BufferedOutputStream is created.
     * @return the given OutputStream or a new {@link BufferedOutputStream} for the given OutputStream
     * @throws NullPointerException if the input parameter is null
     */
    public static BufferedOutputStream buffer(final OutputStream outputStream, final int size) {
        // reject null early on rather than waiting for IO operation to fail
        if (outputStream == null) { // not checked by BufferedOutputStream
            throw new NullPointerException();
        }
        return outputStream instanceof BufferedOutputStream ?
                (BufferedOutputStream) outputStream : new BufferedOutputStream(outputStream, size);
    }

    /**
     * Returns the given InputStream if it is already a {@link com.jn.langx.io.stream.BufferedInputStream}, otherwise creates a
     * BufferedInputStream from the given InputStream.
     *
     * @param inputStream the InputStream to wrap or return (not null)
     * @return the given InputStream or a new {@link com.jn.langx.io.stream.BufferedInputStream} for the given InputStream
     * @throws NullPointerException if the input parameter is null
     */
    public static com.jn.langx.io.stream.BufferedInputStream buffer(final InputStream inputStream) {
        // reject null early on rather than waiting for IO operation to fail
        if (inputStream == null) { // not checked by BufferedInputStream
            throw new NullPointerException();
        }
        return inputStream instanceof com.jn.langx.io.stream.BufferedInputStream ?
                (com.jn.langx.io.stream.BufferedInputStream) inputStream : new com.jn.langx.io.stream.BufferedInputStream(inputStream);
    }

    /**
     * Returns the given InputStream if it is already a {@link com.jn.langx.io.stream.BufferedInputStream}, otherwise creates a
     * BufferedInputStream from the given InputStream.
     *
     * @param inputStream the InputStream to wrap or return (not null)
     * @param size        the buffer size, if a new BufferedInputStream is created.
     * @return the given InputStream or a new {@link com.jn.langx.io.stream.BufferedInputStream} for the given InputStream
     * @throws NullPointerException if the input parameter is null
     */
    public static com.jn.langx.io.stream.BufferedInputStream buffer(final InputStream inputStream, final int size) {
        // reject null early on rather than waiting for IO operation to fail
        if (inputStream == null) { // not checked by BufferedInputStream
            throw new NullPointerException();
        }
        return inputStream instanceof com.jn.langx.io.stream.BufferedInputStream ?
                (com.jn.langx.io.stream.BufferedInputStream) inputStream : new com.jn.langx.io.stream.BufferedInputStream(inputStream, size);
    }

    // read toByteArray
    //-----------------------------------------------------------------------

    /**
     * Gets the contents of an <code>InputStream</code> as a <code>byte[]</code>.
     * <p>
     * This method buffers the input internally, so there is no need to use a
     * <code>BufferedInputStream</code>.
     *
     * @param input the <code>InputStream</code> to read from
     * @return the requested byte array
     * @throws NullPointerException if the input is null
     * @throws IOException          if an I/O error occurs
     */
    public static byte[] toByteArray(final InputStream input) throws IOException {
        com.jn.langx.io.stream.ByteArrayOutputStream output = null;
        try {
            output = new com.jn.langx.io.stream.ByteArrayOutputStream();
            copy(input, output);
            return output.toByteArray();
        } finally {
            close(output);
        }
    }

    /**
     * Gets contents of an <code>InputStream</code> as a <code>byte[]</code>.
     * Use this method instead of <code>toByteArray(InputStream)</code>
     * when <code>InputStream</code> size is known.
     * <b>NOTE:</b> the method checks that the length can safely be cast to an int without truncation
     * before using {@link IOs#toByteArray(java.io.InputStream, int)} to read into the byte array.
     * (Arrays can have no more than Integer.MAX_VALUE entries anyway)
     *
     * @param input the <code>InputStream</code> to read from
     * @param size  the size of <code>InputStream</code>
     * @return the requested byte array
     * @throws IOException              if an I/O error occurs or <code>InputStream</code> size differ from parameter
     *                                  size
     * @throws IllegalArgumentException if size is less than zero or size is greater than Integer.MAX_VALUE
     */
    public static byte[] toByteArray(final InputStream input, final long size) throws IOException {

        if (size > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Size cannot be greater than Integer max value: " + size);
        }

        return toByteArray(input, (int) size);
    }

    /**
     * Gets the contents of an <code>InputStream</code> as a <code>byte[]</code>.
     * Use this method instead of <code>toByteArray(InputStream)</code>
     * when <code>InputStream</code> size is known
     *
     * @param input the <code>InputStream</code> to read from
     * @param size  the size of <code>InputStream</code>
     * @return the requested byte array
     * @throws IOException              if an I/O error occurs or <code>InputStream</code> size differ from parameter
     *                                  size
     * @throws IllegalArgumentException if size is less than zero
     */
    public static byte[] toByteArray(final InputStream input, final int size) throws IOException {

        if (size < 0) {
            throw new IllegalArgumentException("Size must be equal or greater than zero: " + size);
        }

        if (size == 0) {
            return new byte[0];
        }

        final byte[] data = new byte[size];
        int offset = 0;
        int read;

        while (offset < size && (read = input.read(data, offset, size - offset)) != EOF) {
            offset += read;
        }

        if (offset != size) {
            throw new IOException("Unexpected read size. current: " + offset + ", expected: " + size);
        }

        return data;
    }

    /**
     * Gets the contents of a <code>Reader</code> as a <code>byte[]</code>
     * using the default character encoding of the platform.
     * <p>
     * This method buffers the input internally, so there is no need to use a
     * <code>BufferedReader</code>.
     *
     * @param input the <code>Reader</code> to read from
     * @return the requested byte array
     * @throws NullPointerException if the input is null
     * @throws IOException          if an I/O error occurs
     */
    public static byte[] toByteArray(final Reader input) throws IOException {
        return toByteArray(input, Charset.defaultCharset());
    }

    /**
     * Gets the contents of a <code>Reader</code> as a <code>byte[]</code>
     * using the specified character encoding.
     * <p>
     * This method buffers the input internally, so there is no need to use a
     * <code>BufferedReader</code>.
     *
     * @param input    the <code>Reader</code> to read from
     * @param encoding the encoding to use, null means platform default
     * @return the requested byte array
     * @throws NullPointerException if the input is null
     * @throws IOException          if an I/O error occurs
     */
    public static byte[] toByteArray(final Reader input, final Charset encoding) throws IOException {
        com.jn.langx.io.stream.ByteArrayOutputStream output = null;
        try {
            output = new ByteArrayOutputStream();
            copy(input, output, encoding);
            return output.toByteArray();
        } finally {
            close(output);
        }
    }

    /**
     * Gets the contents of a <code>Reader</code> as a <code>byte[]</code>
     * using the specified character encoding.
     * <p>
     * Character encoding names can be found at
     * <a href="http://www.iana.org/assignments/character-sets">IANA</a>.
     * <p>
     * This method buffers the input internally, so there is no need to use a
     * <code>BufferedReader</code>.
     *
     * @param input    the <code>Reader</code> to read from
     * @param encoding the encoding to use, null means platform default
     * @return the requested byte array
     * @throws NullPointerException                         if the input is null
     * @throws IOException                                  if an I/O error occurs
     * @throws java.nio.charset.UnsupportedCharsetException thrown instead of {@link java.io
     *                                                      .UnsupportedEncodingException} in version 2.2 if the
     *                                                      encoding is not supported.
     */
    public static byte[] toByteArray(final Reader input, final String encoding) throws IOException {
        return toByteArray(input, Charsets.getCharset(encoding));
    }

    /**
     * Gets the contents of a <code>String</code> as a <code>byte[]</code>
     * using the default character encoding of the platform.
     * <p>
     * This is the same as {@link String#getBytes()}.
     *
     * @param input the <code>String</code> to convert
     * @return the requested byte array
     * @throws NullPointerException if the input is null
     * @throws IOException          if an I/O error occurs (never occurs)
     */
    public static byte[] toByteArray(final String input) throws IOException {
        // make explicit the use of the default charset
        return input.getBytes(Charset.defaultCharset());
    }

    /**
     * Gets the contents of a <code>URI</code> as a <code>byte[]</code>.
     *
     * @param uri the <code>URI</code> to read
     * @return the requested byte array
     * @throws NullPointerException if the uri is null
     * @throws IOException          if an I/O exception occurs
     */
    public static byte[] toByteArray(final URI uri) throws IOException {
        return toByteArray(uri.toURL());
    }

    /**
     * Gets the contents of a <code>URL</code> as a <code>byte[]</code>.
     *
     * @param url the <code>URL</code> to read
     * @return the requested byte array
     * @throws NullPointerException if the input is null
     * @throws IOException          if an I/O exception occurs
     */
    public static byte[] toByteArray(final URL url) throws IOException {
        final URLConnection conn = URLs.openURL(url);
        try {
            return toByteArray(conn);
        } finally {
            close(conn);
        }
    }

    /**
     * Gets the contents of a <code>URLConnection</code> as a <code>byte[]</code>.
     *
     * @param urlConn the <code>URLConnection</code> to read
     * @return the requested byte array
     * @throws NullPointerException if the urlConn is null
     * @throws IOException          if an I/O exception occurs
     */
    public static byte[] toByteArray(final URLConnection urlConn) throws IOException {
        InputStream inputStream = null;

        try {
            inputStream = urlConn.getInputStream();
            return toByteArray(inputStream);
        } finally {
            close(inputStream);
        }
    }

    // read char[]
    //-----------------------------------------------------------------------

    /**
     * Gets the contents of an <code>InputStream</code> as a character array
     * using the default character encoding of the platform.
     * <p>
     * This method buffers the input internally, so there is no need to use a
     * <code>BufferedInputStream</code>.
     *
     * @param is the <code>InputStream</code> to read from
     * @return the requested character array
     * @throws NullPointerException if the input is null
     * @throws IOException          if an I/O error occurs
     */
    public static char[] toCharArray(final InputStream is) throws IOException {
        return toCharArray(is, Charset.defaultCharset());
    }

    /**
     * Gets the contents of an <code>InputStream</code> as a character array
     * using the specified character encoding.
     * <p>
     * This method buffers the input internally, so there is no need to use a
     * <code>BufferedInputStream</code>.
     *
     * @param is       the <code>InputStream</code> to read from
     * @param encoding the encoding to use, null means platform default
     * @return the requested character array
     * @throws NullPointerException if the input is null
     * @throws IOException          if an I/O error occurs
     */
    public static char[] toCharArray(final InputStream is, final Charset encoding)
            throws IOException {
        final CharArrayWriter output = new CharArrayWriter();
        copy(is, output, encoding);
        return output.toCharArray();
    }

    /**
     * Gets the contents of an <code>InputStream</code> as a character array
     * using the specified character encoding.
     * <p>
     * Character encoding names can be found at
     * <a href="http://www.iana.org/assignments/character-sets">IANA</a>.
     * <p>
     * This method buffers the input internally, so there is no need to use a
     * <code>BufferedInputStream</code>.
     *
     * @param is       the <code>InputStream</code> to read from
     * @param encoding the encoding to use, null means platform default
     * @return the requested character array
     * @throws NullPointerException                         if the input is null
     * @throws IOException                                  if an I/O error occurs
     * @throws java.nio.charset.UnsupportedCharsetException thrown instead of {@link java.io
     *                                                      .UnsupportedEncodingException} in version 2.2 if the
     *                                                      encoding is not supported.
     */
    public static char[] toCharArray(final InputStream is, final String encoding) throws IOException {
        return toCharArray(is, Charsets.getCharset(encoding));
    }

    /**
     * Gets the contents of a <code>Reader</code> as a character array.
     * <p>
     * This method buffers the input internally, so there is no need to use a
     * <code>BufferedReader</code>.
     *
     * @param input the <code>Reader</code> to read from
     * @return the requested character array
     * @throws NullPointerException if the input is null
     * @throws IOException          if an I/O error occurs
     */
    public static char[] toCharArray(final Reader input) throws IOException {
        final CharArrayWriter sw = new CharArrayWriter();
        copy(input, sw);
        return sw.toCharArray();
    }

    // read toString
    //-----------------------------------------------------------------------

    /**
     * Gets the contents of an <code>InputStream</code> as a String
     * using the default character encoding of the platform.
     * <p>
     * This method buffers the input internally, so there is no need to use a
     * <code>BufferedInputStream</code>.
     *
     * @param input the <code>InputStream</code> to read from
     * @return the requested String
     * @throws NullPointerException if the input is null
     * @throws IOException          if an I/O error occurs
     */
    public static String toString(final InputStream input) throws IOException {
        return toString(input, Charset.defaultCharset());
    }

    /**
     * Gets the contents of an <code>InputStream</code> as a String
     * using the specified character encoding.
     * <p>
     * This method buffers the input internally, so there is no need to use a
     * <code>BufferedInputStream</code>.
     * </p>
     *
     * @param input    the <code>InputStream</code> to read from
     * @param encoding the encoding to use, null means platform default
     * @return the requested String
     * @throws NullPointerException if the input is null
     * @throws IOException          if an I/O error occurs
     */
    public static String toString(final InputStream input, final Charset encoding) throws IOException {
        StringBuilderWriter sw = null;
        try {
            sw = new StringBuilderWriter();
            copy(input, sw, encoding);
            return sw.toString();
        } finally {
            close(sw);
        }
    }

    /**
     * Gets the contents of an <code>InputStream</code> as a String
     * using the specified character encoding.
     * <p>
     * Character encoding names can be found at
     * <a href="http://www.iana.org/assignments/character-sets">IANA</a>.
     * <p>
     * This method buffers the input internally, so there is no need to use a
     * <code>BufferedInputStream</code>.
     *
     * @param input    the <code>InputStream</code> to read from
     * @param encoding the encoding to use, null means platform default
     * @return the requested String
     * @throws NullPointerException                         if the input is null
     * @throws IOException                                  if an I/O error occurs
     * @throws java.nio.charset.UnsupportedCharsetException thrown instead of {@link java.io
     *                                                      .UnsupportedEncodingException} in version 2.2 if the
     *                                                      encoding is not supported.
     */
    public static String toString(final InputStream input, final String encoding)
            throws IOException {
        return toString(input, Charsets.getCharset(encoding));
    }

    /**
     * Gets the contents of a <code>Reader</code> as a String.
     * <p>
     * This method buffers the input internally, so there is no need to use a
     * <code>BufferedReader</code>.
     *
     * @param input the <code>Reader</code> to read from
     * @return the requested String
     * @throws NullPointerException if the input is null
     * @throws IOException          if an I/O error occurs
     */
    public static String toString(final Reader input) throws IOException {
        StringBuilderWriter sw = null;
        try {
            sw = new StringBuilderWriter();
            copy(input, sw);
            return sw.toString();
        } finally {
            close(sw);
        }
    }

    /**
     * Gets the contents at the given URI.
     *
     * @param uri The URI source.
     * @return The contents of the URL as a String.
     * @throws IOException if an I/O exception occurs.
     */
    public static String toString(final URI uri) throws IOException {
        return toString(uri, Charset.defaultCharset());
    }

    /**
     * Gets the contents at the given URI.
     *
     * @param uri      The URI source.
     * @param encoding The encoding name for the URL contents.
     * @return The contents of the URL as a String.
     * @throws IOException if an I/O exception occurs.
     *                     .
     */
    public static String toString(final URI uri, final Charset encoding) throws IOException {
        return toString(uri.toURL(), Charsets.getCharset(encoding));
    }

    /**
     * Gets the contents at the given URI.
     *
     * @param uri      The URI source.
     * @param encoding The encoding name for the URL contents.
     * @return The contents of the URL as a String.
     * @throws IOException                                  if an I/O exception occurs.
     * @throws java.nio.charset.UnsupportedCharsetException thrown instead of {@link java.io
     *                                                      .UnsupportedEncodingException} in version 2.2 if the
     *                                                      encoding is not supported.
     */
    public static String toString(final URI uri, final String encoding) throws IOException {
        return toString(uri, Charsets.getCharset(encoding));
    }

    /**
     * Gets the contents at the given URL.
     *
     * @param url The URL source.
     * @return The contents of the URL as a String.
     * @throws IOException if an I/O exception occurs.
     */
    public static String toString(final URL url) throws IOException {
        return toString(url, Charset.defaultCharset());
    }

    /**
     * Gets the contents at the given URL.
     *
     * @param url      The URL source.
     * @param encoding The encoding name for the URL contents.
     * @return The contents of the URL as a String.
     * @throws IOException if an I/O exception occurs.
     */
    public static String toString(final URL url, final Charset encoding) throws IOException {
        InputStream inputStream = null;
        try {
            inputStream = url.openStream();
            return toString(inputStream, encoding);
        } finally {
            close(inputStream);
        }
    }

    /**
     * Gets the contents at the given URL.
     *
     * @param url      The URL source.
     * @param encoding The encoding name for the URL contents.
     * @return The contents of the URL as a String.
     * @throws IOException                                  if an I/O exception occurs.
     * @throws java.nio.charset.UnsupportedCharsetException thrown instead of {@link java.io
     *                                                      .UnsupportedEncodingException} in version 2.2 if the
     *                                                      encoding is not supported.
     */
    public static String toString(final URL url, final String encoding) throws IOException {
        return toString(url, Charsets.getCharset(encoding));
    }

    /**
     * Gets the contents of a <code>byte[]</code> as a String
     * using the default character encoding of the platform.
     *
     * @param input the byte array to read from
     * @return the requested String
     * @throws NullPointerException if the input is null
     * @throws IOException          if an I/O error occurs (never occurs)
     */
    public static String toString(final byte[] input) throws IOException {
        // make explicit the use of the default charset
        return new String(input, Charset.defaultCharset());
    }

    /**
     * Gets the contents of a <code>byte[]</code> as a String
     * using the specified character encoding.
     * <p>
     * Character encoding names can be found at
     * <a href="http://www.iana.org/assignments/character-sets">IANA</a>.
     *
     * @param input    the byte array to read from
     * @param encoding the encoding to use, null means platform default
     * @return the requested String
     * @throws NullPointerException if the input is null
     * @throws IOException          if an I/O error occurs (never occurs)
     */
    public static String toString(final byte[] input, final String encoding) throws IOException {
        return new String(input, Charsets.getCharset(encoding));
    }

    // resources
    //-----------------------------------------------------------------------

    /**
     * Gets the contents of a classpath resource as a String using the
     * specified character encoding.
     * <p>
     * <p>
     * It is expected the given <code>name</code> to be absolute. The
     * behavior is not well-defined otherwise.
     * </p>
     *
     * @param name     name of the desired resource
     * @param encoding the encoding to use, null means platform default
     * @return the requested String
     * @throws IOException if an I/O error occurs
     */
    public static String resourceToString(final String name, final Charset encoding) throws IOException {
        return resourceToString(name, encoding, null);
    }

    /**
     * Gets the contents of a classpath resource as a String using the
     * specified character encoding.
     * <p>
     * <p>
     * It is expected the given <code>name</code> to be absolute. The
     * behavior is not well-defined otherwise.
     * </p>
     *
     * @param name        name of the desired resource
     * @param encoding    the encoding to use, null means platform default
     * @param classLoader the class loader that the resolution of the resource is delegated to
     * @return the requested String
     * @throws IOException if an I/O error occurs
     */
    public static String resourceToString(final String name, final Charset encoding, final ClassLoader classLoader) throws IOException {
        return toString(resourceToURL(name, classLoader), encoding);
    }

    /**
     * Gets the contents of a classpath resource as a byte array.
     * <p>
     * <p>
     * It is expected the given <code>name</code> to be absolute. The
     * behavior is not well-defined otherwise.
     * </p>
     *
     * @param name name of the desired resource
     * @return the requested byte array
     * @throws IOException if an I/O error occurs
     */
    public static byte[] resourceToByteArray(final String name) throws IOException {
        return resourceToByteArray(name, null);
    }

    /**
     * Gets the contents of a classpath resource as a byte array.
     * <p>
     * <p>
     * It is expected the given <code>name</code> to be absolute. The
     * behavior is not well-defined otherwise.
     * </p>
     *
     * @param name        name of the desired resource
     * @param classLoader the class loader that the resolution of the resource is delegated to
     * @return the requested byte array
     * @throws IOException if an I/O error occurs
     */
    public static byte[] resourceToByteArray(final String name, final ClassLoader classLoader) throws IOException {
        return toByteArray(resourceToURL(name, classLoader));
    }

    /**
     * Gets a URL pointing to the given classpath resource.
     * <p>
     * <p>
     * It is expected the given <code>name</code> to be absolute. The
     * behavior is not well-defined otherwise.
     * </p>
     *
     * @param name name of the desired resource
     * @return the requested URL
     * @throws IOException if an I/O error occurs
     */
    public static URL resourceToURL(final String name) throws IOException {
        return resourceToURL(name, null);
    }

    /**
     * Gets a URL pointing to the given classpath resource.
     * <p>
     * <p>
     * It is expected the given <code>name</code> to be absolute. The
     * behavior is not well-defined otherwise.
     * </p>
     *
     * @param name        name of the desired resource
     * @param classLoader the class loader that the resolution of the resource is delegated to
     * @return the requested URL
     * @throws IOException if an I/O error occurs
     */
    public static URL resourceToURL(final String name, final ClassLoader classLoader) throws IOException {
        // What about the thread context class loader?
        // What about the system class loader?
        final URL resource = classLoader == null ? IOs.class.getResource(name) : classLoader.getResource(name);

        if (resource == null) {
            throw new IOException("Resource not found: " + name);
        }

        return resource;
    }


    //-----------------------------------------------------------------------

    /**
     * Converts the specified CharSequence to an input stream, encoded as bytes
     * using the default character encoding of the platform.
     *
     * @param input the CharSequence to convert
     * @return an input stream
     */
    public static InputStream toInputStream(final CharSequence input) {
        return toInputStream(input, Charset.defaultCharset());
    }

    /**
     * Converts the specified CharSequence to an input stream, encoded as bytes
     * using the specified character encoding.
     *
     * @param input    the CharSequence to convert
     * @param encoding the encoding to use, null means platform default
     * @return an input stream
     */
    public static InputStream toInputStream(final CharSequence input, final Charset encoding) {
        return toInputStream(input.toString(), encoding);
    }

    /**
     * Converts the specified CharSequence to an input stream, encoded as bytes
     * using the specified character encoding.
     * <p>
     * Character encoding names can be found at
     * <a href="http://www.iana.org/assignments/character-sets">IANA</a>.
     *
     * @param input    the CharSequence to convert
     * @param encoding the encoding to use, null means platform default
     * @return an input stream
     * @throws IOException                                  if the encoding is invalid
     * @throws java.nio.charset.UnsupportedCharsetException thrown instead of {@link java.io
     *                                                      .UnsupportedEncodingException} in version 2.2 if the
     *                                                      encoding is not supported.
     */
    public static InputStream toInputStream(final CharSequence input, final String encoding) throws IOException {
        return toInputStream(input, Charsets.getCharset(encoding));
    }

    //-----------------------------------------------------------------------

    /**
     * Converts the specified string to an input stream, encoded as bytes
     * using the default character encoding of the platform.
     *
     * @param input the string to convert
     * @return an input stream
     */
    public static InputStream toInputStream(final String input) {
        return toInputStream(input, Charset.defaultCharset());
    }

    /**
     * Converts the specified string to an input stream, encoded as bytes
     * using the specified character encoding.
     *
     * @param input    the string to convert
     * @param encoding the encoding to use, null means platform default
     * @return an input stream
     */
    public static InputStream toInputStream(final String input, final Charset encoding) {
        return new ByteArrayInputStream(input.getBytes(Charsets.getCharset(encoding)));
    }

    /**
     * Converts the specified string to an input stream, encoded as bytes
     * using the specified character encoding.
     * <p>
     * Character encoding names can be found at
     * <a href="http://www.iana.org/assignments/character-sets">IANA</a>.
     *
     * @param input    the string to convert
     * @param encoding the encoding to use, null means platform default
     * @return an input stream
     * @throws IOException                                  if the encoding is invalid
     * @throws java.nio.charset.UnsupportedCharsetException thrown instead of {@link java.io
     *                                                      .UnsupportedEncodingException} in version 2.2 if the
     *                                                      encoding is not supported.
     */
    public static InputStream toInputStream(final String input, final String encoding) throws IOException {
        final byte[] bytes = input.getBytes(Charsets.getCharset(encoding));
        return new ByteArrayInputStream(bytes);
    }

    // write byte[]
    //-----------------------------------------------------------------------

    /**
     * Writes bytes from a <code>byte[]</code> to an <code>OutputStream</code>.
     *
     * @param data   the byte array to write, do not modify during output,
     *               null ignored
     * @param output the <code>OutputStream</code> to write to
     * @throws NullPointerException if output is null
     * @throws IOException          if an I/O error occurs
     */
    public static void write(final byte[] data, final OutputStream output)
            throws IOException {
        if (data != null) {
            output.write(data);
        }
    }

    /**
     * Writes bytes from a <code>byte[]</code> to an <code>OutputStream</code> using chunked writes.
     * This is intended for writing very large byte arrays which might otherwise cause excessive
     * memory usage if the native code has to allocate a copy.
     *
     * @param data   the byte array to write, do not modify during output,
     *               null ignored
     * @param output the <code>OutputStream</code> to write to
     * @throws NullPointerException if output is null
     * @throws IOException          if an I/O error occurs
     */
    public static void writeChunked(final byte[] data, final OutputStream output)
            throws IOException {
        if (data != null) {
            int bytes = data.length;
            int offset = 0;
            while (bytes > 0) {
                final int chunk = Math.min(bytes, DEFAULT_BUFFER_SIZE);
                output.write(data, offset, chunk);
                bytes -= chunk;
                offset += chunk;
            }
        }
    }

    /**
     * Writes bytes from a <code>byte[]</code> to chars on a <code>Writer</code>
     * using the default character encoding of the platform.
     * <p>
     * This method uses {@link String#String(byte[])}.
     *
     * @param data   the byte array to write, do not modify during output,
     *               null ignored
     * @param output the <code>Writer</code> to write to
     * @throws NullPointerException if output is null
     * @throws IOException          if an I/O error occurs
     */
    public static void write(final byte[] data, final Writer output) throws IOException {
        write(data, output, Charset.defaultCharset());
    }

    /**
     * Writes bytes from a <code>byte[]</code> to chars on a <code>Writer</code>
     * using the specified character encoding.
     * <p>
     * This method uses {@link String#String(byte[], String)}.
     *
     * @param data     the byte array to write, do not modify during output,
     *                 null ignored
     * @param output   the <code>Writer</code> to write to
     * @param encoding the encoding to use, null means platform default
     * @throws NullPointerException if output is null
     * @throws IOException          if an I/O error occurs
     */
    public static void write(final byte[] data, final Writer output, final Charset encoding) throws IOException {
        if (data != null) {
            output.write(new String(data, Charsets.getCharset(encoding)));
        }
    }

    /**
     * Writes bytes from a <code>byte[]</code> to chars on a <code>Writer</code>
     * using the specified character encoding.
     * <p>
     * Character encoding names can be found at
     * <a href="http://www.iana.org/assignments/character-sets">IANA</a>.
     * <p>
     * This method uses {@link String#String(byte[], String)}.
     *
     * @param data     the byte array to write, do not modify during output,
     *                 null ignored
     * @param output   the <code>Writer</code> to write to
     * @param encoding the encoding to use, null means platform default
     * @throws NullPointerException                         if output is null
     * @throws IOException                                  if an I/O error occurs
     * @throws java.nio.charset.UnsupportedCharsetException thrown instead of {@link java.io
     *                                                      .UnsupportedEncodingException} in version 2.2 if the
     *                                                      encoding is not supported.
     */
    public static void write(final byte[] data, final Writer output, final String encoding) throws IOException {
        write(data, output, Charsets.getCharset(encoding));
    }

    // write char[]
    //-----------------------------------------------------------------------

    /**
     * Writes chars from a <code>char[]</code> to a <code>Writer</code>
     *
     * @param data   the char array to write, do not modify during output,
     *               null ignored
     * @param output the <code>Writer</code> to write to
     * @throws NullPointerException if output is null
     * @throws IOException          if an I/O error occurs
     */
    public static void write(final char[] data, final Writer output) throws IOException {
        if (data != null) {
            output.write(data);
        }
    }

    /**
     * Writes chars from a <code>char[]</code> to a <code>Writer</code> using chunked writes.
     * This is intended for writing very large byte arrays which might otherwise cause excessive
     * memory usage if the native code has to allocate a copy.
     *
     * @param data   the char array to write, do not modify during output,
     *               null ignored
     * @param output the <code>Writer</code> to write to
     * @throws NullPointerException if output is null
     * @throws IOException          if an I/O error occurs
     */
    public static void writeChunked(final char[] data, final Writer output) throws IOException {
        if (data != null) {
            int bytes = data.length;
            int offset = 0;
            while (bytes > 0) {
                final int chunk = Math.min(bytes, DEFAULT_BUFFER_SIZE);
                output.write(data, offset, chunk);
                bytes -= chunk;
                offset += chunk;
            }
        }
    }

    /**
     * Writes chars from a <code>char[]</code> to bytes on an
     * <code>OutputStream</code>.
     * <p>
     * This method uses {@link String#String(char[])} and
     * {@link String#getBytes()}.
     *
     * @param data   the char array to write, do not modify during output,
     *               null ignored
     * @param output the <code>OutputStream</code> to write to
     * @throws NullPointerException if output is null
     * @throws IOException          if an I/O error occurs
     */
    public static void write(final char[] data, final OutputStream output)
            throws IOException {
        write(data, output, Charset.defaultCharset());
    }

    /**
     * Writes chars from a <code>char[]</code> to bytes on an
     * <code>OutputStream</code> using the specified character encoding.
     * <p>
     * This method uses {@link String#String(char[])} and
     * {@link String#getBytes(String)}.
     *
     * @param data     the char array to write, do not modify during output,
     *                 null ignored
     * @param output   the <code>OutputStream</code> to write to
     * @param encoding the encoding to use, null means platform default
     * @throws NullPointerException if output is null
     * @throws IOException          if an I/O error occurs
     */
    public static void write(final char[] data, final OutputStream output, final Charset encoding) throws IOException {
        if (data != null) {
            output.write(new String(data).getBytes(Charsets.getCharset(encoding)));
        }
    }

    /**
     * Writes chars from a <code>char[]</code> to bytes on an
     * <code>OutputStream</code> using the specified character encoding.
     * <p>
     * Character encoding names can be found at
     * <a href="http://www.iana.org/assignments/character-sets">IANA</a>.
     * <p>
     * This method uses {@link String#String(char[])} and
     * {@link String#getBytes(String)}.
     *
     * @param data     the char array to write, do not modify during output,
     *                 null ignored
     * @param output   the <code>OutputStream</code> to write to
     * @param encoding the encoding to use, null means platform default
     * @throws NullPointerException                         if output is null
     * @throws IOException                                  if an I/O error occurs
     * @throws java.nio.charset.UnsupportedCharsetException thrown instead of {@link java.io
     *                                                      .UnsupportedEncodingException} in version 2.2 if the encoding is not supported.
     */
    public static void write(final char[] data, final OutputStream output, final String encoding)
            throws IOException {
        write(data, output, Charsets.getCharset(encoding));
    }

    // write CharSequence
    //-----------------------------------------------------------------------

    /**
     * Writes chars from a <code>CharSequence</code> to a <code>Writer</code>.
     *
     * @param data   the <code>CharSequence</code> to write, null ignored
     * @param output the <code>Writer</code> to write to
     * @throws NullPointerException if output is null
     * @throws IOException          if an I/O error occurs
     */
    public static void write(final CharSequence data, final Writer output) throws IOException {
        if (data != null) {
            write(data.toString(), output);
        }
    }

    /**
     * Writes chars from a <code>CharSequence</code> to bytes on an
     * <code>OutputStream</code> using the default character encoding of the
     * platform.
     * <p>
     * This method uses {@link String#getBytes()}.
     *
     * @param data   the <code>CharSequence</code> to write, null ignored
     * @param output the <code>OutputStream</code> to write to
     * @throws NullPointerException if output is null
     * @throws IOException          if an I/O error occurs
     */
    public static void write(final CharSequence data, final OutputStream output)
            throws IOException {
        write(data, output, Charset.defaultCharset());
    }

    /**
     * Writes chars from a <code>CharSequence</code> to bytes on an
     * <code>OutputStream</code> using the specified character encoding.
     * <p>
     * This method uses {@link String#getBytes(String)}.
     *
     * @param data     the <code>CharSequence</code> to write, null ignored
     * @param output   the <code>OutputStream</code> to write to
     * @param encoding the encoding to use, null means platform default
     * @throws NullPointerException if output is null
     * @throws IOException          if an I/O error occurs
     */
    public static void write(final CharSequence data, final OutputStream output, final Charset encoding)
            throws IOException {
        if (data != null) {
            write(data.toString(), output, encoding);
        }
    }

    /**
     * Writes chars from a <code>CharSequence</code> to bytes on an
     * <code>OutputStream</code> using the specified character encoding.
     * <p>
     * Character encoding names can be found at
     * <a href="http://www.iana.org/assignments/character-sets">IANA</a>.
     * <p>
     * This method uses {@link String#getBytes(String)}.
     *
     * @param data     the <code>CharSequence</code> to write, null ignored
     * @param output   the <code>OutputStream</code> to write to
     * @param encoding the encoding to use, null means platform default
     * @throws NullPointerException                         if output is null
     * @throws IOException                                  if an I/O error occurs
     * @throws java.nio.charset.UnsupportedCharsetException thrown instead of {@link java.io
     *                                                      .UnsupportedEncodingException} in version 2.2 if the encoding is not supported.
     */
    public static void write(final CharSequence data, final OutputStream output, final String encoding)
            throws IOException {
        write(data, output, Charsets.getCharset(encoding));
    }

    // write String
    //-----------------------------------------------------------------------

    /**
     * Writes chars from a <code>String</code> to a <code>Writer</code>.
     *
     * @param data   the <code>String</code> to write, null ignored
     * @param output the <code>Writer</code> to write to
     * @throws NullPointerException if output is null
     * @throws IOException          if an I/O error occurs
     */
    public static void write(final String data, final Writer output) throws IOException {
        if (data != null) {
            output.write(data);
        }
    }

    /**
     * Writes chars from a <code>String</code> to bytes on an
     * <code>OutputStream</code> using the default character encoding of the
     * platform.
     * <p>
     * This method uses {@link String#getBytes()}.
     *
     * @param data   the <code>String</code> to write, null ignored
     * @param output the <code>OutputStream</code> to write to
     * @throws NullPointerException if output is null
     * @throws IOException          if an I/O error occurs
     */
    public static void write(final String data, final OutputStream output)
            throws IOException {
        write(data, output, Charset.defaultCharset());
    }

    /**
     * Writes chars from a <code>String</code> to bytes on an
     * <code>OutputStream</code> using the specified character encoding.
     * <p>
     * This method uses {@link String#getBytes(String)}.
     *
     * @param data     the <code>String</code> to write, null ignored
     * @param output   the <code>OutputStream</code> to write to
     * @param encoding the encoding to use, null means platform default
     * @throws NullPointerException if output is null
     * @throws IOException          if an I/O error occurs
     */
    public static void write(final String data, final OutputStream output, final Charset encoding) throws IOException {
        if (data != null) {
            output.write(data.getBytes(Charsets.getCharset(encoding)));
        }
    }

    /**
     * Writes chars from a <code>String</code> to bytes on an
     * <code>OutputStream</code> using the specified character encoding.
     * <p>
     * Character encoding names can be found at
     * <a href="http://www.iana.org/assignments/character-sets">IANA</a>.
     * <p>
     * This method uses {@link String#getBytes(String)}.
     *
     * @param data     the <code>String</code> to write, null ignored
     * @param output   the <code>OutputStream</code> to write to
     * @param encoding the encoding to use, null means platform default
     * @throws NullPointerException                         if output is null
     * @throws IOException                                  if an I/O error occurs
     * @throws java.nio.charset.UnsupportedCharsetException thrown instead of {@link java.io
     *                                                      .UnsupportedEncodingException} in version 2.2 if the encoding is not supported.
     */
    public static void write(final String data, final OutputStream output, final String encoding)
            throws IOException {
        write(data, output, Charsets.getCharset(encoding));
    }

    public static void write(final String data, final Writer output, final String encoding) throws IOException {
        if (Emptys.isNotEmpty(data)) {
            write(data.getBytes(encoding), output);
        }
    }

    // write StringBuffer
    //-----------------------------------------------------------------------
    public static void write(final StringBuilder data, final Writer output)
            throws IOException {
        if (data != null) {
            output.write(data.toString());
        }
    }

    /**
     * Writes chars from a <code>StringBuffer</code> to bytes on an
     * <code>OutputStream</code> using the default character encoding of the
     * platform.
     * <p>
     * This method uses {@link String#getBytes()}.
     *
     * @param data   the <code>Appendable</code> to write, null ignored
     * @param output the <code>OutputStream</code> to write to
     * @throws NullPointerException if output is null
     * @throws IOException          if an I/O error occurs
     */
    public static void write(final Appendable data, final OutputStream output)
            throws IOException {
        write(data, output, null);
    }


    /**
     * Writes chars from a <code>StringBuffer</code> to bytes on an
     * <code>OutputStream</code> using the specified character encoding.
     * <p>
     * Character encoding names can be found at
     * <a href="http://www.iana.org/assignments/character-sets">IANA</a>.
     * <p>
     * This method uses {@link String#getBytes(String)}.
     *
     * @param data     the <code>StringBuffer</code> to write, null ignored
     * @param output   the <code>OutputStream</code> to write to
     * @param encoding the encoding to use, null means platform default
     * @throws NullPointerException                         if output is null
     * @throws IOException                                  if an I/O error occurs
     * @throws java.nio.charset.UnsupportedCharsetException thrown instead of {@link java.io
     *                                                      .UnsupportedEncodingException} in version 2.2 if the encoding is not supported.
     */
    public static void write(final Appendable data, final OutputStream output, final String encoding)
            throws IOException {
        if (data != null) {
            output.write(data.toString().getBytes(Charsets.getCharset(encoding)));
        }
    }

    // writeLines
    //-----------------------------------------------------------------------

    /**
     * Writes the <code>toString()</code> value of each item in a collection to
     * an <code>OutputStream</code> line by line, using the default character
     * encoding of the platform and the specified line ending.
     *
     * @param lines      the lines to write, null entries produce blank lines
     * @param lineEnding the line separator to use, null is system default
     * @param output     the <code>OutputStream</code> to write to, not null, not closed
     * @throws NullPointerException if the output is null
     * @throws IOException          if an I/O error occurs
     */
    public static void writeLines(final Collection<?> lines, final String lineEnding,
                                  final OutputStream output) throws IOException {
        writeLines(lines, lineEnding, output, Charset.defaultCharset());
    }

    /**
     * Writes the <code>toString()</code> value of each item in a collection to
     * an <code>OutputStream</code> line by line, using the specified character
     * encoding and the specified line ending.
     *
     * @param lines      the lines to write, null entries produce blank lines
     * @param lineEnding the line separator to use, null is system default
     * @param output     the <code>OutputStream</code> to write to, not null, not closed
     * @param encoding   the encoding to use, null means platform default
     * @throws NullPointerException if the output is null
     * @throws IOException          if an I/O error occurs
     */
    public static void writeLines(final Collection<?> lines, String lineEnding, final OutputStream output,
                                  final Charset encoding) throws IOException {
        if (lines == null) {
            return;
        }
        if (lineEnding == null) {
            lineEnding = LINE_SEPARATOR;
        }
        final Charset cs = Charsets.getCharset(encoding);
        for (final Object line : lines) {
            if (line != null) {
                output.write(line.toString().getBytes(cs));
            }
            output.write(lineEnding.getBytes(cs));
        }
    }

    /**
     * Writes the <code>toString()</code> value of each item in a collection to
     * an <code>OutputStream</code> line by line, using the specified character
     * encoding and the specified line ending.
     * <p>
     * Character encoding names can be found at
     * <a href="http://www.iana.org/assignments/character-sets">IANA</a>.
     *
     * @param lines      the lines to write, null entries produce blank lines
     * @param lineEnding the line separator to use, null is system default
     * @param output     the <code>OutputStream</code> to write to, not null, not closed
     * @param encoding   the encoding to use, null means platform default
     * @throws NullPointerException                         if the output is null
     * @throws IOException                                  if an I/O error occurs
     * @throws java.nio.charset.UnsupportedCharsetException thrown instead of {@link java.io
     *                                                      .UnsupportedEncodingException} in version 2.2 if the
     *                                                      encoding is not supported.
     */
    public static void writeLines(final Collection<?> lines, final String lineEnding,
                                  final OutputStream output, final String encoding) throws IOException {
        writeLines(lines, lineEnding, output, Charsets.getCharset(encoding));
    }

    /**
     * Writes the <code>toString()</code> value of each item in a collection to
     * a <code>Writer</code> line by line, using the specified line ending.
     *
     * @param lines      the lines to write, null entries produce blank lines
     * @param lineEnding the line separator to use, null is system default
     * @param writer     the <code>Writer</code> to write to, not null, not closed
     * @throws NullPointerException if the input is null
     * @throws IOException          if an I/O error occurs
     */
    public static void writeLines(final Collection<?> lines, String lineEnding,
                                  final Writer writer) throws IOException {
        if (lines == null) {
            return;
        }
        if (lineEnding == null) {
            lineEnding = LINE_SEPARATOR;
        }
        for (final Object line : lines) {
            if (line != null) {
                writer.write(line.toString());
            }
            writer.write(lineEnding);
        }
    }


    /**
     * Copies some or all bytes from a large (over 2GB) <code>InputStream</code> to an
     * <code>OutputStream</code>, optionally skipping input bytes.
     * <p>
     * This method buffers the input internally, so there is no need to use a
     * <code>BufferedInputStream</code>.
     * </p>
     * <p>
     * Note that the implementation uses {@link #skip(InputStream, long)}.
     * This means that the method may be considerably less efficient than using the actual skip implementation,
     * this is done to guarantee that the correct number of characters are skipped.
     * </p>
     * The buffer size is given by {@link #DEFAULT_BUFFER_SIZE}.
     *
     * @param input       the <code>InputStream</code> to read from
     * @param output      the <code>OutputStream</code> to write to
     * @param inputOffset : number of bytes to skip from input before copying
     *                    -ve values are ignored
     * @param length      : number of bytes to copy. -ve means all
     * @return the number of bytes copied
     * @throws NullPointerException if the input or output is null
     * @throws IOException          if an I/O error occurs
     */
    public static long copyLarge(final InputStream input, final OutputStream output, final long inputOffset,
                                 final long length) throws IOException {
        return copyLarge(input, output, inputOffset, length, new byte[DEFAULT_BUFFER_SIZE]);
    }

    /**
     * Copies some or all bytes from a large (over 2GB) <code>InputStream</code> to an
     * <code>OutputStream</code>, optionally skipping input bytes.
     * <p>
     * This method uses the provided buffer, so there is no need to use a
     * <code>BufferedInputStream</code>.
     * </p>
     * <p>
     * Note that the implementation uses {@link #skip(InputStream, long)}.
     * This means that the method may be considerably less efficient than using the actual skip implementation,
     * this is done to guarantee that the correct number of characters are skipped.
     * </p>
     *
     * @param input       the <code>InputStream</code> to read from
     * @param output      the <code>OutputStream</code> to write to
     * @param inputOffset : number of bytes to skip from input before copying
     *                    -ve values are ignored
     * @param length      : number of bytes to copy. -ve means all
     * @param buffer      the buffer to use for the copy
     * @return the number of bytes copied
     * @throws NullPointerException if the input or output is null
     * @throws IOException          if an I/O error occurs
     */
    public static long copyLarge(final InputStream input, final OutputStream output,
                                 final long inputOffset, final long length, final byte[] buffer) throws IOException {
        if (inputOffset > 0) {
            skipFully(input, inputOffset);
        }
        if (length == 0) {
            return 0;
        }
        final int bufferLength = buffer.length;
        int bytesToRead = bufferLength;
        if (length > 0 && length < bufferLength) {
            bytesToRead = (int) length;
        }
        int read;
        long totalRead = 0;
        while (bytesToRead > 0 && EOF != (read = input.read(buffer, 0, bytesToRead))) {
            output.write(buffer, 0, read);
            totalRead += read;
            if (length > 0) { // only adjust length if not reading to the end
                // Note the cast must work because buffer.length is an integer
                bytesToRead = (int) Math.min(length - totalRead, bufferLength);
            }
        }
        return totalRead;
    }

    /**
     * Copies bytes from an <code>InputStream</code> to chars on a
     * <code>Writer</code> using the default character encoding of the platform.
     * <p>
     * This method buffers the input internally, so there is no need to use a
     * <code>BufferedInputStream</code>.
     * <p>
     * This method uses {@link InputStreamReader}.
     *
     * @param input  the <code>InputStream</code> to read from
     * @param output the <code>Writer</code> to write to
     * @throws NullPointerException if the input or output is null
     * @throws IOException          if an I/O error occurs
     */
    public static void copy(final InputStream input, final Writer output)
            throws IOException {
        copy(input, output, Charset.defaultCharset());
    }

    /**
     * Copies bytes from an <code>InputStream</code> to chars on a
     * <code>Writer</code> using the specified character encoding.
     * <p>
     * This method buffers the input internally, so there is no need to use a
     * <code>BufferedInputStream</code>.
     * <p>
     * This method uses {@link InputStreamReader}.
     *
     * @param input         the <code>InputStream</code> to read from
     * @param output        the <code>Writer</code> to write to
     * @param inputEncoding the encoding to use for the input stream, null means platform default
     * @throws NullPointerException if the input or output is null
     * @throws IOException          if an I/O error occurs
     */
    public static void copy(final InputStream input, final Writer output, final Charset inputEncoding)
            throws IOException {
        final InputStreamReader in = new InputStreamReader(input, Charsets.getCharset(inputEncoding));
        copy(in, output);
    }

    /**
     * Copies bytes from an <code>InputStream</code> to chars on a
     * <code>Writer</code> using the specified character encoding.
     * <p>
     * This method buffers the input internally, so there is no need to use a
     * <code>BufferedInputStream</code>.
     * <p>
     * Character encoding names can be found at
     * <a href="http://www.iana.org/assignments/character-sets">IANA</a>.
     * <p>
     * This method uses {@link InputStreamReader}.
     *
     * @param input         the <code>InputStream</code> to read from
     * @param output        the <code>Writer</code> to write to
     * @param inputEncoding the encoding to use for the InputStream, null means platform default
     * @throws NullPointerException                         if the input or output is null
     * @throws IOException                                  if an I/O error occurs
     * @throws java.nio.charset.UnsupportedCharsetException thrown instead of {@link java.io
     *                                                      .UnsupportedEncodingException} in version 2.2 if the
     *                                                      encoding is not supported.
     */
    public static void copy(final InputStream input, final Writer output, final String inputEncoding)
            throws IOException {
        copy(input, output, Charsets.getCharset(inputEncoding));
    }

    // copy from Reader
    //-----------------------------------------------------------------------

    /**
     * Copies chars from a <code>Reader</code> to a <code>Writer</code>.
     * <p>
     * This method buffers the input internally, so there is no need to use a
     * <code>BufferedReader</code>.
     * <p>
     * Large streams (over 2GB) will return a chars copied value of
     * <code>-1</code> after the copy has completed since the correct
     * number of chars cannot be returned as an int. For large streams
     * use the <code>copyLarge(Reader, Writer)</code> method.
     *
     * @param input  the <code>Reader</code> to read from
     * @param output the <code>Writer</code> to write to
     * @return the number of characters copied, or -1 if &gt; Integer.MAX_VALUE
     * @throws NullPointerException if the input or output is null
     * @throws IOException          if an I/O error occurs
     */
    public static int copy(final Reader input, final Writer output) throws IOException {
        final long count = copyLarge(input, output);
        if (count > Integer.MAX_VALUE) {
            return -1;
        }
        return (int) count;
    }

    /**
     * Copies chars from a large (over 2GB) <code>Reader</code> to a <code>Writer</code>.
     * <p>
     * This method buffers the input internally, so there is no need to use a
     * <code>BufferedReader</code>.
     * <p>
     * The buffer size is given by {@link #DEFAULT_BUFFER_SIZE}.
     *
     * @param input  the <code>Reader</code> to read from
     * @param output the <code>Writer</code> to write to
     * @return the number of characters copied
     * @throws NullPointerException if the input or output is null
     * @throws IOException          if an I/O error occurs
     */
    public static long copyLarge(final Reader input, final Writer output) throws IOException {
        return copyLarge(input, output, new char[DEFAULT_BUFFER_SIZE]);
    }

    /**
     * Copies chars from a large (over 2GB) <code>Reader</code> to a <code>Writer</code>.
     * <p>
     * This method uses the provided buffer, so there is no need to use a
     * <code>BufferedReader</code>.
     * <p>
     *
     * @param input  the <code>Reader</code> to read from
     * @param output the <code>Writer</code> to write to
     * @param buffer the buffer to be used for the copy
     * @return the number of characters copied
     * @throws NullPointerException if the input or output is null
     * @throws IOException          if an I/O error occurs
     */
    public static long copyLarge(final Reader input, final Writer output, final char[] buffer) throws IOException {
        long count = 0;
        int n;
        while (EOF != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }

    /**
     * Copies some or all chars from a large (over 2GB) <code>InputStream</code> to an
     * <code>OutputStream</code>, optionally skipping input chars.
     * <p>
     * This method buffers the input internally, so there is no need to use a
     * <code>BufferedReader</code>.
     * <p>
     * The buffer size is given by {@link #DEFAULT_BUFFER_SIZE}.
     *
     * @param input       the <code>Reader</code> to read from
     * @param output      the <code>Writer</code> to write to
     * @param inputOffset : number of chars to skip from input before copying
     *                    -ve values are ignored
     * @param length      : number of chars to copy. -ve means all
     * @return the number of chars copied
     * @throws NullPointerException if the input or output is null
     * @throws IOException          if an I/O error occurs
     */
    public static long copyLarge(final Reader input, final Writer output, final long inputOffset, final long length)
            throws IOException {
        return copyLarge(input, output, inputOffset, length, new char[DEFAULT_BUFFER_SIZE]);
    }

    /**
     * Copies some or all chars from a large (over 2GB) <code>InputStream</code> to an
     * <code>OutputStream</code>, optionally skipping input chars.
     * <p>
     * This method uses the provided buffer, so there is no need to use a
     * <code>BufferedReader</code>.
     * <p>
     *
     * @param input       the <code>Reader</code> to read from
     * @param output      the <code>Writer</code> to write to
     * @param inputOffset : number of chars to skip from input before copying
     *                    -ve values are ignored
     * @param length      : number of chars to copy. -ve means all
     * @param buffer      the buffer to be used for the copy
     * @return the number of chars copied
     * @throws NullPointerException if the input or output is null
     * @throws IOException          if an I/O error occurs
     */
    public static long copyLarge(final Reader input, final Writer output, final long inputOffset, final long length,
                                 final char[] buffer)
            throws IOException {
        if (inputOffset > 0) {
            skipFully(input, inputOffset);
        }
        if (length == 0) {
            return 0;
        }
        int bytesToRead = buffer.length;
        if (length > 0 && length < buffer.length) {
            bytesToRead = (int) length;
        }
        int read;
        long totalRead = 0;
        while (bytesToRead > 0 && EOF != (read = input.read(buffer, 0, bytesToRead))) {
            output.write(buffer, 0, read);
            totalRead += read;
            if (length > 0) { // only adjust length if not reading to the end
                // Note the cast must work because buffer.length is an integer
                bytesToRead = (int) Math.min(length - totalRead, buffer.length);
            }
        }
        return totalRead;
    }

    /**
     * Copies chars from a <code>Reader</code> to bytes on an
     * <code>OutputStream</code> using the default character encoding of the
     * platform, and calling flush.
     * <p>
     * This method buffers the input internally, so there is no need to use a
     * <code>BufferedReader</code>.
     * <p>
     * Due to the implementation of OutputStreamWriter, this method performs a
     * flush.
     * <p>
     * This method uses {@link OutputStreamWriter}.
     *
     * @param input  the <code>Reader</code> to read from
     * @param output the <code>OutputStream</code> to write to
     * @throws NullPointerException if the input or output is null
     * @throws IOException          if an I/O error occurs
     */
    public static void copy(final Reader input, final OutputStream output)
            throws IOException {
        copy(input, output, Charset.defaultCharset());
    }

    /**
     * Copies chars from a <code>Reader</code> to bytes on an
     * <code>OutputStream</code> using the specified character encoding, and
     * calling flush.
     * <p>
     * This method buffers the input internally, so there is no need to use a
     * <code>BufferedReader</code>.
     * </p>
     * <p>
     * Due to the implementation of OutputStreamWriter, this method performs a
     * flush.
     * </p>
     * <p>
     * This method uses {@link OutputStreamWriter}.
     * </p>
     *
     * @param input          the <code>Reader</code> to read from
     * @param output         the <code>OutputStream</code> to write to
     * @param outputEncoding the encoding to use for the OutputStream, null means platform default
     * @throws NullPointerException if the input or output is null
     * @throws IOException          if an I/O error occurs
     */
    public static void copy(final Reader input, final OutputStream output, final Charset outputEncoding)
            throws IOException {
        final OutputStreamWriter out = new OutputStreamWriter(output, Charsets.getCharset(outputEncoding));
        copy(input, out);
        // XXX Unless anyone is planning on rewriting OutputStreamWriter,
        // we have to flush here.
        out.flush();
    }

    /**
     * Copies chars from a <code>Reader</code> to bytes on an
     * <code>OutputStream</code> using the specified character encoding, and
     * calling flush.
     * <p>
     * This method buffers the input internally, so there is no need to use a
     * <code>BufferedReader</code>.
     * <p>
     * Character encoding names can be found at
     * <a href="http://www.iana.org/assignments/character-sets">IANA</a>.
     * <p>
     * Due to the implementation of OutputStreamWriter, this method performs a
     * flush.
     * <p>
     * This method uses {@link OutputStreamWriter}.
     *
     * @param input          the <code>Reader</code> to read from
     * @param output         the <code>OutputStream</code> to write to
     * @param outputEncoding the encoding to use for the OutputStream, null means platform default
     * @throws NullPointerException                         if the input or output is null
     * @throws IOException                                  if an I/O error occurs
     * @throws java.nio.charset.UnsupportedCharsetException thrown instead of {@link java.io
     *                                                      .UnsupportedEncodingException} in version 2.2 if the
     *                                                      encoding is not supported.
     */
    public static void copy(final Reader input, final OutputStream output, final String outputEncoding)
            throws IOException {
        copy(input, output, Charsets.getCharset(outputEncoding));
    }

    // content equals
    //-----------------------------------------------------------------------

    /**
     * Compares the contents of two Streams to determine if they are equal or
     * not.
     * <p>
     * This method buffers the input internally using
     * <code>BufferedInputStream</code> if they are not already buffered.
     *
     * @param input1 the first stream
     * @param input2 the second stream
     * @return true if the content of the streams are equal or they both don't
     * exist, false otherwise
     * @throws NullPointerException if either input is null
     * @throws IOException          if an I/O error occurs
     */
    public static boolean contentEquals(InputStream input1, InputStream input2)
            throws IOException {
        if (input1 == input2) {
            return true;
        }
        if (!(input1 instanceof com.jn.langx.io.stream.BufferedInputStream)) {
            input1 = new com.jn.langx.io.stream.BufferedInputStream(input1);
        }
        if (!(input2 instanceof com.jn.langx.io.stream.BufferedInputStream)) {
            input2 = new com.jn.langx.io.stream.BufferedInputStream(input2);
        }

        int ch = input1.read();
        while (EOF != ch) {
            final int ch2 = input2.read();
            if (ch != ch2) {
                return false;
            }
            ch = input1.read();
        }

        final int ch2 = input2.read();
        return ch2 == EOF;
    }

    /**
     * Compares the contents of two Readers to determine if they are equal or
     * not.
     * <p>
     * This method buffers the input internally using
     * <code>BufferedReader</code> if they are not already buffered.
     *
     * @param input1 the first reader
     * @param input2 the second reader
     * @return true if the content of the readers are equal or they both don't
     * exist, false otherwise
     * @throws NullPointerException if either input is null
     * @throws IOException          if an I/O error occurs
     */
    public static boolean contentEquals(Reader input1, Reader input2)
            throws IOException {
        if (input1 == input2) {
            return true;
        }

        input1 = toBufferedReader(input1);
        input2 = toBufferedReader(input2);

        int ch = input1.read();
        while (EOF != ch) {
            final int ch2 = input2.read();
            if (ch != ch2) {
                return false;
            }
            ch = input1.read();
        }

        final int ch2 = input2.read();
        return ch2 == EOF;
    }

    /**
     * Compares the contents of two Readers to determine if they are equal or
     * not, ignoring EOL characters.
     * <p>
     * This method buffers the input internally using
     * <code>BufferedReader</code> if they are not already buffered.
     *
     * @param input1 the first reader
     * @param input2 the second reader
     * @return true if the content of the readers are equal (ignoring EOL differences),  false otherwise
     * @throws NullPointerException if either input is null
     * @throws IOException          if an I/O error occurs
     */
    public static boolean contentEqualsIgnoreEOL(final Reader input1, final Reader input2)
            throws IOException {
        if (input1 == input2) {
            return true;
        }
        final BufferedReader br1 = toBufferedReader(input1);
        final BufferedReader br2 = toBufferedReader(input2);

        String line1 = br1.readLine();
        String line2 = br2.readLine();
        while (line1 != null && Objs.equals(line1, line2)) {
            line1 = br1.readLine();
            line2 = br2.readLine();
        }
        if (line1 == null) {
            return line2 == null;
        } else {
            return Objs.equals(line1, line2);
        }
    }

    /**
     * Skips bytes from an input byte stream.
     * This implementation guarantees that it will read as many bytes
     * as possible before giving up; this may not always be the case for
     * skip() implementations in subclasses of {@link InputStream}.
     * <p>
     * Note that the implementation uses {@link InputStream#read(byte[], int, int)} rather
     * than delegating to {@link InputStream#skip(long)}.
     * This means that the method may be considerably less efficient than using the actual skip implementation,
     * this is done to guarantee that the correct number of bytes are skipped.
     * </p>
     *
     * @param input  byte stream to skip
     * @param toSkip number of bytes to skip.
     * @return number of bytes actually skipped.
     * @throws IOException              if there is a problem reading the file
     * @throws IllegalArgumentException if toSkip is negative
     * @see InputStream#skip(long)
     * @see <a href="https://issues.apache.org/jira/browse/IO-203">IO-203 - Add skipFully() method for InputStreams</a>
     */
    public static long skip(final InputStream input, final long toSkip) throws IOException {
        if (toSkip < 0) {
            throw new IllegalArgumentException("Skip count must be non-negative, actual: " + toSkip);
        }
        /*
         * N.B. no need to synchronize this because: - we don't care if the buffer is created multiple times (the data
         * is ignored) - we always use the same size buffer, so if it it is recreated it will still be OK (if the buffer
         * size were variable, we would need to synch. to ensure some other thread did not create a smaller one)
         */
        if (SKIP_BYTE_BUFFER == null) {
            SKIP_BYTE_BUFFER = new byte[SKIP_BUFFER_SIZE];
        }
        long remain = toSkip;
        while (remain > 0) {
            // See https://issues.apache.org/jira/browse/IO-203 for why we use read() rather than delegating to skip()
            final long n = input.read(SKIP_BYTE_BUFFER, 0, (int) Math.min(remain, SKIP_BUFFER_SIZE));
            if (n < 0) { // EOF
                break;
            }
            remain -= n;
        }
        return toSkip - remain;
    }

    /**
     * Skips bytes from a ReadableByteChannel.
     * This implementation guarantees that it will read as many bytes
     * as possible before giving up.
     *
     * @param input  ReadableByteChannel to skip
     * @param toSkip number of bytes to skip.
     * @return number of bytes actually skipped.
     * @throws IOException              if there is a problem reading the ReadableByteChannel
     * @throws IllegalArgumentException if toSkip is negative
     */
    public static long skip(final ReadableByteChannel input, final long toSkip) throws IOException {
        if (toSkip < 0) {
            throw new IllegalArgumentException("Skip count must be non-negative, actual: " + toSkip);
        }
        final ByteBuffer skipByteBuffer = ByteBuffer.allocate((int) Math.min(toSkip, SKIP_BUFFER_SIZE));
        long remain = toSkip;
        while (remain > 0) {
            skipByteBuffer.position(0);
            skipByteBuffer.limit((int) Math.min(remain, SKIP_BUFFER_SIZE));
            final int n = input.read(skipByteBuffer);
            if (n == EOF) {
                break;
            }
            remain -= n;
        }
        return toSkip - remain;
    }

    /**
     * Skips characters from an input character stream.
     * This implementation guarantees that it will read as many characters
     * as possible before giving up; this may not always be the case for
     * skip() implementations in subclasses of {@link Reader}.
     * <p>
     * Note that the implementation uses {@link Reader#read(char[], int, int)} rather
     * than delegating to {@link Reader#skip(long)}.
     * This means that the method may be considerably less efficient than using the actual skip implementation,
     * this is done to guarantee that the correct number of characters are skipped.
     * </p>
     *
     * @param input  character stream to skip
     * @param toSkip number of characters to skip.
     * @return number of characters actually skipped.
     * @throws IOException              if there is a problem reading the file
     * @throws IllegalArgumentException if toSkip is negative
     * @see Reader#skip(long)
     * @see <a href="https://issues.apache.org/jira/browse/IO-203">IO-203 - Add skipFully() method for InputStreams</a>
     */
    public static long skip(final Reader input, final long toSkip) throws IOException {
        if (toSkip < 0) {
            throw new IllegalArgumentException("Skip count must be non-negative, actual: " + toSkip);
        }
        /*
         * N.B. no need to synchronize this because: - we don't care if the buffer is created multiple times (the data
         * is ignored) - we always use the same size buffer, so if it it is recreated it will still be OK (if the buffer
         * size were variable, we would need to synch. to ensure some other thread did not create a smaller one)
         */
        if (SKIP_CHAR_BUFFER == null) {
            SKIP_CHAR_BUFFER = new char[SKIP_BUFFER_SIZE];
        }
        long remain = toSkip;
        while (remain > 0) {
            // See https://issues.apache.org/jira/browse/IO-203 for why we use read() rather than delegating to skip()
            final long n = input.read(SKIP_CHAR_BUFFER, 0, (int) Math.min(remain, SKIP_BUFFER_SIZE));
            if (n < 0) { // EOF
                break;
            }
            remain -= n;
        }
        return toSkip - remain;
    }

    /**
     * Skips the requested number of bytes or fail if there are not enough left.
     * <p>
     * This allows for the possibility that {@link InputStream#skip(long)} may
     * not skip as many bytes as requested (most likely because of reaching EOF).
     * <p>
     * Note that the implementation uses {@link #skip(InputStream, long)}.
     * This means that the method may be considerably less efficient than using the actual skip implementation,
     * this is done to guarantee that the correct number of characters are skipped.
     * </p>
     *
     * @param input  stream to skip
     * @param toSkip the number of bytes to skip
     * @throws IOException              if there is a problem reading the file
     * @throws IllegalArgumentException if toSkip is negative
     * @throws EOFException             if the number of bytes skipped was incorrect
     * @see InputStream#skip(long)
     */
    public static void skipFully(final InputStream input, final long toSkip) throws IOException {
        if (toSkip < 0) {
            throw new IllegalArgumentException("Bytes to skip must not be negative: " + toSkip);
        }
        final long skipped = skip(input, toSkip);
        if (skipped != toSkip) {
            throw new EOFException("Bytes to skip: " + toSkip + " actual: " + skipped);
        }
    }

    /**
     * Skips the requested number of bytes or fail if there are not enough left.
     *
     * @param input  ReadableByteChannel to skip
     * @param toSkip the number of bytes to skip
     * @throws IOException              if there is a problem reading the ReadableByteChannel
     * @throws IllegalArgumentException if toSkip is negative
     * @throws EOFException             if the number of bytes skipped was incorrect
     */
    public static void skipFully(final ReadableByteChannel input, final long toSkip) throws IOException {
        if (toSkip < 0) {
            throw new IllegalArgumentException("Bytes to skip must not be negative: " + toSkip);
        }
        final long skipped = skip(input, toSkip);
        if (skipped != toSkip) {
            throw new EOFException("Bytes to skip: " + toSkip + " actual: " + skipped);
        }
    }

    /**
     * Skips the requested number of characters or fail if there are not enough left.
     * <p>
     * This allows for the possibility that {@link Reader#skip(long)} may
     * not skip as many characters as requested (most likely because of reaching EOF).
     * <p>
     * Note that the implementation uses {@link #skip(Reader, long)}.
     * This means that the method may be considerably less efficient than using the actual skip implementation,
     * this is done to guarantee that the correct number of characters are skipped.
     * </p>
     *
     * @param input  stream to skip
     * @param toSkip the number of characters to skip
     * @throws IOException              if there is a problem reading the file
     * @throws IllegalArgumentException if toSkip is negative
     * @throws EOFException             if the number of characters skipped was incorrect
     * @see Reader#skip(long)
     */
    public static void skipFully(final Reader input, final long toSkip) throws IOException {
        final long skipped = skip(input, toSkip);
        if (skipped != toSkip) {
            throw new EOFException("Chars to skip: " + toSkip + " actual: " + skipped);
        }
    }


    /**
     * Reads characters from an input character stream.
     * This implementation guarantees that it will read as many characters
     * as possible before giving up; this may not always be the case for
     * subclasses of {@link Reader}.
     *
     * @param input  where to read input from
     * @param buffer destination
     * @param offset initial offset into buffer
     * @param length length to read, must be &gt;= 0
     * @return actual length read; may be less than requested if EOF was reached
     * @throws IOException if a read error occurs
     */
    public static int read(final Reader input, final char[] buffer, final int offset, final int length)
            throws IOException {
        if (length < 0) {
            throw new IllegalArgumentException("Length must not be negative: " + length);
        }
        int remaining = length;
        while (remaining > 0) {
            final int location = length - remaining;
            final int count = input.read(buffer, offset + location, remaining);
            if (EOF == count) { // EOF
                break;
            }
            remaining -= count;
        }
        return length - remaining;
    }

    /**
     * Reads characters from an input character stream.
     * This implementation guarantees that it will read as many characters
     * as possible before giving up; this may not always be the case for
     * subclasses of {@link Reader}.
     *
     * @param input  where to read input from
     * @param buffer destination
     * @return actual length read; may be less than requested if EOF was reached
     * @throws IOException if a read error occurs
     */
    public static int read(final Reader input, final char[] buffer) throws IOException {
        return read(input, buffer, 0, buffer.length);
    }

    /**
     * Reads bytes from an input stream.
     * This implementation guarantees that it will read as many bytes
     * as possible before giving up; this may not always be the case for
     * subclasses of {@link InputStream}.
     *
     * @param input  where to read input from
     * @param buffer destination
     * @param offset initial offset into buffer
     * @param length length to read, must be &gt;= 0
     * @return actual length read; may be less than requested if EOF was reached
     * @throws IOException if a read error occurs
     */
    public static int read(final InputStream input, final byte[] buffer, final int offset, final int length)
            throws IOException {
        if (length < 0) {
            throw new IllegalArgumentException("Length must not be negative: " + length);
        }
        int remaining = length;
        while (remaining > 0) {
            final int location = length - remaining;
            final int count = input.read(buffer, offset + location, remaining);
            if (EOF == count) { // EOF
                break;
            }
            remaining -= count;
        }
        return length - remaining;
    }

    /**
     * Reads bytes from an input stream.
     * This implementation guarantees that it will read as many bytes
     * as possible before giving up; this may not always be the case for
     * subclasses of {@link InputStream}.
     *
     * @param input  where to read input from
     * @param buffer destination
     * @return actual length read; may be less than requested if EOF was reached
     * @throws IOException if a read error occurs
     */
    public static int read(final InputStream input, final byte[] buffer) throws IOException {
        return read(input, buffer, 0, buffer.length);
    }

    /**
     * Reads bytes from a ReadableByteChannel.
     * <p>
     * This implementation guarantees that it will read as many bytes
     * as possible before giving up; this may not always be the case for
     * subclasses of {@link ReadableByteChannel}.
     *
     * @param input  the byte channel to read
     * @param buffer byte buffer destination
     * @return the actual length read; may be less than requested if EOF was reached
     * @throws IOException if a read error occurs
     */
    public static int read(final ReadableByteChannel input, final ByteBuffer buffer) throws IOException {
        final int length = buffer.remaining();
        while (buffer.remaining() > 0) {
            final int count = input.read(buffer);
            if (EOF == count) { // EOF
                break;
            }
        }
        return length - buffer.remaining();
    }

    /**
     * @param byteBuffer the bytes source
     * @param position   the position in the bytes source
     * @param maxLength  the max length will read
     */
    public static byte[] read(@NonNull ByteBuffer byteBuffer, int position, int maxLength) {
        if (byteBuffer == null) {
            return new byte[0];
        }
        Preconditions.checkArgument(position >= 0, "the position argument is wrong: {}", position);
        if (maxLength < 0) {
            maxLength = Integer.MAX_VALUE;
        }

        int remaining = byteBuffer.limit() - position;
        if (remaining < 1) {
            return new byte[0];
        }

        byteBuffer.position(position);

        int length = Maths.min(remaining, maxLength);

        byte[] bytes = new byte[length];
        byteBuffer.get(bytes);
        return bytes;
    }

    /**
     * Reads the requested number of characters or fail if there are not enough left.
     * <p>
     * This allows for the possibility that {@link Reader#read(char[], int, int)} may
     * not read as many characters as requested (most likely because of reaching EOF).
     *
     * @param input  where to read input from
     * @param buffer destination
     * @param offset initial offset into buffer
     * @param length length to read, must be &gt;= 0
     * @throws IOException              if there is a problem reading the file
     * @throws IllegalArgumentException if length is negative
     * @throws EOFException             if the number of characters read was incorrect
     */
    public static void readFully(final Reader input, final char[] buffer, final int offset, final int length)
            throws IOException {
        final int actual = read(input, buffer, offset, length);
        if (actual != length) {
            throw new EOFException("Length to read: " + length + " actual: " + actual);
        }
    }

    /**
     * Reads the requested number of characters or fail if there are not enough left.
     * <p>
     * This allows for the possibility that {@link Reader#read(char[], int, int)} may
     * not read as many characters as requested (most likely because of reaching EOF).
     *
     * @param input  where to read input from
     * @param buffer destination
     * @throws IOException              if there is a problem reading the file
     * @throws IllegalArgumentException if length is negative
     * @throws EOFException             if the number of characters read was incorrect
     */
    public static void readFully(final Reader input, final char[] buffer) throws IOException {
        readFully(input, buffer, 0, buffer.length);
    }

    /**
     * Reads the requested number of bytes or fail if there are not enough left.
     * <p>
     * This allows for the possibility that {@link InputStream#read(byte[], int, int)} may
     * not read as many bytes as requested (most likely because of reaching EOF).
     *
     * @param input  where to read input from
     * @param buffer destination
     * @param offset initial offset into buffer
     * @param length length to read, must be &gt;= 0
     * @throws IOException              if there is a problem reading the file
     * @throws IllegalArgumentException if length is negative
     * @throws EOFException             if the number of bytes read was incorrect
     */
    public static void readFully(final InputStream input, final byte[] buffer, final int offset, final int length)
            throws IOException {
        final int actual = read(input, buffer, offset, length);
        if (actual != length) {
            throw new EOFException("Length to read: " + length + " actual: " + actual);
        }
    }

    /**
     * Reads the requested number of bytes or fail if there are not enough left.
     * <p>
     * This allows for the possibility that {@link InputStream#read(byte[], int, int)} may
     * not read as many bytes as requested (most likely because of reaching EOF).
     *
     * @param input  where to read input from
     * @param buffer destination
     * @throws IOException              if there is a problem reading the file
     * @throws IllegalArgumentException if length is negative
     * @throws EOFException             if the number of bytes read was incorrect
     */
    public static void readFully(final InputStream input, final byte[] buffer) throws IOException {
        readFully(input, buffer, 0, buffer.length);
    }

    /**
     * Reads the requested number of bytes or fail if there are not enough left.
     * <p>
     * This allows for the possibility that {@link InputStream#read(byte[], int, int)} may
     * not read as many bytes as requested (most likely because of reaching EOF).
     *
     * @param input  where to read input from
     * @param length length to read, must be &gt;= 0
     * @return the bytes read from input
     * @throws IOException              if there is a problem reading the file
     * @throws IllegalArgumentException if length is negative
     * @throws EOFException             if the number of bytes read was incorrect
     */
    public static byte[] readFully(final InputStream input, final int length) throws IOException {
        final byte[] buffer = new byte[length];
        readFully(input, buffer, 0, buffer.length);
        return buffer;
    }

    /**
     * Reads the requested number of bytes or fail if there are not enough left.
     * <p>
     * This allows for the possibility that {@link ReadableByteChannel#read(ByteBuffer)} may
     * not read as many bytes as requested (most likely because of reaching EOF).
     *
     * @param input  the byte channel to read
     * @param buffer byte buffer destination
     * @throws IOException  if there is a problem reading the file
     * @throws EOFException if the number of bytes read was incorrect
     */
    public static void readFully(final ReadableByteChannel input, final ByteBuffer buffer) throws IOException {
        final int expected = buffer.remaining();
        final int actual = read(input, buffer);
        if (actual != expected) {
            throw new EOFException("Length to read: " + expected + " actual: " + actual);
        }
    }

    public static int getRemaining(InputStream inputStream) {
        Preconditions.checkNotNull(inputStream);
        try {
            return inputStream.available();
        } catch (IOException ex) {
            throw Throwables.wrapAsRuntimeIOException(ex);
        }
    }

    // readLines
    //-----------------------------------------------------------------------
    public static List<String> readLines(File file) throws IOException {
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            return readLines(inputStream);
        } finally {
            IOs.close(inputStream);
        }
    }

    public static List<String> readLines(File file, Charset charset) throws IOException {
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            return readLines(inputStream, charset);
        } finally {
            IOs.close(inputStream);
        }
    }

    /**
     * Gets the contents of an <code>InputStream</code> as a list of Strings,
     * one entry per line, using the default character encoding of the platform.
     * <p>
     * This method buffers the input internally, so there is no need to use a
     * <code>BufferedInputStream</code>.
     *
     * @param input the <code>InputStream</code> to read from, not null
     * @return the list of Strings, never null
     * @throws NullPointerException if the input is null
     * @throws IOException          if an I/O error occurs
     */
    public static List<String> readLines(final InputStream input) throws IOException {
        return readLines(input, Charset.defaultCharset());
    }

    /**
     * Gets the contents of an <code>InputStream</code> as a list of Strings,
     * one entry per line, using the specified character encoding.
     * <p>
     * This method buffers the input internally, so there is no need to use a
     * <code>BufferedInputStream</code>.
     *
     * @param input    the <code>InputStream</code> to read from, not null
     * @param encoding the encoding to use, null means platform default
     * @return the list of Strings, never null
     * @throws NullPointerException if the input is null
     * @throws IOException          if an I/O error occurs
     */
    public static List<String> readLines(final InputStream input, final Charset encoding) throws IOException {
        final InputStreamReader reader = new InputStreamReader(input, Charsets.getCharset(encoding));
        return readLines(reader);
    }

    /**
     * Gets the contents of an <code>InputStream</code> as a list of Strings,
     * one entry per line, using the specified character encoding.
     * <p>
     * Character encoding names can be found at
     * <a href="http://www.iana.org/assignments/character-sets">IANA</a>.
     * <p>
     * This method buffers the input internally, so there is no need to use a
     * <code>BufferedInputStream</code>.
     *
     * @param input    the <code>InputStream</code> to read from, not null
     * @param encoding the encoding to use, null means platform default
     * @return the list of Strings, never null
     * @throws NullPointerException                         if the input is null
     * @throws IOException                                  if an I/O error occurs
     * @throws java.nio.charset.UnsupportedCharsetException thrown instead of {@link java.io
     *                                                      .UnsupportedEncodingException} in version 2.2 if the
     *                                                      encoding is not supported.
     */
    public static List<String> readLines(final InputStream input, final String encoding) throws IOException {
        return readLines(input, Charsets.getCharset(encoding));
    }

    /**
     * Gets the contents of a <code>Reader</code> as a list of Strings,
     * one entry per line.
     * <p>
     * This method buffers the input internally, so there is no need to use a
     * <code>BufferedReader</code>.
     *
     * @param input the <code>Reader</code> to read from, not null
     * @return the list of Strings, never null
     * @throws NullPointerException if the input is null
     * @throws IOException          if an I/O error occurs
     */
    public static List<String> readLines(final Reader input) throws IOException {
        final BufferedReader reader = toBufferedReader(input);
        final List<String> list = new ArrayList<String>();
        String line = reader.readLine();
        while (line != null) {
            list.add(line);
            line = reader.readLine();
        }
        return list;
    }

    // lineIterator
    //-----------------------------------------------------------------------

    /**
     * Returns an Iterator for the lines in a <code>Reader</code>.
     * <p>
     * <code>LineIterator</code> holds a reference to the open
     * <code>Reader</code> specified here. When you have finished with the
     * iterator you should close the reader to free internal resources.
     * This can be done by closing the reader directly, or by calling
     * {@link LineIterator#close()}.
     * <p>
     * The recommended usage pattern is:
     * <pre>
     * try {
     *   LineIterator it = IOUtils.lineIterator(reader);
     *   while (it.hasNext()) {
     *     String line = it.nextLine();
     *     /// do something with line
     *   }
     * } finally {
     *   IOUtils.closeQuietly(reader);
     * }
     * </pre>
     *
     * @param reader the <code>Reader</code> to read from, not null
     * @return an Iterator of the lines in the reader, never null
     * @throws IllegalArgumentException if the reader is null
     */
    public static LineIterator lineIterator(final Reader reader) {
        return new LineIterator(reader);
    }

    /**
     * Returns an Iterator for the lines in an <code>InputStream</code>, using
     * the character encoding specified (or default encoding if null).
     * <p>
     * <code>LineIterator</code> holds a reference to the open
     * <code>InputStream</code> specified here. When you have finished with
     * the iterator you should close the stream to free internal resources.
     * This can be done by closing the stream directly, or by calling
     * {@link LineIterator#close()}.
     * <p>
     * The recommended usage pattern is:
     * <pre>
     * try {
     *   LineIterator it = IOUtils.lineIterator(stream, charset);
     *   while (it.hasNext()) {
     *     String line = it.nextLine();
     *     /// do something with line
     *   }
     * } finally {
     *   IOUtils.closeQuietly(stream);
     * }
     * </pre>
     *
     * @param input    the <code>InputStream</code> to read from, not null
     * @param encoding the encoding to use, null means platform default
     * @return an Iterator of the lines in the reader, never null
     * @throws IllegalArgumentException if the input is null
     * @throws IOException              if an I/O error occurs, such as if the encoding is invalid
     */
    public static LineIterator lineIterator(final InputStream input, final Charset encoding) throws IOException {
        return new LineIterator(new InputStreamReader(input, Charsets.getCharset(encoding)));
    }

    /**
     * Returns an Iterator for the lines in an <code>InputStream</code>, using
     * the character encoding specified (or default encoding if null).
     * <p>
     * <code>LineIterator</code> holds a reference to the open
     * <code>InputStream</code> specified here. When you have finished with
     * the iterator you should close the stream to free internal resources.
     * This can be done by closing the stream directly, or by calling
     * {@link LineIterator#close()}.
     * <p>
     * The recommended usage pattern is:
     * <pre>
     * try {
     *   LineIterator it = IOUtils.lineIterator(stream, "UTF-8");
     *   while (it.hasNext()) {
     *     String line = it.nextLine();
     *     /// do something with line
     *   }
     * } finally {
     *   IOUtils.closeQuietly(stream);
     * }
     * </pre>
     *
     * @param input    the <code>InputStream</code> to read from, not null
     * @param encoding the encoding to use, null means platform default
     * @return an Iterator of the lines in the reader, never null
     * @throws IllegalArgumentException                     if the input is null
     * @throws IOException                                  if an I/O error occurs, such as if the encoding is invalid
     * @throws java.nio.charset.UnsupportedCharsetException thrown instead of {@link java.io
     *                                                      .UnsupportedEncodingException} in version 2.2 if the
     *                                                      encoding is not supported.
     */
    public static LineIterator lineIterator(final InputStream input, final String encoding) throws IOException {
        return lineIterator(input, Charsets.getCharset(encoding));
    }

    /**
     * {@link InputStream#read()} 返回值 范围是 0-255， -1 代表流结束。
     * <p>
     * 而通常我们读到的数据都是以 byte 范围的。java中 byte 范围的数据是在 -128 ~ 127
     * <p>
     * 也就是说，存在 -1 ~ -128 这些数，也就是执行 read()一旦返回 -1，就会中断流的读取，而实际上流并未结束。
     * 解决该问题的办法，就是两者范围统一。
     */
    public static int filterInputStreamRead(byte theByte) {
        return theByte < 0 ? (theByte + 256) : theByte;
    }

    public static int filterInputStreamRead(int theByte) {
        return filterInputStreamRead((byte) theByte);
    }

    private IOs() {

    }
}
