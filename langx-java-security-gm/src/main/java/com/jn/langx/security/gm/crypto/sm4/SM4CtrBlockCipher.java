package com.jn.langx.security.gm.crypto.sm4;

import com.jn.langx.security.gm.crypto.sm4.internal._SM4BlockCipher;
import org.bouncycastle.crypto.modes.CCMBlockCipher;
/**
 * @see // cn.gmssl.crypto.impl.SM4JCE$CTR
 */
public class SM4CtrBlockCipher extends CCMBlockCipher
{
    public SM4CtrBlockCipher() {
        super(new _SM4BlockCipher());
    }
}
