package com.jn.langx.util.hash.streaming.crc16;

import com.jn.langx.util.hash.streaming.AbstractStreamingHasher;

/**
 * @since 4.4.1
 */
public class AbstractCrc16Hasher extends AbstractStreamingHasher {
    private static final long serialVersionUID = 1L;

    /**
     * CRC16 Checksum 结果值
     */
    protected int wCRCin;

    public AbstractCrc16Hasher() {
        super();
    }

    @Override
    public long getHash() {
        return wCRCin;
    }


    @Override
    public void reset() {
        wCRCin = 0x0000;
    }
}
