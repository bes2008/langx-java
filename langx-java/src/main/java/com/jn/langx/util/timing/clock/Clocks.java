package com.jn.langx.util.timing.clock;

public class Clocks {
    private Clocks(){

    }
    private static final Clock DEFAULT = new SystemClock();

    /**
     * The default clock to use.
     *
     * @return the default {@link Clock} instance
     */
    public static Clock defaultClock() {
        return DEFAULT;
    }
}
