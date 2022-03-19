package com.jn.langx.util.hash;

import com.jn.langx.util.Strings;
import com.jn.langx.util.reflect.Reflects;

public abstract class AbstractHasher implements Hasher{

    protected long seed;

    /**
     *
     * @param seed 也是初始值
     */
    public void setSeed(long seed) {
        this.seed = seed;
    }

    /*********************************************************************************
     * 一次性计算
     *********************************************************************************/

    /**
     * 计算hash
     * @param bytes input bytes
     * @return hash值
     */
    public long hash(byte[] bytes) {
        return hash( bytes, bytes.length, 0);
    }

    public long hash(byte[] bytes, long seed) {
        return hash(bytes, bytes.length, seed);
    }

    /**
     * 一次性计算 hash
     * <p>
     * Calculate a hash using bytes from 0 to <code>length</code>, and
     * the provided seed value
     *
     * @param bytes  input bytes
     * @param length length of the valid bytes to consider
     * @param seed   seed value
     * @return hash value
     */
    public abstract long hash(byte[] bytes, int length, long seed);


    /********************************************************************************
     *  其他方法
     ********************************************************************************/

    /**
     *
     * @return the hasher name
     */
    @Override
    public String getName() {
        String name = Reflects.getSimpleClassName(this);
        if (name.endsWith("Hasher")) {
            name = name.substring(0, name.length() - "Hasher".length());
        }
        name = Strings.lowerCase(name);
        return name;
    }

    @Override
    public final Hasher get(Long seed) {
        if (seed == null) {
            seed = 0L;
        }
        AbstractHasher hasher = createInstance();
        hasher.setSeed(seed);
        return hasher;
    }

    protected AbstractHasher createInstance(){
        throw new UnsupportedOperationException();
    }
}
