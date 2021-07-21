package com.jn.langx.security.gm.crypto.skf;


import java.security.PrivateKey;
import java.security.cert.X509Certificate;

public interface ICryptoProvider
{
    X509Certificate getCert(final int p0) throws Exception;

    PrivateKey getPrivateKey(final int p0) throws Exception;

    byte[] doSign(final byte[] p0, final int p1, final int p2) throws Exception;
}
