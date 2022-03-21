package com.jn.langx.util.hash.streaming.crc;

/**
 * CRC16_USB：多项式x16+x15+x2+1（0x8005），初始值0xFFFF，低位在前，高位在后，结果与0xFFFF异或
 * 0xA001是0x8005按位颠倒后的结果
 *
 * @since 4.4.1
 */
public class Crc16_usbHasher extends AbstractCrc16Hasher {
    private static final int WC_POLY = 0xa001;

    @Override
    public void reset() {
        super.reset();
        this.wCRCin = 0xFFFF;
    }

    @Override
    public void update(byte[] b, int off, int len) {
        super.update(b, off, len);
    }

    @Override
    public long getHash() {
        int r = (int) super.getHash();
        r ^= 0xffff;
        return r;
    }

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
