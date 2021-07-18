/* ====================================================================
 * Copyright (c) 2015 - 2017 The GmSSL Project.  All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. All advertising materials mentioning features or use of this
 *    software must display the following acknowledgment:
 *    "This product includes software developed by the GmSSL Project.
 *    (http://gmssl.org/)"
 *
 * 4. The name "GmSSL Project" must not be used to endorse or promote
 *    products derived from this software without prior written
 *    permission. For written permission, please contact
 *    guanzhi1980@gmail.com.
 *
 * 5. Products derived from this software may not be called "GmSSL"
 *    nor may "GmSSL" appear in their names without prior written
 *    permission of the GmSSL Project.
 *
 * 6. Redistributions of any form whatsoever must retain the following
 *    acknowledgment:
 *    "This product includes software developed by the GmSSL Project
 *    (http://gmssl.org/)"
 *
 * THIS SOFTWARE IS PROVIDED BY THE GmSSL PROJECT ``AS IS'' AND ANY
 * EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE GmSSL PROJECT OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 * ====================================================================
 */
package com.jn.langx.security.gm.gmssl;

import com.jn.langx.util.ClassLoaders;
import com.jn.langx.util.jni.NativeLibraryLoader;

public class GmSSL {

    public native String[] getVersions();

    public native String[] getCiphers();

    public native String[] getDigests();

    public native String[] getMacs();

    public native String[] getSignAlgorithms();

    public native String[] getPublicKeyEncryptions();

    public native String[] getDeriveKeyAlgorithms();

    public native byte[] generateRandom(int length);

    public native int getCipherIVLength(String cipher);

    public native int getCipherKeyLength(String cipher);

    public native int getCipherBlockSize(String cipher);

    public native byte[] symmetricEncrypt(String cipher, byte[] in, byte[] key, byte[] iv);

    public native byte[] symmetricDecrypt(String cipher, byte[] in, byte[] key, byte[] iv);

    public native int getDigestLength(String digest);

    public native int getDigestBlockSize(String digest);

    public native byte[] digest(String algor, byte[] data);

    public native String[] getMacLength(String algor);

    public native byte[] mac(String algor, byte[] data, byte[] key);

    public native byte[] sign(String algor, byte[] data, byte[] privateKey);

    public native int verify(String algor, byte[] digest, byte[] signature, byte[] publicKey);

    public native byte[] publicKeyEncrypt(String algor, byte[] in, byte[] publicKey);

    public native byte[] publicKeyDecrypt(String algor, byte[] in, byte[] privateKey);

    public native byte[] deriveKey(String algor, int keyLength, byte[] peerPublicKey, byte[] privateKey);

    public native String[] getErrorStrings();

    public static void main(String[] args) {
        final GmSSL gmssl = new GmSSL();
        String[] versions = gmssl.getVersions();
        if (versions != null) {
            for (String v : versions) {
                System.out.println(v);
            }
        } else {
            System.out.println("no versions");
        }
    }

    static {
        try {
            if (NativeLoader.win()) {
                NativeLoader.addGmSSLDir("META-INF/native");
                System.loadLibrary("libcrypto-1_1-x64");
                System.loadLibrary("gmssljni");
            } else {
                NativeLibraryLoader.load("crypto", ClassLoaders.getClassLoader(GmSSL.class));
                NativeLibraryLoader.load("gmssljni", ClassLoaders.getClassLoader(GmSSL.class));
            }

        } catch (Exception e) {
            throw new RuntimeException("GmSSL static init exp", e);
        }
    }
}
