package com.jn.langx.util.logging;

import org.slf4j.spi.LocationAwareLogger;


public enum Level {

    ERROR(LocationAwareLogger.ERROR_INT, "ERROR"),
    WARN(LocationAwareLogger.WARN_INT, "WARN"),
    INFO(LocationAwareLogger.INFO_INT, "INFO"),
    DEBUG(LocationAwareLogger.DEBUG_INT, "DEBUG"),
    TRACE(LocationAwareLogger.TRACE_INT, "TRACE");

    private int levelInt;
    private String levelStr;

    Level(int i, String s) {
        levelInt = i;
        levelStr = s;
    }

    public int toInt() {
        return levelInt;
    }

    /**
     * Returns the string representation of this Level.
     */
    public String toString() {
        return levelStr;
    }

}

