package com.jn.langx.util.concurrent.forkjoin;

import com.jn.langx.util.logging.Loggers;
import org.slf4j.Logger;
import sun.misc.Unsafe;

import java.lang.reflect.Field;

class Unsafes {
    private static final Logger logger = Loggers.getLogger(Unsafes.class);

    static Unsafe reflectGetUnsafe() {
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            return (Unsafe) field.get(null);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }
}
