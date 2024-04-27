package com.jn.langx.security.pbe.cipher.kdf;

import com.jn.langx.security.SecurityException;
import com.jn.langx.util.Objs;

public class OpenSSLEvpKDFGeneratorSpi extends PBKDFKeyGeneratorSpi {
    @Override
    protected DerivedKey engineGenerateDerivedKey() {
        PBKDF kdf = new EvpKDF();
        if( Objs.isEmpty(this.algorithmParameter.getSalt())){
            this.algorithmParameter.setSalt(kdf.genSalt(this.secureRandom, this.algorithmParameter.getSaltBitSize(),1));
        }
        DerivedKey derivedKey=null;
        try {
             derivedKey= kdf.generate(
                    this.algorithmParameter.getPswd(),
                    this.algorithmParameter.getSalt(),
                    this.algorithmParameter.getKeyBitSize(),
                    this.algorithmParameter.getIvBitSize(),
                    this.algorithmParameter.getIterations(),
                    this.algorithmParameter.getHashAlgorithm());
        }catch (Throwable e){
            throw new SecurityException(e);
        }
        return derivedKey;
    }
}
