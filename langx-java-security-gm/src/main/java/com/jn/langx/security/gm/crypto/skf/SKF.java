package com.jn.langx.security.gm.crypto.skf;


import java.io.ByteArrayInputStream;
import java.security.PrivateKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

public class SKF implements ICryptoProvider
{
    private static boolean logined;
    private int err;

    static {
        SKF.logined = false;
        System.loadLibrary("skf4gmssl");
    }

    private static native int login(final byte[] p0, final byte[] p1);

    private static native byte[] getCrt(final int p0);

    private static native byte[] sign(final byte[] p0, final int p1, final int p2);

    public SKF() {
        this.err = 0;
    }

    public boolean login(final String s, final String s2) {
        if (!SKF.logined) {
            synchronized (SKF.class) {
                if (!SKF.logined) {
                    this.err = login(s.getBytes(), s2.getBytes());
                    if (this.err == 0) {
                        SKF.logined = true;
                    }
                }
            }
            // monitorexit(SKF.class)
        }
        return SKF.logined;
    }

    public int getError() {
        return this.err;
    }

    @Override
    public X509Certificate getCert(final int n) throws Exception {
        if (!SKF.logined) {
            return null;
        }
        byte[] crt = null;
        try {
            synchronized (SKF.class) {
                crt = getCrt(n);
            }
            // monitorexit(SKF.class)
        }
        catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
        return (X509Certificate) CertificateFactory.getInstance("X509").generateCertificate(new ByteArrayInputStream(crt));
    }

    @Override
    public PrivateKey getPrivateKey(final int n) {
        if (!SKF.logined) {
            return null;
        }
        return new SKF_PrivateKey(this, n);
    }

    @Override
    public byte[] doSign(final byte[] array, final int n, final int n2) throws Exception {
        if (!SKF.logined) {
            return null;
        }
        try {
            synchronized (SKF.class) {
                final byte[] sign = sign(array, n, n2);
                return sign;
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }
}
