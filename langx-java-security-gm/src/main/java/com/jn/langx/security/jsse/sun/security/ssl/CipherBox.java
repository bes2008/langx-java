/*
 * Copyright (c) 1996, 2013, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */


package com.jn.langx.security.jsse.sun.security.ssl;

import com.jn.langx.security.jsse.sun.misc.HexDumpEncoder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Hashtable;

import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;

import java.nio.*;

import static com.jn.langx.security.jsse.sun.security.ssl.CipherSuite.*;


/**
 * This class handles bulk data enciphering/deciphering for each SSLv3
 * message.  This provides data confidentiality.  Stream ciphers (such
 * as RC4) don't need to do padding; block ciphers (e.g. DES) need it.
 *
 * Individual instances are obtained by calling the static method
 * newCipherBox(), which should only be invoked by BulkCipher.newCipher().
 *
 * In RFC 2246, with bock ciphers in CBC mode, the Initialization
 * Vector (IV) for the first record is generated with the other keys
 * and secrets when the security parameters are set.  The IV for
 * subsequent records is the last ciphertext block from the previous
 * record.
 *
 * In RFC 4346, the implicit Initialization Vector (IV) is replaced
 * with an explicit IV to protect against CBC attacks.  RFC 4346
 * recommends two algorithms used to generated the per-record IV.
 * The implementation uses the algorithm (2)(b), as described at
 * section 6.2.3.2 of RFC 4346.
 *
 * The usage of IV in CBC block cipher can be illustrated in
 * the following diagrams.
 *
 *   (random)
 *        R         P1                    IV        C1
 *        |          |                     |         |
 *  SIV---+    |-----+    |-...            |-----    |------
 *        |    |     |    |                |    |    |     |
 *     +----+  |  +----+  |             +----+  |  +----+  |
 *     | Ek |  |  + Ek +  |             | Dk |  |  | Dk |  |
 *     +----+  |  +----+  |             +----+  |  +----+  |
 *        |    |     |    |                |    |    |     |
 *        |----|     |----|           SIV--+    |----|     |-...
 *        |          |                     |       |
 *       IV         C1                     R      P1
 *                                     (discard)
 *
 *       CBC Encryption                    CBC Decryption
 *
 * NOTE that any ciphering involved in key exchange (e.g. with RSA) is
 * handled separately.
 *
 * @author David Brownell
 * @author Andreas Sterbenz
 */
final class CipherBox {

    // A CipherBox that implements the identity operation
    final static CipherBox NULL = new CipherBox();

    /* Class and subclass dynamic debugging support */
    private static final Debug debug = Debug.getInstance("ssl");

    // the protocol version this cipher conforms to
    private final ProtocolVersion protocolVersion;

    // cipher object
    private final Cipher cipher;

    /**
     * Cipher blocksize, 0 for stream ciphers
     */
    private int blockSize;

    /**
     * Is the cipher of CBC mode?
     */
     private final boolean isCBCMode;

    /**
     * secure random
     */
    private SecureRandom random;

    /**
     * Fixed masks of various block size, as the initial decryption IVs
     * for TLS 1.1 or later.
     *
     * For performance, we do not use random IVs. As the initial decryption
     * IVs will be discarded by TLS decryption processes, so the fixed masks
     * do not hurt cryptographic strength.
     */
    private static Hashtable<Integer, IvParameterSpec> masks;

    /**
     * NULL cipherbox. Identity operation, no encryption.
     */
    private CipherBox() {
        this.protocolVersion = ProtocolVersion.DEFAULT;
        this.cipher = null;
        this.isCBCMode = false;
    }

