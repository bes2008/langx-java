package com.jn.langx.util.random;

import com.jn.langx.util.Preconditions;
import com.jn.langx.util.concurrent.threadlocal.GlobalThreadLocalMap;

import java.nio.ByteBuffer;

/**
 * @since 4.4.7
 */
public class PooledBytesRandom extends CommonBytesRandom {

    /**
     * It is best to make fewer, larger requests to the crypto module to
     * avoid system call overhead. So, random numbers are generated in a
     * pool. The pool is a Buffer that is larger than the initial random
     * request size by this multiplier. The pool is enlarged if subsequent
     * requests exceed the maximum buffer size.
     */
    public static final int POOL_SIZE_MULTIPLIER = 128;


    private ByteBuffer pool;

    public PooledBytesRandom() {
        this(POOL_SIZE_MULTIPLIER);
    }

    public PooledBytesRandom(int multiplier) {
        setMultiplier(multiplier);
        setDelegate(GlobalThreadLocalMap.getRandom());
    }

    @Override
    public byte[] get(Integer size) {
        Preconditions.checkNotNullArgument(size, "size");
        return getRandomBytes(size);
    }

    private byte[] getRandomBytes(int bytesLength) {
        // 检查是否需要重新生成随机数
        boolean reGenBytes = false;
        if (pool == null || pool.capacity() < bytesLength) {
            pool = ByteBuffer.allocate(bytesLength * this.getMultiplier());
            reGenBytes = true;
        } else if (pool.position() + bytesLength > pool.limit()) {
            reGenBytes = true;
        }
        if (reGenBytes) {
            get(pool.array());
            pool = ByteBuffer.wrap(pool.array());
        }
        byte[] ret = new byte[bytesLength];
        pool.get(ret);
        return ret;
    }
}
