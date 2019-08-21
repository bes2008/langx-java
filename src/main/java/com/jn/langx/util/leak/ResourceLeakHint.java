package com.jn.langx.util.leak;


/**
 * A hint object that provides human-readable message for easier resource leak tracking.
 */
public interface ResourceLeakHint {
    /**
     * Returns a human-readable message that potentially enables easier resource leak tracking.
     */
    String toHintString();
}
