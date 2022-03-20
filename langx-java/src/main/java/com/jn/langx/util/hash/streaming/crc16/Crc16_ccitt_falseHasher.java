package com.jn.langx.util.hash.streaming.crc16;


/**
 * CRC16_ANSI
 *
 * @since 4.4.1
 */
public class Crc16_ccitt_falseHasher extends AbstractCrc16Hasher {
    private static final int WC_POLY = 0x1021;

    @Override
    public void reset() {
        super.reset();
        this.wCRCin = 0xffff;
    }

    @Override
    public void update(byte[] b, int off, int len) {
        super.update(b, off, len);
    }

    @Override
    public long getHash() {
        int v = (int) super.getHash();
        v ^= 0xffff;
        return v;
    }

    @Override
    protected void update(byte b) {
        for (int i = 0; i < 8; i++) {
            boolean bit = ((b >> (7 - i) & 1) == 1);
            boolean c15 = ((wCRCin >> 15 & 1) == 1);
            wCRCin <<= 1;
            if (c15 ^ bit) {
                wCRCin ^= WC_POLY;
            }
        }
    }
}
