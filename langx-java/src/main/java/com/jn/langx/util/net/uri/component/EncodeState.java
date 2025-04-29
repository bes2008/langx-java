package com.jn.langx.util.net.uri.component;

enum EncodeState {

    /**
     * Not encoded.
     */
    RAW,

    /**
     * URI vars expanded first and then each URI component encoded by
     * quoting only illegal characters within that URI component.
     */
    FULLY_ENCODED,

    /**
     * URI template encoded first by quoting illegal characters only, and
     * then URI vars encoded more strictly when expanded, by quoting both
     * illegal chars and chars with reserved meaning.
     */
    TEMPLATE_ENCODED;


    public boolean isEncoded() {
        return this.equals(FULLY_ENCODED) || this.equals(TEMPLATE_ENCODED);
    }
}