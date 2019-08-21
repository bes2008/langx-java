package com.jn.langx.util;

import static com.jn.langx.util.ByteProcessors.CARRIAGE_RETURN;
import static com.jn.langx.util.ByteProcessors.HTAB;
import static com.jn.langx.util.ByteProcessors.LINE_FEED;
import static com.jn.langx.util.ByteProcessors.SPACE;

/**
 * Provides a mechanism to iterate over a collection of bytes.
 */
public interface ByteProcessor {
    /**
     * A {@link io.netty.util.ByteProcessor} which finds the first appearance of a specific byte.
     */
    class IndexOfProcessor implements io.netty.util.ByteProcessor {
        private final byte byteToFind;

        public IndexOfProcessor(byte byteToFind) {
            this.byteToFind = byteToFind;
        }

        @Override
        public boolean process(byte value) {
            return value != byteToFind;
        }
    }

    /**
     * A {@link io.netty.util.ByteProcessor} which finds the first appearance which is not of a specific byte.
     */
    class IndexNotOfProcessor implements io.netty.util.ByteProcessor {
        private final byte byteToNotFind;

        public IndexNotOfProcessor(byte byteToNotFind) {
            this.byteToNotFind = byteToNotFind;
        }

        @Override
        public boolean process(byte value) {
            return value == byteToNotFind;
        }
    }

    /**
     * Aborts on a {@code NUL (0x00)}.
     */
    io.netty.util.ByteProcessor FIND_NUL = new io.netty.util.ByteProcessor.IndexOfProcessor((byte) 0);

    /**
     * Aborts on a non-{@code NUL (0x00)}.
     */
    io.netty.util.ByteProcessor FIND_NON_NUL = new io.netty.util.ByteProcessor.IndexNotOfProcessor((byte) 0);

    /**
     * Aborts on a {@code CR ('\r')}.
     */
    io.netty.util.ByteProcessor FIND_CR = new io.netty.util.ByteProcessor.IndexOfProcessor(CARRIAGE_RETURN);

    /**
     * Aborts on a non-{@code CR ('\r')}.
     */
    io.netty.util.ByteProcessor FIND_NON_CR = new io.netty.util.ByteProcessor.IndexNotOfProcessor(CARRIAGE_RETURN);

    /**
     * Aborts on a {@code LF ('\n')}.
     */
    io.netty.util.ByteProcessor FIND_LF = new io.netty.util.ByteProcessor.IndexOfProcessor(LINE_FEED);

    /**
     * Aborts on a non-{@code LF ('\n')}.
     */
    io.netty.util.ByteProcessor FIND_NON_LF = new io.netty.util.ByteProcessor.IndexNotOfProcessor(LINE_FEED);

    /**
     * Aborts on a semicolon {@code (';')}.
     */
    io.netty.util.ByteProcessor FIND_SEMI_COLON = new io.netty.util.ByteProcessor.IndexOfProcessor((byte) ';');

    /**
     * Aborts on a comma {@code (',')}.
     */
    io.netty.util.ByteProcessor FIND_COMMA = new io.netty.util.ByteProcessor.IndexOfProcessor((byte) ',');

    /**
     * Aborts on a ascii space character ({@code ' '}).
     */
    io.netty.util.ByteProcessor FIND_ASCII_SPACE = new io.netty.util.ByteProcessor.IndexOfProcessor(SPACE);

    /**
     * Aborts on a {@code CR ('\r')} or a {@code LF ('\n')}.
     */
    io.netty.util.ByteProcessor FIND_CRLF = new io.netty.util.ByteProcessor() {
        @Override
        public boolean process(byte value) {
            return value != CARRIAGE_RETURN && value != LINE_FEED;
        }
    };

    /**
     * Aborts on a byte which is neither a {@code CR ('\r')} nor a {@code LF ('\n')}.
     */
    io.netty.util.ByteProcessor FIND_NON_CRLF = new io.netty.util.ByteProcessor() {
        @Override
        public boolean process(byte value) {
            return value == CARRIAGE_RETURN || value == LINE_FEED;
        }
    };

    /**
     * Aborts on a linear whitespace (a ({@code ' '} or a {@code '\t'}).
     */
    io.netty.util.ByteProcessor FIND_LINEAR_WHITESPACE = new io.netty.util.ByteProcessor() {
        @Override
        public boolean process(byte value) {
            return value != SPACE && value != HTAB;
        }
    };

    /**
     * Aborts on a byte which is not a linear whitespace (neither {@code ' '} nor {@code '\t'}).
     */
    io.netty.util.ByteProcessor FIND_NON_LINEAR_WHITESPACE = new io.netty.util.ByteProcessor() {
        @Override
        public boolean process(byte value) {
            return value == SPACE || value == HTAB;
        }
    };

    /**
     * @return {@code true} if the processor wants to continue the loop and handle the next byte in the buffer.
     *         {@code false} if the processor wants to stop handling bytes and abort the loop.
     */
    boolean process(byte value) throws Exception;
}
