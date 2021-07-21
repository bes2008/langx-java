package com.jn.langx.security.gm.crypto.sm4;

import org.bouncycastle.jcajce.provider.symmetric.util.BaseBlockCipher;
/**
 * @see // cn.gmssl.crypto.impl.SM4JCE$ECB
 */
public class SM4EcbBlockCipher extends BaseBlockCipher
{
    public SM4EcbBlockCipher() {
        super(new SM4BlockCipher());
    }
}