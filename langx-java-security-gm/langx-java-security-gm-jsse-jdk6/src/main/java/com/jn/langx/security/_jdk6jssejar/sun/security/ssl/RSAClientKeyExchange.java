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


package com.jn.langx.security._jdk6jssejar.sun.security.ssl;

import java.io.*;
import java.security.*;

import javax.crypto.*;

import javax.net.ssl.*;

import com.jn.langx.security._jdk6jssejar.sun.security.util.KeyUtil;
import sun.security.internal.spec.TlsRsaPremasterSecretParameterSpec;

/**
 * This is the client key exchange message (CLIENT --> SERVER) used with
 * all RSA key exchanges; it holds the RSA-encrypted pre-master secret.
 * <p>
 * The message is encrypted using PKCS #1 block type 02 encryption with the
 * server's public key.  The padding and resulting message size is a function
 * of this server's public key modulus size, but the pre-master secret is
 * always exactly 48 bytes.
 */
final class RSAClientKeyExchange extends HandshakeMessage {

    /*
     * The following field values were encrypted with the server's public
     * key (or temp key from server key exchange msg) and are presented
     * here in DECRYPTED form.
     */
    private ProtocolVersion protocolVersion; // preMaster [0,1]
    SecretKey preMaster;
    private byte[] encrypted;           // same size as public modulus

    /*
     * Client randomly creates a pre-master secret and encrypts it
     * using the server's RSA public key; only the server can decrypt
     * it, using its RSA private key.  Result is the same size as the
     * server's public key, and uses PKCS #1 block format 02.
     */
    RSAClientKeyExchange(ProtocolVersion protocolVersion,
                         ProtocolVersion maxVersion,
                         SecureRandom generator, PublicKey publicKey) throws IOException {
        if (publicKey.getAlgorithm().equals("RSA") == false) {
            throw new SSLKeyException("Public key not of type RSA");
        }
        this.protocolVersion = protocolVersion;

        try {
            KeyGenerator kg = JsseJce.getKeyGenerator("SunTlsRsaPremasterSecret");
            kg.init(new TlsRsaPremasterSecretParameterSpec(
                    maxVersion.v, protocolVersion.v), generator);
            preMaster = kg.generateKey();

            Cipher cipher = JsseJce.getCipher(JsseJce.CIPHER_RSA_PKCS1);
            cipher.init(Cipher.WRAP_MODE, publicKey, generator);
            encrypted = cipher.wrap(preMaster);
        } catch (GeneralSecurityException e) {
            throw (SSLKeyException) new SSLKeyException
                    ("RSA premaster secret error").initCause(e);
        }
    }

    /*
     * Server gets the PKCS #1 (block format 02) data, decrypts
     * it with its private key.
     */
    RSAClientKeyExchange(ProtocolVersion currentVersion,
                         ProtocolVersion maxVersion,
                         SecureRandom generator, HandshakeInStream input,
                         int messageSize, PrivateKey privateKey) throws IOException {

        if (!privateKey.getAlgorithm().equals("RSA")) {
            throw new SSLKeyException("Private key not of type RSA");
        }

        if (currentVersion.v >= ProtocolVersion.TLS10.v) {
            encrypted = input.getBytes16();
        } else {
            encrypted = new byte[messageSize];
            if (input.read(encrypted) != messageSize) {
                throw new SSLProtocolException
                        ("SSL: read PreMasterSecret: short read");
            }
        }

        byte[] encoded = null;
        try {
            Cipher cipher = JsseJce.getCipher(JsseJce.CIPHER_RSA_PKCS1);
            boolean needFailover = !KeyUtil.isOracleJCEProvider(
                    cipher.getProvider().getName());
            if (needFailover) {
                cipher.init(Cipher.DECRYPT_MODE, privateKey);
                boolean failed = false;
                try {
                    encoded = cipher.doFinal(encrypted);
                } catch (BadPaddingException bpe) {
                    // Note: encoded == null
                    failed = true;
                }
                encoded = KeyUtil.checkTlsPreMasterSecretKey(
                        maxVersion.v, currentVersion.v,
                        generator, encoded, failed);
                preMaster = generatePreMasterSecret(
                        maxVersion.v, currentVersion.v,
                        encoded, generator);
            } else {
                cipher.init(Cipher.UNWRAP_MODE, privateKey,
                        new TlsRsaPremasterSecretParameterSpec(
                                maxVersion.v, currentVersion.v),
                        generator);
                preMaster = (SecretKey) cipher.unwrap(encrypted,
                        "TlsRsaPremasterSecret", Cipher.SECRET_KEY);
            }
        } catch (InvalidKeyException ibk) {
            // the message is too big to process with RSA
            throw new SSLProtocolException(
                    "Unable to process PreMasterSecret, may be too big");
        } catch (Exception e) {
            // unlikely to happen, otherwise, must be a provider exception
            if (debug != null && Debug.isOn("handshake")) {
                System.out.println("RSA premaster secret decryption error:");
                e.printStackTrace(System.out);
            }
            throw new RuntimeException("Could not generate dummy secret", e);
        }
    }

    // generate a premaster secret with the specified version number
    @SuppressWarnings("deprecation")
    private static SecretKey generatePreMasterSecret(
            int clientVersion, int serverVersion,
            byte[] encodedSecret, SecureRandom generator) {

        if (debug != null && Debug.isOn("handshake")) {
            System.out.println("Generating a premaster secret");
        }

        try {
            String s = ((clientVersion >= ProtocolVersion.TLS12.v) ?
                    "SunTls12RsaPremasterSecret" : "SunTlsRsaPremasterSecret");
            KeyGenerator kg = JsseJce.getKeyGenerator(s);
            kg.init(new TlsRsaPremasterSecretParameterSpec(
                            clientVersion, serverVersion, encodedSecret),
                    generator);
            return kg.generateKey();
        } catch (InvalidAlgorithmParameterException iae) {
            // unlikely to happen, otherwise, must be a provider exception
            if (debug != null && Debug.isOn("handshake")) {
                System.out.println("RSA premaster secret generation error:");
                iae.printStackTrace(System.out);
            }
            throw new RuntimeException("Could not generate premaster secret", iae);
        } catch (NoSuchAlgorithmException nsae) {
            // unlikely to happen, otherwise, must be a provider exception
            if (debug != null && Debug.isOn("handshake")) {
                System.out.println("RSA premaster secret generation error:");
                nsae.printStackTrace(System.out);
            }
            throw new RuntimeException("Could not generate premaster secret", nsae);
        }
    }

    @Override
    int messageType() {
        return ht_client_key_exchange;
    }

    @Override
    int messageLength() {
        if (protocolVersion.v >= ProtocolVersion.TLS10.v) {
            return encrypted.length + 2;
        } else {
            return encrypted.length;
        }
    }

    @Override
    void send(HandshakeOutStream s) throws IOException {
        if (protocolVersion.v >= ProtocolVersion.TLS10.v) {
            s.putBytes16(encrypted);
        } else {
            s.write(encrypted);
        }
    }

    @Override
    void print(PrintStream s) throws IOException {
        s.println("*** ClientKeyExchange, RSA PreMasterSecret, " +
                protocolVersion);
    }
}
