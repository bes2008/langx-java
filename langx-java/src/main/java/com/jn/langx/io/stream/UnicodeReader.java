package com.jn.langx.io.stream;


import com.jn.langx.util.io.Charsets;
import com.jn.langx.util.io.unicode.BOM;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;

/**
 * Generic unicode text reader, which will use BOM mark to identify the encoding to be used. If BOM
 * is not found then use a given default or system encoding.
 */
public class UnicodeReader extends Reader {

    PushbackInputStream pushbackInputStream;
    InputStreamReader internalReader = null;

    private static final int BOM_SIZE = 4;

    public UnicodeReader(InputStream in) {
        pushbackInputStream = new PushbackInputStream(in, BOM_SIZE);
    }

    /**
     * Get stream encoding or NULL if stream is uninitialized. Call init() or read() method to
     * initialize it.
     *
     * @return the name of the character encoding being used by this stream.
     */
    public String getEncoding() {
        return internalReader.getEncoding();
    }

    /**
     * Read-ahead four bytes and check for BOM marks. Extra bytes are unread back to the stream, only
     * BOM bytes are skipped.
     *
     * @throws IOException if InputStream cannot be created
     */
    protected void init() throws IOException {
        if (internalReader != null) {
            return;
        }

        Charset encoding;
        byte[] bomBytes = new byte[BOM_SIZE];
        int n, unread;
        n = pushbackInputStream.read(bomBytes, 0, bomBytes.length);
        if (n < bomBytes.length) {
            byte[] bs = new byte[n];
            System.arraycopy(bomBytes, 0, bs, 0, n);
            bomBytes = bs;
        }

        BOM bom = BOM.findBom(bomBytes);
        if (bom != null) {
            unread = n - bom.getBytes().length;
            encoding = Charsets.getCharset(bom.getName());
        } else {
            unread = n;
            encoding = Charsets.UTF_8;
        }

        if (unread > 0) {
            pushbackInputStream.unread(bomBytes, (n - unread), unread);
        }

        // Use given encoding
        CharsetDecoder decoder = encoding.newDecoder().onUnmappableCharacter(CodingErrorAction.REPORT);
        internalReader = new InputStreamReader(pushbackInputStream, decoder);
    }

    public void close() throws IOException {
        init();
        internalReader.close();
    }

    public int read(char[] cbuf, int off, int len) throws IOException {
        init();
        return internalReader.read(cbuf, off, len);
    }
}
