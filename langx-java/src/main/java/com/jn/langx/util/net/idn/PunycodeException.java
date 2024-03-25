package com.jn.langx.util.net.idn;

import com.jn.langx.codec.CodecException;

public class PunycodeException extends CodecException {
    public static String OVERFLOW = "Overflow.";
    public static String BAD_INPUT = "Bad input.";

    /**
     * Creates a new PunycodeException.
     *
     * @param m message.
     */
    public PunycodeException(String m)
    {
        super(m);
    }
}
