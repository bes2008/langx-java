package com.jn.langx.util.logging;

import com.jn.langx.util.enums.base.CommonEnum;
import com.jn.langx.util.enums.base.EnumDelegate;
import org.slf4j.spi.LocationAwareLogger;


public enum Level implements CommonEnum {

    ERROR(LocationAwareLogger.ERROR_INT, "ERROR"),
    WARN(LocationAwareLogger.WARN_INT, "WARN"),
    INFO(LocationAwareLogger.INFO_INT, "INFO"),
    DEBUG(LocationAwareLogger.DEBUG_INT, "DEBUG"),
    TRACE(LocationAwareLogger.TRACE_INT, "TRACE");

    private EnumDelegate delegate;
    Level(int i, String s) {
        this(i, s, s);
    }

    Level(int i, String s, String displayText) {
        this.delegate = new EnumDelegate(i, s, displayText);
    }

    public int toInt() {
        return this.getCode();
    }

    /**
     * Returns the string representation of this Level.
     */
    @Override
    public String toString() {
        return this.getName();
    }


    @Override
    public int getCode() {
        return this.delegate.getCode();
    }

    @Override
    public String getName() {
        return this.delegate.getName();
    }

    @Override
    public String getDisplayText() {
        return this.delegate.getDisplayText();
    }
}

