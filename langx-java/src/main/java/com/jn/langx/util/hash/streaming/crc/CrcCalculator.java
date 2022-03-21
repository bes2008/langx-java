package com.jn.langx.util.hash.streaming.crc;

class CrcCalculator {
    private CrcAlgoMetadata metadata;

    private byte hashSize;
    private long mask = 0xFFFFFFFFFFFFFFFFL;
    private long[] table = new long[256];


    CrcCalculator(CrcAlgoMetadata parameters) {
        this.metadata = parameters;
        this.hashSize = (byte) parameters.getHashSize();
        if (this.hashSize < 64) {
            mask = (1L << this.hashSize) - 1;
        }
        createTable();
    }

    public long calc(byte[] data) {
        return calc(data, 0, data.length);
    }

    public long calc(byte[] data, int offset, int length) {
        long init = metadata.isRefOut() ? CrcCalculator.reverseBits(metadata.getInit(), hashSize) : metadata.getInit();
        long hash = computeCrc(init, data, offset, length);
        return (hash ^ metadata.getXorOut()) & mask;
    }

    private long computeCrc(long init, byte[] data, int offset, int length) {
        long crc = init;

        if (metadata.isRefOut()) {
            for (int i = offset; i < offset + length; i++) {
                crc = (table[(int) ((crc ^ data[i]) & 0xFF)] ^ (crc >>> 8));
                crc &= mask;
            }
        } else {
            int toRight = (hashSize - 8);
            toRight = Math.max(toRight, 0);
            for (int i = offset; i < offset + length; i++) {
                crc = (table[(int) (((crc >> toRight) ^ data[i]) & 0xFF)] ^ (crc << 8));
                crc &= mask;
            }
        }

        return crc;
    }

    private void createTable() {
        for (int i = 0; i < table.length; i++) {
            table[i] = createTableEntry(i);
        }
    }

    private long createTableEntry(int index) {
        long r = (long) index;

        if (metadata.isRefIn()) {
            r = CrcCalculator.reverseBits(r, hashSize);
        } else if (hashSize > 8) {
            r <<= (hashSize - 8);
        }

        long lastBit = (1L << (hashSize - 1));

        for (int i = 0; i < 8; i++) {
            if ((r & lastBit) != 0) {
                r = ((r << 1) ^ metadata.getPoly());
            } else {
                r <<= 1;
            }
        }

        if (metadata.isRefOut()) {
            r = CrcCalculator.reverseBits(r, hashSize);
        }

        return r & mask;
    }

    public CrcAlgoMetadata getMetadata() {
        return metadata;
    }

    static long reverseBits(long ul, int valueLength) {
        long newValue = 0;

        for (int i = valueLength - 1; i >= 0; i--) {
            newValue |= (ul & 1) << i;
            ul >>= 1;
        }

        return newValue;
    }
}