    /**
     * Construct a new CipherBox using the cipher transformation.
     *
     * @exception NoSuchAlgorithmException if no appropriate JCE Cipher
     * implementation could be found.
     */
    private CipherBox(ProtocolVersion protocolVersion, BulkCipher bulkCipher,
            SecretKey key, IvParameterSpec iv, SecureRandom random,
            boolean encrypt) throws NoSuchAlgorithmException {
        try {
            this.protocolVersion = protocolVersion;
            this.cipher = JsseJce.getCipher(bulkCipher.transformation);
            int mode = encrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE;
            this.isCBCMode = bulkCipher.isCBCMode;

            if (random == null) {
                random = JsseJce.getSecureRandom();
            }
            this.random = random;

            /*
             * RFC 4346 recommends two algorithms used to generated the
             * per-record IV. The implementation uses the algorithm (2)(b),
             * as described at section 6.2.3.2 of RFC 4346.
             *
             * As we don't care about the initial IV value for TLS 1.1 or
             * later, so if the "iv" parameter is null, we use the default
             * value generated by Cipher.init() for encryption, and a fixed
             * mask for decryption.
             */
            if (iv == null && bulkCipher.ivSize != 0 &&
                    mode == Cipher.DECRYPT_MODE &&
                    protocolVersion.v >= ProtocolVersion.TLS11.v) {
                iv = getFixedMask(bulkCipher.ivSize);
            }

            cipher.init(mode, key, iv, random);

            // Do not call getBlockSize until after init()
            // otherwise we would disrupt JCE delayed provider selection
            blockSize = cipher.getBlockSize();
            // some providers implement getBlockSize() incorrectly
            if (blockSize == 1) {
                blockSize = 0;
            }
        } catch (NoSuchAlgorithmException e) {
            throw e;
        } catch (Exception e) {
            throw new NoSuchAlgorithmException
                    ("Could not create cipher " + bulkCipher, e);
        } catch (ExceptionInInitializerError e) {
            throw new NoSuchAlgorithmException
                    ("Could not create cipher " + bulkCipher, e);
        }
    }

    /*
     * Factory method to obtain a new CipherBox object.
     */
    static CipherBox newCipherBox(ProtocolVersion version, BulkCipher cipher,
            SecretKey key, IvParameterSpec iv, SecureRandom random,
            boolean encrypt) throws NoSuchAlgorithmException {
        if (cipher.allowed == false) {
            throw new NoSuchAlgorithmException("Unsupported cipher " + cipher);
        }

        if (cipher == B_NULL) {
            return NULL;
        } else {
            return new CipherBox(version, cipher, key, iv, random, encrypt);
        }
    }

    /*
     * Get a fixed mask, as the initial decryption IVs for TLS 1.1 or later.
     */
    private static IvParameterSpec getFixedMask(int ivSize) {
        if (masks == null) {
            masks = new Hashtable<Integer, IvParameterSpec>(5);
        }

        IvParameterSpec iv = masks.get(ivSize);
        if (iv == null) {
            iv = new IvParameterSpec(new byte[ivSize]);
            masks.put(ivSize, iv);
        }

        return iv;
    }

    /*
     * Encrypts a block of data, returning the size of the
     * resulting block.
     */
    int encrypt(byte[] buf, int offset, int len) {
        if (cipher == null) {
            return len;
        }

        try {
            if (blockSize != 0) {
                // TLSv1.1 needs a IV block
                if (protocolVersion.v >= ProtocolVersion.TLS11.v) {
                    // generate a random number
                    byte[] prefix = new byte[blockSize];
                    random.nextBytes(prefix);

                    // move forward the plaintext
                    System.arraycopy(buf, offset,
                                     buf, offset + prefix.length, len);

                    // prefix the plaintext
                    System.arraycopy(prefix, 0,
                                     buf, offset, prefix.length);

                    len += prefix.length;
                }

                len = addPadding(buf, offset, len, blockSize);
            }
            if (debug != null && Debug.isOn("plaintext")) {
                try {
                    HexDumpEncoder hd = new HexDumpEncoder();

                    System.out.println(
                        "Padded plaintext before ENCRYPTION:  len = "
                        + len);
                    hd.encodeBuffer(
                        new ByteArrayInputStream(buf, offset, len),
                        System.out);
                } catch (IOException e) { }
            }
            int newLen = cipher.update(buf, offset, len, buf, offset);
            if (newLen != len) {
                // catch BouncyCastle buffering error
                throw new RuntimeException("Cipher buffering error " +
                    "in JCE provider " + cipher.getProvider().getName());
            }
            return newLen;
        } catch (ShortBufferException e) {
            throw new ArrayIndexOutOfBoundsException(e.toString());
        }
    }

