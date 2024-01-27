package com.jn.langx.util.id;

import com.jn.langx.IdGenerator;
import com.jn.langx.util.random.ThreadLocalRandom;

import java.util.Random;
import java.util.UUID;

public class ConcurrentUuidGenerator implements IdGenerator<Object> {
    public static final ConcurrentUuidGenerator INSTANCE= new ConcurrentUuidGenerator();
    @Override
    public String get(Object object) {
        Random rnd = ThreadLocalRandom.current();
        long mostSig = rnd.nextLong();
        long leastSig = rnd.nextLong();
        mostSig &= -61441L;
        mostSig |= 16384L;
        leastSig &= 4611686018427387903L;
        leastSig |= Long.MIN_VALUE;
        return new UUID(mostSig, leastSig).toString();
    }

    public String get() {
        return get(null);
    }

}
