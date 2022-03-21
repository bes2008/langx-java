package com.jn.langx.util.hash.streaming.crc;

import com.jn.langx.Named;
import com.jn.langx.util.Objs;

class CrcAlgoMetadata implements Named {
    /**
     *This is a name given to the algorithm. A string value.
     */
    private String name;

    /**
     *This is hash size.
     */
    private int hashSize;

    /**
     *This parameter is the poly. This is a binary value that
     *should be specified as a hexadecimal number.The top bit of the
     *poly should be omitted.For example, if the poly is 10110, you
     *should specify 06. An important aspect of this parameter is that it
     *represents the unreflected poly; the bottom bit of this parameter
     *is always the LSB of the divisor during the division regardless of
     *whether the algorithm being modelled is reflected.
     */
    private long poly;

    /**
     *This parameter specifies the initial value of the register
     *when the algorithm starts.This is the value that is to be assigned
     *to the register in the direct table algorithm. In the table
     *algorithm, we may think of the register always commencing with the
     *value zero, and this value being XORed into the register after the
     *N'th bit iteration. This parameter should be specified as a
     *hexadecimal number.
     */
    private long init;

    /**
     *This is a boolean parameter. If it is FALSE, input bytes are
     *processed with bit 7 being treated as the most significant bit
     *(MSB) and bit 0 being treated as the least significant bit.If this
     *parameter is FALSE, each byte is reflected before being processed.
     */
    private boolean refIn;

    /**
     *This is a boolean parameter. If it is set to FALSE, the
     *final value in the register is fed into the XOROUT stage directly,
     *otherwise, if this parameter is TRUE, the final register value is
     *reflected first.
     */
    private boolean refOut;

    /**
     *This is an W-bit value that should be specified as a
     *hexadecimal number.It is XORed to the final register value (after
     *the REFOUT) stage before the value is returned as the official
     *checksum.
     */
    private long xorOut;

    /**
     *This field is not strictly part of the definition, and, in
     *the event of an inconsistency between this field and the other
     *field, the other fields take precedence.This field is a check
     *value that can be used as a weak validator of implementations of
     *the algorithm.The field contains the checksum obtained when the
     *ASCII string "123456789" is fed through the specified algorithm
     *(i.e. 313233... (hexadecimal)).
     */
    private long check;

    /**
     * Create new CRC algorithm
     * @param name CRC algorithm name
     * @param hashSize hash size
     * @param poly poly number
     * @param init initial value of the CRC algorithm
     * @param refIn input bytes are processed
     * @param refOut final register value is reflected first
     * @param xorOut value is XORed to the final register value
     * @param check CRC value for new byte[]{'1','2','3','4','5','6','7','8','9'}
     */
    public CrcAlgoMetadata(String name, int hashSize, long poly, long init, boolean refIn, boolean refOut, long xorOut, long check) {
        this.name = name;
        this.hashSize = hashSize;
        this.poly = poly;
        this.init = init;
        this.refIn = refIn;
        this.refOut = refOut;
        this.xorOut = xorOut;
        this.check = check;
    }

    /**
     * This is a name given to the algorithm. A string value.
     * @return CRC algorithm name
     */
    public String getName() {
        return name;
    }

    /**
     * This is hash size.
     * @return hash size
     */
    public int getHashSize() {
        return hashSize;
    }

    /**
     * This parameter is the poly. This is a binary value that
     * should be specified as a hexadecimal number.The top bit of the
     * poly should be omitted.For example, if the poly is 10110, you
     * should specify 06. An important aspect of this parameter is that it
     * represents the unreflected poly; the bottom bit of this parameter
     * is always the LSB of the divisor during the division regardless of
     * whether the algorithm being modelled is reflected.
     * @return poly number
     */
    public long getPoly() {
        return poly;
    }

    /**
     *
     * This parameter specifies the initial value of the register
     * when the algorithm starts.This is the value that is to be assigned
     * to the register in the direct table algorithm. In the table
     * algorithm, we may think of the register always commencing with the
     * value zero, and this value being XORed into the register after the
     * N'th bit iteration. This parameter should be specified as a
     * hexadecimal number.
     * @return initial value of the CRC algorithm
     */
    public long getInit() {
        return init;
    }

    /**
     * This is a boolean parameter. If it is TRUE, input bytes are
     * processed with bit 7 being treated as the most significant bit
     * (MSB) and bit 0 being treated as the least significant bit.If this
     * parameter is FALSE, each byte is reflected before being processed.
     * @return input bytes are processed
     */
    public boolean isRefIn() {
        return refIn;
    }

    /**
     * This is a boolean parameter. If it is set to FALSE, the
     * final value in the register is fed into the XOROUT stage directly,
     * otherwise, if this parameter is TRUE, the final register value is
     * reflected first.
     * @return final register value is reflected first
     */
    public boolean isRefOut() {
        return refOut;
    }

    /**
     * This is an W-bit value that should be specified as a
     * hexadecimal number.It is XORed to the final register value (after
     * the REFOUT) stage before the value is returned as the official
     * checksum.
     * @return value is XORed to the final register value
     */
    public long getXorOut() {
        return xorOut;
    }

    /**
     * This field is not strictly part of the definition, and, in
     * the event of an inconsistency between this field and the other
     * field, the other fields take precedence.This field is a check
     * value that can be used as a weak validator of implementations of
     * the algorithm.The field contains the checksum obtained when the
     * ASCII string "123456789" is fed through the specified algorithm
     * (i.e. 313233... (hexadecimal)).
     * @return CRC value for new byte[]{'1','2','3','4','5','6','7','8','9'}
     */
    public long getCheck() {
        return check;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CrcAlgoMetadata other = (CrcAlgoMetadata) obj;
        if (this.hashSize != other.hashSize) {
            return false;
        }
        if (this.poly != other.poly) {
            return false;
        }
        if (this.init != other.init) {
            return false;
        }
        if (this.refIn != other.refIn) {
            return false;
        }
        if (this.refOut != other.refOut) {
            return false;
        }
        if (this.xorOut != other.xorOut) {
            return false;
        }
        if (this.check != other.check) {
            return false;
        }
        if (!Objs.equals(this.name, other.name)) {
            return false;
        }
        return true;
    }

}
