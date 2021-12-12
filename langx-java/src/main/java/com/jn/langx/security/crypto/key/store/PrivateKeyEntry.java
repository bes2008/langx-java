package com.jn.langx.security.crypto.key.store;

import java.security.PrivateKey;
import java.security.cert.Certificate;

public class PrivateKeyEntry<CERT extends Certificate> {
    private PrivateKey privateKey;
    private CERT[] certificateChain;

    public PrivateKeyEntry(PrivateKey key, CERT[] certificateChain){
        setPrivateKey(key);
        setCertificateChain(certificateChain);
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }

    public Certificate[] getCertificateChain() {
        return certificateChain;
    }

    public void setCertificateChain(CERT[] certificateChain) {
        this.certificateChain = certificateChain;
    }
}
