package com.jn.langx.security.crypto.pbe.pswdenc;

import com.jn.langx.codec.StringifyFormat;
import com.jn.langx.codec.Stringifys;
import com.jn.langx.security.crypto.mac.HMacs;
import com.jn.langx.util.io.Charsets;

public class HMacPasswordEncryptor extends CheckByEncryptEncryptor{
    private String hmacAlgorithm;
    private byte[] secretKey;

    private StringifyFormat format;
    public HMacPasswordEncryptor(String hmacAlgorithm, byte[] secretKey, StringifyFormat format){
        this.hmacAlgorithm=hmacAlgorithm;
        this.secretKey = secretKey;
        this.format = format;
    }

    @Override
    public String encrypt(String password) {
        byte[] bytes= HMacs.hmac(hmacAlgorithm, secretKey, password.getBytes(Charsets.UTF_8));
        return Stringifys.stringify(bytes, this.format);
    }
}