    /*
     * Encrypts a ByteBuffer block of data, returning the size of the
     * resulting block.
     *
     * The byte buffers position and limit initially define the amount
     * to encrypt.  On return, the position and limit are
     * set to last position padded/encrypted.  The limit may have changed
     * because of the added padding bytes.
     */
    int encrypt(ByteBuffer bb) {

        int len = bb.remaining();

        if (cipher == null) {
            bb.position(bb.limit());
            return len;
        }

        try {
            int pos = bb.position();

            if (blockSize != 0) {
                // TLSv1.1 needs a IV block
                if (protocolVersion.v >= ProtocolVersion.TLS11.v) {
                    // generate a random number
                    byte[] prefix = new byte[blockSize];
                    random.nextBytes(prefix);

                    // move forward the plaintext
                    byte[] buf = null;
                    int limit = bb.limit();
                    if (bb.hasArray()) {
                        int arrayOffset = bb.arrayOffset();
                        buf = bb.array();
                        System.arraycopy(buf, arrayOffset + pos,
                            buf, arrayOffset + pos + prefix.length,
                            limit - pos);
                        bb.limit(limit + prefix.length);
                    } else {
                        buf = new byte[limit - pos];
                        bb.get(buf, 0, limit - pos);
                        bb.position(pos + prefix.length);
                        bb.limit(limit + prefix.length);
                        bb.put(buf);
                    }
                    bb.position(pos);

                    // prefix the plaintext
                    bb.put(prefix);
                    bb.position(pos);
                }

                // addPadding adjusts pos/limit
                len = addPadding(bb, blockSize);
                bb.position(pos);
            }
            if (debug != null && Debug.isOn("plaintext")) {
                try {
                    HexDumpEncoder hd = new HexDumpEncoder();

                    System.out.println(
                        "Padded plaintext before ENCRYPTION:  len = "
                        + len);
                    hd.encodeBuffer(bb, System.out);

                } catch (IOException e) { }
                /*
                 * reset back to beginning
                 */
                bb.position(pos);
            }

            /*
             * Encrypt "in-place".  This does not add its own padding.
             */
            ByteBuffer dup = bb.duplicate();
            int newLen = cipher.update(dup, bb);

            if (bb.position() != dup.position()) {
                throw new RuntimeException("bytebuffer padding error");
            }

            if (newLen != len) {
                // catch BouncyCastle buffering error
                throw new RuntimeException("Cipher buffering error " +
                    "in JCE provider " + cipher.getProvider().getName());
            }
            return newLen;
        } catch (ShortBufferException e) {
            RuntimeException exc = new RuntimeException(e.toString());
            exc.initCause(e);
            throw exc;
        }
    }


