package com.jn.langx.util.io;


import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;

import java.io.IOException;
import java.io.OutputStream;

/**
 * This OutputStream writes all data to the famous <b>/dev/null</b>.
 * <p>
 * This output stream has no destination (file/socket etc.) and all
 * bytes written to it are ignored and lost.
 * </p>
 */
public class NullOutputStream extends OutputStream {

    /**
     * A singleton.
     */
    public static final NullOutputStream NULL_OUTPUT_STREAM = new NullOutputStream();

    /**
     * Does nothing - output to <code>/dev/null</code>.
     *
     * @param b   The bytes to write
     * @param off The start offset
     * @param len The number of bytes to write
     */
    @Override
    public void write(@Nullable final byte[] b, final int off, final int len) {
        //to /dev/null
    }

    /**
     * Does nothing - output to <code>/dev/null</code>.
     *
     * @param b The byte to write
     */
    @Override
    public void write(final int b) {
        //to /dev/null
    }

    /**
     * Does nothing - output to <code>/dev/null</code>.
     *
     * @param b The bytes to write
     */
    @Override
    public void write(final byte[] b) {
        //to /dev/null
    }

}