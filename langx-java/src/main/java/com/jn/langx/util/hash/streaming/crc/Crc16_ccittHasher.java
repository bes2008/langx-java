package com.jn.langx.util.hash.streaming.crc;

/**
 * CRC16_CCITT：多项式x16+x12+x5+1（0x1021），初始值0x0000，低位在前，高位在后，结果与0x0000异或
 * 0x8408是0x1021按位颠倒后的结果。
 *
 * @since 4.4.1
 */
public class Crc16_ccittHasher extends AbstractCrc16Hasher {
    private static final int WC_POLY = 0x8408;

    @Override
    protected void update(byte b) {
        wCRCin ^= (b & 0x00ff);
        for (int j = 0; j < 8; j++) {
            if ((wCRCin & 0x0001) != 0) {
                wCRCin >>= 1;
                wCRCin ^= WC_POLY;
            } else {
                wCRCin >>= 1;
            }
        }
    }
}