    /*
     * Decrypts a block of data, returning the size of the
     * resulting block if padding was required.
     *
     * For SSLv3 and TLSv1.0, with block ciphers in CBC mode the
     * Initialization Vector (IV) for the first record is generated by
     * the handshake protocol, the IV for subsequent records is the
     * last ciphertext block from the previous record.
     *
     * From TLSv1.1, the implicit IV is replaced with an explicit IV to
     * protect against CBC attacks.
     *
     * Differentiating between bad_record_mac and decryption_failed alerts
     * may permit certain attacks against CBC mode. It is preferable to
     * uniformly use the bad_record_mac alert to hide the specific type of
     * the error.
     */
    int decrypt(byte[] buf, int offset, int len,
            int tagLen) throws BadPaddingException {
        if (cipher == null) {
            return len;
        }

        try {
            int newLen = cipher.update(buf, offset, len, buf, offset);
            if (newLen != len) {
                // catch BouncyCastle buffering error
                throw new RuntimeException("Cipher buffering error " +
                    "in JCE provider " + cipher.getProvider().getName());
            }
            if (debug != null && Debug.isOn("plaintext")) {
                try {
                    HexDumpEncoder hd = new HexDumpEncoder();

                    System.out.println(
                        "Padded plaintext after DECRYPTION:  len = "
                        + newLen);
                    hd.encodeBuffer(
                        new ByteArrayInputStream(buf, offset, newLen),
                        System.out);
                } catch (IOException e) { }
            }
            if (blockSize != 0) {
                newLen = removePadding(
                    buf, offset, newLen, tagLen, blockSize, protocolVersion); 

                if (protocolVersion.v >= ProtocolVersion.TLS11.v) {
                    if (newLen < blockSize) {
                        throw new BadPaddingException("invalid explicit IV");
                    }

                    // discards the first cipher block, the IV component.
                    System.arraycopy(buf, offset + blockSize,
                                     buf, offset, newLen - blockSize);

                    newLen -= blockSize;
                }
            }
            return newLen;
        } catch (ShortBufferException e) {
            throw new ArrayIndexOutOfBoundsException(e.toString());
        }
    }


    /*
     * Decrypts a block of data, returning the size of the
     * resulting block if padding was required.  position and limit
     * point to the end of the decrypted/depadded data.  The initial
     * limit and new limit may be different, given we may
     * have stripped off some padding bytes.
     *
     *  @see decrypt(byte[], int, int)
     */
    int decrypt(ByteBuffer bb, int tagLen) throws BadPaddingException {

        int len = bb.remaining();

        if (cipher == null) {
            bb.position(bb.limit());
            return len;
        }

        try {
            /*
             * Decrypt "in-place".
             */
            int pos = bb.position();
            ByteBuffer dup = bb.duplicate();
            int newLen = cipher.update(dup, bb);
            if (newLen != len) {
                // catch BouncyCastle buffering error
                throw new RuntimeException("Cipher buffering error " +
                    "in JCE provider " + cipher.getProvider().getName());
            }

            if (debug != null && Debug.isOn("plaintext")) {
                try {
                    HexDumpEncoder hd = new HexDumpEncoder();

                    System.out.println(
                        "Padded plaintext after DECRYPTION:  len = "
                        + newLen);

                    hd.encodeBuffer(
                        (ByteBuffer)bb.duplicate().position(pos), System.out);
                } catch (IOException e) { }
            }

            /*
             * Remove the block padding.
             */
            if (blockSize != 0) {
                bb.position(pos);
                newLen = removePadding(
                    bb, tagLen, blockSize, protocolVersion);

                if (protocolVersion.v >= ProtocolVersion.TLS11.v) {
                    if (newLen < blockSize) {
                        throw new BadPaddingException("invalid explicit IV");
                    }

                    // discards the first cipher block, the IV component.
                    byte[] buf = null;
                    int limit = bb.limit();
                    if (bb.hasArray()) {
                        int arrayOffset = bb.arrayOffset();
                        buf = bb.array();
                        System.arraycopy(buf, arrayOffset + pos + blockSize,
                            buf, arrayOffset + pos, limit - pos - blockSize);
                        bb.limit(limit - blockSize);
                    } else {
                        buf = new byte[limit - pos - blockSize];
                        bb.position(pos + blockSize);
                        bb.get(buf);
                        bb.position(pos);
                        bb.put(buf);
                        bb.limit(limit - blockSize);
                    }

                    // reset the position to the end of the decrypted data
                    limit = bb.limit();
                    bb.position(limit);
                }
            }
            return newLen;
        } catch (ShortBufferException e) {
            RuntimeException exc = new RuntimeException(e.toString());
            exc.initCause(e);
            throw exc;
        }
    }

