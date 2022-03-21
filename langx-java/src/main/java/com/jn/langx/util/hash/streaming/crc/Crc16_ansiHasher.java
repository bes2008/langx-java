package com.jn.langx.util.hash.streaming.crc;

public class Crc16_ansiHasher extends AbstractCrc16Hasher {
    private static final int WC_POLY = 0xa001;

    @Override
    public void reset() {
        super.reset();
        this.wCRCin = 0xffff;
    }

    @Override
    protected void update(byte b) {
        int hi = wCRCin >> 8;
        hi ^= b;
        wCRCin = hi;

        for (int i = 0; i < 8; i++) {
            int flag = wCRCin & 0x0001;
            wCRCin = wCRCin >> 1;
            if (flag == 1) {
                wCRCin ^= WC_POLY;
            }
        }
    }
}
