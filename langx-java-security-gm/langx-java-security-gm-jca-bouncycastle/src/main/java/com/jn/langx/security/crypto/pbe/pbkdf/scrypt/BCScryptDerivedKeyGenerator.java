package com.jn.langx.security.crypto.pbe.pbkdf.scrypt;

import com.jn.langx.security.Securitys;
import com.jn.langx.security.crypto.pbe.pbkdf.DerivedKeyGenerator;
import com.jn.langx.security.crypto.pbe.pbkdf.SimpleDerivedKey;
import org.bouncycastle.crypto.generators.SCrypt;

public class BCScryptDerivedKeyGenerator extends DerivedKeyGenerator {
    private int cpuCost;
    private int memoryCost;
    private int parallelization;

    public void setCpuCost(int cpuCost) {
        this.cpuCost = cpuCost;
    }

    public void setMemoryCost(int memoryCost) {
        this.memoryCost = memoryCost;
    }

    public void setParallelization(int parallelization) {
        this.parallelization = parallelization;
    }

    @Override
    public SimpleDerivedKey generateDerivedKey(int keySize) {
        int keyBytesLength = Securitys.getBytesLength(keySize);
        byte[] key = SCrypt.generate(password, salt, cpuCost, memoryCost, parallelization, keyBytesLength);
        return new SimpleDerivedKey(key);
    }

    @Override
    public SimpleDerivedKey generateDerivedKeyWithIV(int keySize, int ivSize) {
        return generateDerivedKey(keySize);
    }

    @Override
    public SimpleDerivedKey generateDerivedKeyUseHMac(int keySize) {
        return generateDerivedKey(keySize);
    }
}
