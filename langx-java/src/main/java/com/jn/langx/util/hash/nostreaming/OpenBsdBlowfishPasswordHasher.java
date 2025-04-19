package com.jn.langx.util.hash.nostreaming;

import com.jn.langx.security.crypto.pbe.pswdenc.bcrypt.BCrypt;
import com.jn.langx.util.Numbers;
import com.jn.langx.util.hash.AbstractHasher;
import com.jn.langx.util.io.Charsets;

/**
 * @since 4.5.3
 */
public class OpenBsdBlowfishPasswordHasher extends AbstractNonStreamingHasher {
    private String salt;

    @Override
    public void setSeed(long seed) {
        if (seed <= 0) {
            seed = BCrypt.GENSALT_DEFAULT_LOG2_ROUNDS;
        }
        if (seed != this.seed) {
            super.setSeed(seed);
            this.salt = seed > Integer.MAX_VALUE ? BCrypt.gensalt() : BCrypt.gensalt(Numbers.toInt(seed));
        }
    }

    @Override
    protected long doFinal(byte[] bytes, int off, int len) {
        String text = new String(bytes, off, len, Charsets.UTF_8);
        String hashed = BCrypt.hashpw(text, this.salt);
        byte[] bs = hashed.getBytes(Charsets.UTF_8);
        return toLong(bs);
    }

    @Override
    protected AbstractHasher createInstance(Object initParams) {
        return new OpenBsdBlowfishPasswordHasher();
    }
}
