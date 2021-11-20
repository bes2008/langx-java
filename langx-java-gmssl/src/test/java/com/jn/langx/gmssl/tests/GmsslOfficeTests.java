package com.jn.langx.gmssl.tests;

import com.jn.langx.codec.base64.Base64;
import org.gmssl.GmSSL;

public class GmsslOfficeTests {

    public static void main(String[] args) {
        int i;
        final GmSSL gmssl = new GmSSL();

        /* GmSSL versions */
        String[] versions = gmssl.getVersions();
        for (i = 0; i < versions.length; i++) {
            System.out.println(versions[i]);
        }

        /* Supported algorithms */
        System.out.print("Ciphers: ");
        String[] ciphers = gmssl.getCiphers();
        for (i = 0; i < ciphers.length - 1; i++) {
            System.out.print(ciphers[i] + ", ");
        }
        System.out.println(ciphers[i]);

        System.out.print("Digests: ");
        String[] digests = gmssl.getDigests();
        for (i = 0; i < digests.length - 1; i++) {
            System.out.print(digests[i] + ", ");
        }
        System.out.println(digests[i]);

        System.out.print("MACs: ");
        String[] macs = gmssl.getMacs();
        for (i = 0; i < macs.length - 1; i++) {
            System.out.print(macs[i] + ", ");
        }
        System.out.println(macs[i]);

        System.out.print("SignAlgorithms: ");
        String[] signAlgors = gmssl.getSignAlgorithms();
        for (i = 0; i < signAlgors.length - 1; i++) {
            System.out.print(signAlgors[i] + ", ");
        }
        System.out.println(signAlgors[i]);

        System.out.print("PublicKeyEncryptions: ");
        String[] encAlgors = gmssl.getPublicKeyEncryptions();
        for (i = 0; i < encAlgors.length - 1; i++) {
            System.out.print(encAlgors[i] + ", ");
        }
        System.out.println(encAlgors[i]);

        System.out.print("DeriveKeyAlgorithms: ");
        String[] kdfs = gmssl.getDeriveKeyAlgorithms();
        for (i = 0; i < kdfs.length - 1; i++) {
            System.out.print(kdfs[i] + ", ");
        }
        System.out.println(kdfs[i]);

        /* Crypto operations */
        System.out.print("Random(20) = ");
        byte[] data = gmssl.generateRandom(20);
        for (i = 0; i < data.length; i++) {
            System.out.printf("%02X", data[i]);
        }
        System.out.println("");

        System.out.printf("SMS4 IV length = %d bytes, key length = %d bytes, block size = %d bytes\n",
                gmssl.getCipherIVLength("SMS4"),
                gmssl.getCipherKeyLength("SMS4"),
                gmssl.getCipherBlockSize("SMS4"));

        //byte[] key = {1,2,3,4,5,6,7,8,1,2,3,4,5,6,7,8};
        byte[] key = {1,2,3,4,5,6,7,1,1,2,3,4,5,6,7,1};
        byte[] iv = {1,2,3,4,5,6,7,8,1,2,3,4,5,6,7,8};
        byte[] ciphertext = gmssl.symmetricEncrypt("SM4-CBC", "01234567".getBytes(), key, iv);

        System.out.print("Ciphertext base64: "+ Base64.encodeBase64String(ciphertext));
        System.out.println("");
        byte[] plaintext = gmssl.symmetricDecrypt("SM4-CBC", ciphertext, key, iv);
        System.out.print("Plaintext: "+new String(plaintext));

        System.out.println("");

        byte[] dgst = gmssl.digest("SM3", "abc".getBytes());
        System.out.print("SM3(\"abc\") = ");
        for (i = 0; i < dgst.length; i++) {
            System.out.printf("%02X", dgst[i]);
        }
        System.out.println("");

        byte[] macTag = gmssl.mac("HMAC-SM3", "abc".getBytes(), "password".getBytes());
        System.out.print("HMAC-SM3(\"abc\") = ");
        for (i = 0; i < macTag.length; i++) {
            System.out.printf("%02X", macTag[i]);
        }
        System.out.println("");

        byte[] sm2PrivateKey = new byte[] {
                (byte)0x30,(byte)0x77,(byte)0x02,(byte)0x01,(byte)0x01,(byte)0x04,(byte)0x20,(byte)0x28,
                (byte)0x7d,(byte)0x3f,(byte)0xb9,(byte)0xf4,(byte)0xbb,(byte)0xc8,(byte)0xbd,(byte)0xe1,
                (byte)0x54,(byte)0x75,(byte)0x87,(byte)0x9f,(byte)0x08,(byte)0x61,(byte)0x20,(byte)0xe3,
                (byte)0x65,(byte)0xf8,(byte)0xb2,(byte)0xca,(byte)0x14,(byte)0x26,(byte)0x81,(byte)0xf6,
                (byte)0x1e,(byte)0xd8,(byte)0x7f,(byte)0xc0,(byte)0x66,(byte)0x20,(byte)0x29,(byte)0xa0,
                (byte)0x0a,(byte)0x06,(byte)0x08,(byte)0x2a,(byte)0x81,(byte)0x1c,(byte)0xcf,(byte)0x55,
                (byte)0x01,(byte)0x82,(byte)0x2d,(byte)0xa1,(byte)0x44,(byte)0x03,(byte)0x42,(byte)0x00,
                (byte)0x04,(byte)0xb1,(byte)0x1e,(byte)0x4c,(byte)0x8c,(byte)0xa9,(byte)0x02,(byte)0xf2,
                (byte)0x8d,(byte)0x8b,(byte)0x98,(byte)0xd2,(byte)0xd0,(byte)0xc4,(byte)0xf1,(byte)0x60,
                (byte)0x91,(byte)0xfb,(byte)0x61,(byte)0x62,(byte)0x00,(byte)0xcf,(byte)0x93,(byte)0x4e,
                (byte)0x3f,(byte)0xca,(byte)0xfd,(byte)0xf7,(byte)0x9d,(byte)0x76,(byte)0xb8,(byte)0x2b,
                (byte)0xb3,(byte)0x30,(byte)0x98,(byte)0x65,(byte)0xf5,(byte)0x12,(byte)0xab,(byte)0x45,
                (byte)0x78,(byte)0x29,(byte)0x87,(byte)0xdc,(byte)0x74,(byte)0x07,(byte)0x75,(byte)0xd0,
                (byte)0x68,(byte)0xad,(byte)0x85,(byte)0x71,(byte)0x08,(byte)0xc2,(byte)0x19,(byte)0xf0,
                (byte)0xf4,(byte)0xca,(byte)0x6e,(byte)0xe1,(byte)0xea,(byte)0x86,(byte)0xe6,(byte)0x21,
                (byte)0x76};

        byte[] sm2PublicKey = new byte[] {
                (byte)0x30,(byte)0x59,(byte)0x30,(byte)0x13,(byte)0x06,(byte)0x07,(byte)0x2a,(byte)0x86,
                (byte)0x48,(byte)0xce,(byte)0x3d,(byte)0x02,(byte)0x01,(byte)0x06,(byte)0x08,(byte)0x2a,
                (byte)0x81,(byte)0x1c,(byte)0xcf,(byte)0x55,(byte)0x01,(byte)0x82,(byte)0x2d,(byte)0x03,
                (byte)0x42,(byte)0x00,(byte)0x04,(byte)0xb1,(byte)0x1e,(byte)0x4c,(byte)0x8c,(byte)0xa9,
                (byte)0x02,(byte)0xf2,(byte)0x8d,(byte)0x8b,(byte)0x98,(byte)0xd2,(byte)0xd0,(byte)0xc4,
                (byte)0xf1,(byte)0x60,(byte)0x91,(byte)0xfb,(byte)0x61,(byte)0x62,(byte)0x00,(byte)0xcf,
                (byte)0x93,(byte)0x4e,(byte)0x3f,(byte)0xca,(byte)0xfd,(byte)0xf7,(byte)0x9d,(byte)0x76,
                (byte)0xb8,(byte)0x2b,(byte)0xb3,(byte)0x30,(byte)0x98,(byte)0x65,(byte)0xf5,(byte)0x12,
                (byte)0xab,(byte)0x45,(byte)0x78,(byte)0x29,(byte)0x87,(byte)0xdc,(byte)0x74,(byte)0x07,
                (byte)0x75,(byte)0xd0,(byte)0x68,(byte)0xad,(byte)0x85,(byte)0x71,(byte)0x08,(byte)0xc2,
                (byte)0x19,(byte)0xf0,(byte)0xf4,(byte)0xca,(byte)0x6e,(byte)0xe1,(byte)0xea,(byte)0x86,
                (byte)0xe6,(byte)0x21,(byte)0x76};

        byte[] sig = gmssl.sign("sm2sign", dgst, sm2PrivateKey);
        System.out.print("SM2 Signature : ");
        for (i = 0; i < sig.length; i++) {
            System.out.printf("%02X", sig[i]);
        }
        System.out.print("\n");

        int vret = gmssl.verify("sm2sign", dgst, sig, sm2PublicKey);
        System.out.println("Verification result = " + vret);

        byte[] sm2Ciphertext = gmssl.publicKeyEncrypt("sm2encrypt-with-sm3", dgst, sm2PublicKey);
        System.out.print("SM2 Ciphertext : ");
        for (i = 0; i < sm2Ciphertext.length; i++) {
            System.out.printf("%02X", sm2Ciphertext[i]);
        }
        System.out.print("\n");

        byte[] sm2Plaintext = gmssl.publicKeyDecrypt("sm2encrypt-with-sm3", sm2Ciphertext, sm2PrivateKey);
        System.out.print("SM2 Plaintext : ");
        for (i = 0; i < sm2Plaintext.length; i++) {
            System.out.printf("%02X", sm2Plaintext[i]);
        }
        System.out.print("\n");

        /* Errors */
        System.out.println("Errors:");
        String[] errors = gmssl.getErrorStrings();
        for (i = 0; i < errors.length; i++) {
            System.out.println(errors[i]);
        }

    }

}
