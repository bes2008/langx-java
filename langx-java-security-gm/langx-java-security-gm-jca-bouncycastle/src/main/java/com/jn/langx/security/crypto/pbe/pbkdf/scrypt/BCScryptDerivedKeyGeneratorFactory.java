package com.jn.langx.security.crypto.pbe.pbkdf.scrypt;

import com.jn.langx.security.crypto.key.PKIs;
import com.jn.langx.security.crypto.pbe.pbkdf.DerivedKeyGenerator;
import com.jn.langx.security.crypto.pbe.pbkdf.PBKDFKeySpec;
import com.jn.langx.security.crypto.pbe.pbkdf.PasswordToPkcs5Utf8Converter;
import com.jn.langx.security.crypto.pbe.pswdenc.scrypt.ScryptDerivedKeyGeneratorFactory;
import com.jn.langx.security.crypto.pbe.pswdenc.scrypt.ScryptPBKDFKeySpec;
import com.jn.langx.text.StringTemplates;

public class BCScryptDerivedKeyGeneratorFactory implements ScryptDerivedKeyGeneratorFactory {
    @Override
    public DerivedKeyGenerator get(PBKDFKeySpec keySpec) {
        ScryptPBKDFKeySpec keyGenParameters = (ScryptPBKDFKeySpec) keySpec;
        char[] rawPassword = keyGenParameters.getPassword();
        byte[] salt = keyGenParameters.getSalt();

        int keyBytesLength = PKIs.getBytesLength(keySpec.getKeyLength());
        int cpuCost = keyGenParameters.getCpuCost();
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
            throw new IllegalArgumentException(StringTemplates.formatWithPlaceholder("Parallelisation parameter must be >= 1 and <= {}  (based on block size memoryCost of {})", maxParallel, memoryCost));
        }
        if (keyBytesLength < 1) {
            throw new IllegalArgumentException("Key length must be >= 1 and <= " + Integer.MAX_VALUE);
        }

        BCScryptDerivedKeyGenerator keyGenerator = new BCScryptDerivedKeyGenerator();
        keyGenerator.init(new PasswordToPkcs5Utf8Converter().apply(rawPassword), salt, keySpec.getIterationCount());
        keyGenerator.setCpuCost(cpuCost);
        keyGenerator.setMemoryCost(memoryCost);
        keyGenerator.setParallelization(parallelization);
        return keyGenerator;
    }
}
