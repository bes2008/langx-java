package com.jn.langx.security.crypto.pbe.pswdenc.scrypt;

import com.jn.langx.codec.StringifyFormat;
import com.jn.langx.codec.Stringifys;
import com.jn.langx.security.SecurityException;
import com.jn.langx.security.Securitys;
import com.jn.langx.security.crypto.pbe.pbkdf.DerivedPBEKey;
import com.jn.langx.security.crypto.pbe.pbkdf.PBKDFEngine;
import com.jn.langx.security.crypto.pbe.pbkdf.PBKDFEngineBuilder;
import com.jn.langx.security.crypto.pbe.pswdenc.PasswordEncryptor;
import com.jn.langx.security.crypto.salt.RandomBytesSaltGenerator;
import com.jn.langx.util.Objs;
import com.jn.langx.util.spi.CommonServiceProvider;


public class ScryptPasswordEncryptor implements PasswordEncryptor {
    private int cpuCost;

    private int memoryCost;

    private int parallelization;

    private int keyBitLength;

    private int saltBitLength;

    public ScryptPasswordEncryptor() {
        this(65536, 8, 1, 32 * 8, 16 * 6);
    }

    public ScryptPasswordEncryptor(int cpuCost, int memoryCost, int parallelization, int keyBitLength, int saltBitLength) {
        this.cpuCost = cpuCost;
        this.memoryCost = memoryCost;
        this.parallelization = parallelization;
        this.keyBitLength = keyBitLength;
        this.saltBitLength = saltBitLength;
    }

    @Override
    public String encrypt(String password) {
        byte[] salt = new RandomBytesSaltGenerator().get(Securitys.getBytesLength(this.saltBitLength));
        DerivedPBEKey pbeKey = generateSCryptKey("scrypt", password, salt, cpuCost, memoryCost, parallelization, keyBitLength);

        String params = Long.toString(
                ((int) (Math.log(this.cpuCost) / Math.log(2)) << 16L) | this.memoryCost << 8 | this.parallelization,
                16);
        StringBuilder builder = new StringBuilder((salt.length + pbeKey.getEncoded().length) * 2);
        builder.append("$").append(params).append('$');
        builder.append(Stringifys.stringify(salt, StringifyFormat.BASE64)).append('$');
        builder.append(Stringifys.stringify(pbeKey.getEncoded(), StringifyFormat.BASE64));
        return builder.toString();

    }

    @Override
    public boolean check(String plainPassword, String encryptedPassword) {
        String[] parts = encryptedPassword.split("\\$");
        if (parts.length != 4) {
            return false;
        } else {
            long params = Long.parseLong(parts[1], 16);
            byte[] salt = Stringifys.toBytes(parts[2], StringifyFormat.BASE64);
            byte[] secretKey = Stringifys.toBytes(parts[3], StringifyFormat.BASE64);
            int cpuCost = (int) Math.pow(2.0, (double) (params >> 16 & 65535L));
            int memoryCost = (int) params >> 8 & 255;
            int parallelization = (int) params & 255;

            DerivedPBEKey pbeKey = generateSCryptKey("scrypt", plainPassword, salt, cpuCost, memoryCost, parallelization, keyBitLength);
            return Objs.deepEquals(secretKey, pbeKey.getEncoded());
        }
    }

    private DerivedPBEKey generateSCryptKey(String pbeAlgorithm, String password, byte[] salt, int cpuCost, int memoryCost, int parallelization, int keyBitLength) {
        ScryptPBKDFKeySpec keySpec = new ScryptPBKDFKeySpec(password.toCharArray(), salt, keyBitLength, 1);
        keySpec.setParallel(parallelization);
        keySpec.setCpuCost(cpuCost);
        keySpec.setMemoryCost(memoryCost);


        ScryptDerivedKeyGeneratorFactory factory = CommonServiceProvider.loadFirstService(ScryptDerivedKeyGeneratorFactory.class);
        if (factory == null) {
            throw new SecurityException("ScryptDerivedKeyGeneratorFactory impl not found, check the classpath for 'langx-java-security-gm-jca-bouncycastle.jar'");
        }

        PBKDFEngine engine = new PBKDFEngineBuilder().withKeyGeneratorFactory(factory).build();

        DerivedPBEKey pbeKey = engine.apply(pbeAlgorithm, keySpec);
        return pbeKey;
    }


}