    private static int addPadding(byte[] buf, int offset, int len,
            int blockSize) {
        int     newlen = len + 1;
        byte    pad;
        int     i;

        if ((newlen % blockSize) != 0) {
            newlen += blockSize - 1;
            newlen -= newlen % blockSize;
        }
        pad = (byte) (newlen - len);

        if (buf.length < (newlen + offset)) {
            throw new IllegalArgumentException("no space to pad buffer");
        }

        /*
         * TLS version of the padding works for both SSLv3 and TLSv1
         */
        for (i = 0, offset += len; i < pad; i++) {
            buf [offset++] = (byte) (pad - 1);
        }
        return newlen;
    }

    /*
     * Apply the padding to the buffer.
     *
     * Limit is advanced to the new buffer length.
     * Position is equal to limit.
     */
    private static int addPadding(ByteBuffer bb, int blockSize) {

        int     len = bb.remaining();
        int     offset = bb.position();

        int     newlen = len + 1;
        byte    pad;
        int     i;

        if ((newlen % blockSize) != 0) {
            newlen += blockSize - 1;
            newlen -= newlen % blockSize;
        }
        pad = (byte) (newlen - len);

        /*
         * Update the limit to what will be padded.
         */
        bb.limit(newlen + offset);

        /*
         * TLS version of the padding works for both SSLv3 and TLSv1
         */
        for (i = 0, offset += len; i < pad; i++) {
            bb.put(offset++, (byte) (pad - 1));
        }

        bb.position(offset);
        bb.limit(offset);

        return newlen;
    }

    /*
     * A constant-time check of the padding.
     *
     * NOTE that we are checking both the padding and the padLen bytes here.
     *
     * The caller MUST ensure that the len parameter is a positive number.
     */
    private static int[] checkPadding(
            byte[] buf, int offset, int len, byte pad) {

        if (len <= 0) {
            throw new RuntimeException("padding len must be positive");
        }

        // An array of hits is used to prevent Hotspot optimization for
        // the purpose of a constant-time check
        int[] results = {0, 0};    // {missed #, matched #}
        for (int i = 0; i <= 256;) {
            for (int j = 0; j < len && i <= 256; j++, i++) {     // j <= i
                if (buf[offset + j] != pad) {
                    results[0]++;       // mismatched padding data
                } else {
                    results[1]++;       // matched padding data
                }
            }
        }

        return results;
    }

    /*
     * A constant-time check of the padding.
     *
     * NOTE that we are checking both the padding and the padLen bytes here.
     *
     * The caller MUST ensure that the bb parameter has remaining.
     */
    private static int[] checkPadding(ByteBuffer bb, byte pad) {

        if (!bb.hasRemaining()) {
            throw new RuntimeException("hasRemaining() must be positive");
        }

        // An array of hits is used to prevent Hotspot optimization for
        // the purpose of a constant-time check.
        int[] results = {0, 0};    // {missed #, matched #}
        bb.mark();
        for (int i = 0; i <= 256; bb.reset()) {
            for (; bb.hasRemaining() && i <= 256; i++) {
                if (bb.get() != pad) {
                    results[0]++;       // mismatched padding data
                } else {
                    results[1]++;       // matched padding data
                }
            }
        }

        return results;
    }

