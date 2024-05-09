package com.jn.langx.security.crypto.pbe.pswdenc;

import com.jn.langx.codec.StringifyFormat;
import com.jn.langx.codec.Stringifys;
import com.jn.langx.security.Securitys;
import com.jn.langx.security.crypto.pbe.pbkdf.DerivedPBEKey;
import com.jn.langx.security.crypto.pbe.pbkdf.ScryptPBKDF;
import com.jn.langx.security.crypto.salt.RandomBytesSaltGenerator;

import java.security.MessageDigest;

public class ScryptPasswordEncryptor implements PasswordEncryptor{
    private int cpuCost;

    private int memoryCost;

    private int parallelization;

    private int keyBitLength;

    private int saltBitLength;

    public ScryptPasswordEncryptor(int cpuCost, int memoryCost, int parallelization, int keyBitLength, int saltBitLength) {
        this.cpuCost=cpuCost;
        this.memoryCost=memoryCost;
        this.parallelization=parallelization;
        this.keyBitLength= keyBitLength;
        this.saltBitLength = saltBitLength;
    }

    @Override
    public String encrypt(String password) {
        byte[] salt = new RandomBytesSaltGenerator().get(Securitys.getBytesLength(this.saltBitLength));
        DerivedPBEKey pbeKey = ScryptPBKDF.generateSecretKey("scrypt", password, salt, cpuCost,memoryCost, parallelization, keyBitLength);

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
            byte[] secretKey = Stringifys.toBytes(parts[3],StringifyFormat.BASE64);
            int cpuCost = (int)Math.pow(2.0, (double)(params >> 16 & 65535L));
            int memoryCost = (int)params >> 8 & 255;
            int parallelization = (int)params & 255;

            DerivedPBEKey pbeKey = ScryptPBKDF.generateSecretKey("scrypt", plainPassword, salt, cpuCost, memoryCost, parallelization, keyBitLength);
            return MessageDigest.isEqual(secretKey, pbeKey.getEncoded());
        }
    }

}
