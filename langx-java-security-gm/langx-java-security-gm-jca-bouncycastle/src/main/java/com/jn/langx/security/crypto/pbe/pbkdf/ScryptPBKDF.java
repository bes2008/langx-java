package com.jn.langx.security.crypto.pbe.pbkdf;

import com.jn.langx.security.crypto.key.PKIs;
import com.jn.langx.util.Strings;
import org.bouncycastle.crypto.generators.SCrypt;

public class ScryptPBKDF implements PBKDF {
    public static DerivedPBEKey generateSecretKey(String pbeAlgorithm, String password, byte[] salt, int cpuCost, int memoryCost, int parallelization, int keyBitLength){
        ScryptPBKDFKeySpec keySpec = new ScryptPBKDFKeySpec(password.toCharArray(), salt, keyBitLength, 1);
        keySpec.setParallel(parallelization);
        keySpec.setCpuCost(cpuCost);
        keySpec.setMemoryCost(memoryCost);
        DerivedPBEKey pbeKey = new ScryptPBKDF().apply(pbeAlgorithm, keySpec);
        return pbeKey;
    }


    @Override
    public DerivedPBEKey apply(String pbeAlgorithm, PBKDFKeySpec keySpec) {
        ScryptPBKDFKeySpec keyGenParameters = (ScryptPBKDFKeySpec)keySpec;
        char[] rawPassword = keyGenParameters.getPassword();
        byte[] salt = keyGenParameters.getSalt();

        int keyBytesLength = PKIs.getBytesLength(keySpec.getKeyLength());
        int cpuCost= keyGenParameters.getCpuCost();
        if (cpuCost <= 1) {
            throw new IllegalArgumentException("Cpu cost parameter must be > 1.");
        }
        int memoryCost = keyGenParameters.getMemoryCost();
        if (memoryCost < 1) {
            throw new IllegalArgumentException("Memory cost must be >= 1.");
        }
        if (memoryCost == 1 && cpuCost > 65536) {
            throw new IllegalArgumentException("Cpu cost parameter must be > 1 and < 65536.");
        }

        int maxParallel = Integer.MAX_VALUE / (128 * memoryCost * 8);
        int parallelization = keyGenParameters.getParallel();
        if (parallelization < 1 || parallelization > maxParallel) {
            throw new IllegalArgumentException("Parallelisation parameter p must be >= 1 and <= " + maxParallel
                    + " (based on block size r of " + memoryCost + ")");
        }
        if (keyBytesLength < 1 || keyBytesLength > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Key length must be >= 1 and <= " + Integer.MAX_VALUE);
        }

        byte[] secretKey = SCrypt.generate(Strings.getBytesUtf8(new String(rawPassword)), salt, cpuCost, memoryCost, parallelization, keyBytesLength);
        DerivedPBEKey pbeKey = new DerivedPBEKey(pbeAlgorithm, keySpec, secretKey);
        return pbeKey;
    }
}