    /*
     * Typical TLS padding format for a 64 bit block cipher is as follows:
     *   xx xx xx xx xx xx xx 00
     *   xx xx xx xx xx xx 01 01
     *   ...
     *   xx 06 06 06 06 06 06 06
     *   07 07 07 07 07 07 07 07
     * TLS also allows any amount of padding from 1 and 256 bytes as long
     * as it makes the data a multiple of the block size
     */
    private static int removePadding(byte[] buf, int offset, int len,
            int tagLen, int blockSize,
            ProtocolVersion protocolVersion) throws BadPaddingException {

        // last byte is length byte (i.e. actual padding length - 1)
        int padOffset = offset + len - 1;
        int padLen = buf[padOffset] & 0xFF;

        int newLen = len - (padLen + 1);
        if ((newLen - tagLen) < 0) {
            // If the buffer is not long enough to contain the padding plus
            // a MAC tag, do a dummy constant-time padding check.
            //
            // Note that it is a dummy check, so we won't care about what is
            // the actual padding data.
            checkPadding(buf, offset, len, (byte)(padLen & 0xFF));

            throw new BadPaddingException("Invalid Padding length: " + padLen);
        }

        // The padding data should be filled with the padding length value.
        int[] results = checkPadding(buf, offset + newLen,
                        padLen + 1, (byte)(padLen & 0xFF));
        if (protocolVersion.v >= ProtocolVersion.TLS10.v) {
            if (results[0] != 0) {          // padding data has invalid bytes
                throw new BadPaddingException("Invalid TLS padding data");
            }
        } else { // SSLv3
            // SSLv3 requires 0 <= length byte < block size
            // some implementations do 1 <= length byte <= block size,
            // so accept that as well
            // v3 does not require any particular value for the other bytes
            if (padLen > blockSize) {
                throw new BadPaddingException("Invalid SSLv3 padding");
            }
        }
        return newLen;
    }

    /*
     * Position/limit is equal the removed padding.
     */
    private static int removePadding(ByteBuffer bb,
            int tagLen, int blockSize,
            ProtocolVersion protocolVersion) throws BadPaddingException {

        int len = bb.remaining();
        int offset = bb.position();

        // last byte is length byte (i.e. actual padding length - 1)
        int padOffset = offset + len - 1;
        int padLen = bb.get(padOffset) & 0xFF;

        int newLen = len - (padLen + 1);
        if ((newLen - tagLen) < 0) {
            // If the buffer is not long enough to contain the padding plus
            // a MAC tag, do a dummy constant-time padding check.
            //
            // Note that it is a dummy check, so we won't care about what is
            // the actual padding data.
            checkPadding(bb.duplicate(), (byte)(padLen & 0xFF));

            throw new BadPaddingException("Invalid Padding length: " + padLen);
        }

        // The padding data should be filled with the padding length value.
        int[] results = checkPadding(
                (ByteBuffer)bb.duplicate().position(offset + newLen),
                (byte)(padLen & 0xFF));
        if (protocolVersion.v >= ProtocolVersion.TLS10.v) {
            if (results[0] != 0) {          // padding data has invalid bytes
                throw new BadPaddingException("Invalid TLS padding data");
            }
        } else { // SSLv3
            // SSLv3 requires 0 <= length byte < block size
            // some implementations do 1 <= length byte <= block size,
            // so accept that as well
            // v3 does not require any particular value for the other bytes
           if (padLen > blockSize) {
                throw new BadPaddingException("Invalid SSLv3 padding");
            }
        }

        /*
         * Reset buffer limit to remove padding.
         */
        bb.position(offset + newLen);
        bb.limit(offset + newLen);

        return newLen;
    }

    /*
     * Does the cipher use CBC mode?
     *
     * @return true if the cipher use CBC mode, false otherwise.
     */
    boolean isCBCMode() {
        return isCBCMode;
    }

    /**
     * Is the cipher null?
     *
     * @return true if the cipher is null, false otherwise.
     */
    boolean isNullCipher() {
        return cipher == null;
    }

    /**
     * Sanity check the length of a fragment before decryption.
     *
     * In CBC mode, check that the fragment length is one or multiple times
     * of the block size of the cipher suite, and is at least one (one is the
     * smallest size of padding in CBC mode) bigger than the tag size of the
     * MAC algorithm.
     *
     * In non-CBC mode, check that the fragment length is not less than the
     * tag size of the MAC algorithm.
     *
     * @return true if the length of a fragment matches above requirements
     */
    boolean sanityCheck(int tagLen, int fragmentLen) {
        if (!isCBCMode) {
            return fragmentLen >= tagLen;
        }

        if ((fragmentLen % blockSize) == 0) {
            int minimal = tagLen + 1;
            minimal = (minimal >= blockSize) ? minimal : blockSize;
            return (fragmentLen >= minimal);
        }

        return false;
    }
}
