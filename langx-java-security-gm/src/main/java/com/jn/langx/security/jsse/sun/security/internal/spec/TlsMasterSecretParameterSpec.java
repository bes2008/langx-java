/*
 * Copyright (c) 2005, 2017, Oracle and/or its affiliates. All rights reserved.
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

package com.jn.langx.security.jsse.sun.security.internal.spec;

import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.SecretKey;

/**
 * Parameters for SSL/TLS master secret generation.
 * This class encapsulates the information necessary to calculate a SSL/TLS
 * master secret from the premaster secret and other parameters.
 * It is used to initialize KeyGenerators of the type "TlsMasterSecret".
 *
 * <p>Instances of this class are immutable.
 *
 * @author Andreas Sterbenz
 * Sun JDK internal use only --- WILL BE REMOVED in Dolphin (JDK 7)
 * @since 1.6
 */
public class TlsMasterSecretParameterSpec implements AlgorithmParameterSpec {

    private final SecretKey premasterSecret;
    private final int majorVersion, minorVersion;
    private final byte[] clientRandom, serverRandom;
    private final byte[] extendedMasterSecretSessionHash;

    /**
     * Constructs a new TlsMasterSecretParameterSpec.
     *
     * <p>The <code>getAlgorithm()</code> method of <code>premasterSecret</code>
     * should return <code>"TlsRsaPremasterSecret"</code> if the key exchange
     * algorithm was RSA and <code>"TlsPremasterSecret"</code> otherwise.
     *
     * @param premasterSecret the premaster secret
     * @param majorVersion    the major number of the protocol version
     * @param minorVersion    the minor number of the protocol version
     * @param clientRandom    the client's random value
     * @param serverRandom    the server's random value
     * @throws NullPointerException     if premasterSecret, clientRandom,
     *                                  or serverRandom are null
     * @throws IllegalArgumentException if minorVersion or majorVersion are
     *                                  negative or larger than 255
     */
    public TlsMasterSecretParameterSpec(SecretKey premasterSecret,
                                        int majorVersion, int minorVersion,
                                        byte[] clientRandom, byte[] serverRandom) {
        if (premasterSecret == null) {
            throw new NullPointerException("premasterSecret must not be null");
        }
        this.premasterSecret = premasterSecret;
        this.majorVersion = checkVersion(majorVersion);
        this.minorVersion = checkVersion(minorVersion);
        this.clientRandom = clientRandom.clone();
        this.serverRandom = serverRandom.clone();
        this.extendedMasterSecretSessionHash = new byte[0];
    }

    /**
     * Constructs a new TlsMasterSecretParameterSpec.
     *
     * <p>The <code>getAlgorithm()</code> method of <code>premasterSecret</code>
     * should return <code>"TlsRsaPremasterSecret"</code> if the key exchange
     * algorithm was RSA and <code>"TlsPremasterSecret"</code> otherwise.
     *
     * @param premasterSecret                 the premaster secret
     * @param majorVersion                    the major number of the protocol version
     * @param minorVersion                    the minor number of the protocol version
     * @param extendedMasterSecretSessionHash the session hash for
     *                                        Extended Master Secret
     * @throws NullPointerException     if premasterSecret is null
     * @throws IllegalArgumentException if minorVersion or majorVersion are
     *                                  negative or larger than 255
     */
    public TlsMasterSecretParameterSpec(SecretKey premasterSecret,
                                        int majorVersion, int minorVersion,
                                        byte[] extendedMasterSecretSessionHash) {
        if (premasterSecret == null) {
            throw new NullPointerException("premasterSecret must not be null");
        }
        this.premasterSecret = premasterSecret;
        this.majorVersion = checkVersion(majorVersion);
        this.minorVersion = checkVersion(minorVersion);
        this.clientRandom = new byte[0];
        this.serverRandom = new byte[0];
        this.extendedMasterSecretSessionHash =
                (extendedMasterSecretSessionHash != null ?
                        extendedMasterSecretSessionHash.clone() : new byte[0]);
    }

    static int checkVersion(int version) {
        if ((version < 0) || (version > 255)) {
            throw new IllegalArgumentException(
                    "Version must be between 0 and 255");
        }
        return version;
    }

    /**
     * Returns the premaster secret.
     *
     * @return the premaster secret.
     */
    public SecretKey getPremasterSecret() {
        return premasterSecret;
    }

    /**
     * Returns the major version number.
     *
     * @return the major version number.
     */
    public int getMajorVersion() {
        return majorVersion;
    }

    /**
     * Returns the minor version number.
     *
     * @return the minor version number.
     */
    public int getMinorVersion() {
        return minorVersion;
    }

    /**
     * Returns a copy of the client's random value.
     *
     * @return a copy of the client's random value.
     */
    public byte[] getClientRandom() {
        return clientRandom.clone();
    }

    /**
     * Returns a copy of the server's random value.
     *
     * @return a copy of the server's random value.
     */
    public byte[] getServerRandom() {
        return serverRandom.clone();
    }

    /**
     * +     * Returns a copy of the Extended Master Secret session hash.
     * +     *
     * +     * @return a copy of the Extended Master Secret session hash, or an empty
     * +     *         array if no extended master secret session hash was provided
     * +     *         at instantiation time
     * +
     */
    public byte[] getExtendedMasterSecretSessionHash() {
        return extendedMasterSecretSessionHash.clone();
    }
}
