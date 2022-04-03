package com.jn.langx.util.random;

import com.jn.langx.annotation.IntLimit;
import com.jn.langx.security.Securitys;

import java.nio.ByteBuffer;

public class PooledBytesRandom implements BytesRandom {


    // It is best to make fewer, larger requests to the crypto module to
// avoid system call overhead. So, random numbers are generated in a
// pool. The pool is a Buffer that is larger than the initial random
// request size by this multiplier. The pool is enlarged if subsequent
// requests exceed the maximum buffer size.
    public static final int POOL_SIZE_MULTIPLIER = 128;

    @IntLimit(min = 1, value = POOL_SIZE_MULTIPLIER)
    private int poolSizeMultiplier;

    private ByteBuffer pool;

    public PooledBytesRandom(){
        this(POOL_SIZE_MULTIPLIER);
    }

    public PooledBytesRandom(int poolSizeMultiplier){
        this.poolSizeMultiplier = poolSizeMultiplier;
    }

    @Override
    public void get(byte[] dest) {
        Securitys.getSecureRandom().nextBytes(dest);
    }

    @Override
    public byte[] get(Integer size) {
        return getRandomBytes(size == null ? 21 : size);
    }

    private final byte[] getRandomBytes(int bytesLength) {
        // 检查是否需要重新生成随机数
        boolean reGenRandom = false;
        if (pool == null || pool.capacity() < bytesLength) {
            pool = ByteBuffer.allocate(bytesLength * POOL_SIZE_MULTIPLIER);
            //crypto.randomFillSync(pool)
            reGenRandom = true;
        } else if (pool.position() + bytesLength > pool.capacity()) {
            reGenRandom = true;
        }
        if (reGenRandom) {
            get(pool.array());
            pool = ByteBuffer.wrap(pool.array());
        }
        byte[] ret = new byte[bytesLength];
        pool.get(ret);
        return ret;
    }
}
