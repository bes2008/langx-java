package com.jn.langx.security.crypto.pbe.pswdenc.argon2;

import com.jn.langx.codec.StringifyFormat;
import com.jn.langx.codec.Stringifys;
import com.jn.langx.security.Securitys;
import com.jn.langx.security.crypto.pbe.pbkdf.PBKDFEngine;
import com.jn.langx.security.crypto.pbe.pswdenc.PasswordEncryptor;
import com.jn.langx.security.crypto.salt.RandomBytesSaltGenerator;
import com.jn.langx.util.Objs;
import com.jn.langx.util.Strings;
import com.jn.langx.util.struct.Pair;

/**
 * @since 5.5.0
 */
public class Argon2PasswordEncryptor implements PasswordEncryptor {
    private int saltBitLength;
    /**
     * 生成的 hash 的 长度
     */
    private int hashBitLength;

    private int parallelism;

    private int memory;

    private int iterations;

    public Argon2PasswordEncryptor() {
        this(16 * 8, 32 * 8, 1, 1 << 14, 2);
    }

    public Argon2PasswordEncryptor(int saltBitLength, int hashBitLength, int parallelism, int memory, int iterations) {
        this.saltBitLength = saltBitLength;
        this.hashBitLength = hashBitLength;
        this.parallelism = parallelism;
        this.memory = memory;
        this.iterations = iterations;
    }

    @Override
    public String encrypt(String rawPassword) {
        byte[] salt = new RandomBytesSaltGenerator().get(Securitys.getBytesLength(this.saltBitLength));
        // @formatter:off
        Argon2KeySpec params = new Argon2KeySpecBuilder(Argon2Constants.ARGON2_id)
                .withSalt(salt)
                .withParallelism(this.parallelism)
                .withMemoryAsKB(this.memory)
                .withIterations(this.iterations)
                .withPassword(rawPassword.toCharArray())
                .withKeyBitSize(this.hashBitLength)
                .build();
        byte[] hash = new PBKDFEngine(new Argon2DerivedKeyGeneratorFactory()).apply("argon2", params).getEncoded();
        return stringifyHash(params, hash);
    }

    private String stringifyHash(Argon2KeySpec keySpec, byte[] hash) {
        StringBuilder stringBuilder = new StringBuilder();
        Argon2Parameters parameters = keySpec.getParameters();
        switch (parameters.getType()) {
            case Argon2Constants.ARGON2_d:
                stringBuilder.append("$argon2d");
                break;
            case Argon2Constants.ARGON2_i:
                stringBuilder.append("$argon2i");
                break;
            case Argon2Constants.ARGON2_id:
                stringBuilder.append("$argon2id");
                break;
            default:
                throw new IllegalArgumentException("Invalid algorithm type: " + parameters.getType());
        }
        stringBuilder.append("$v=").append(parameters.getVersion()).append("$m=").append(parameters.getMemory())
                .append(",t=").append(parameters.getIterations()).append(",p=").append(parameters.getLanes());
        if (parameters.getSalt() != null) {
            stringBuilder.append("$").append(Stringifys.stringify(parameters.getSalt(), StringifyFormat.BASE64));
        }
        stringBuilder.append("$").append(Stringifys.stringify(hash, StringifyFormat.BASE64));
        return stringBuilder.toString();
    }

    private Pair<byte[], Argon2KeySpec> extract(String rawPassword, String encodedPassword) {
        Argon2KeySpecBuilder paramsBuilder;
        String[] parts = encodedPassword.split("\\$");
        if (parts.length < 4) {
            throw new IllegalArgumentException("Invalid encoded Argon2-hash");
        }
        int currentPart = 1;
        String type = parts[currentPart++];
        if (Strings.equals("argon2d", type)) {
            paramsBuilder = new Argon2KeySpecBuilder(Argon2Constants.ARGON2_d);
        } else if (Strings.equals("argon2i", type)) {
            paramsBuilder = new Argon2KeySpecBuilder(Argon2Constants.ARGON2_i);
        } else if (Strings.equals("argon2id", type)) {
            paramsBuilder = new Argon2KeySpecBuilder(Argon2Constants.ARGON2_id);
        } else {
            throw new IllegalArgumentException("Invalid algorithm type");
        }
        if (parts[currentPart].startsWith("v=")) {
            paramsBuilder.withVersion(Integer.parseInt(parts[currentPart].substring(2)));
            currentPart++;
        }
        String[] performanceParams = parts[currentPart++].split(",");
        if (performanceParams.length != 3) {
            throw new IllegalArgumentException("Amount of performance parameters invalid");
        }
        if (!performanceParams[0].startsWith("m=")) {
            throw new IllegalArgumentException("Invalid memory parameter");
        }
        paramsBuilder.withMemoryAsKB(Integer.parseInt(performanceParams[0].substring(2)));
        if (!performanceParams[1].startsWith("t=")) {
            throw new IllegalArgumentException("Invalid iterations parameter");
        }
        paramsBuilder.withIterations(Integer.parseInt(performanceParams[1].substring(2)));
        if (!performanceParams[2].startsWith("p=")) {
            throw new IllegalArgumentException("Invalid parallelity parameter");
        }
        paramsBuilder.withParallelism(Integer.parseInt(performanceParams[2].substring(2)));
        String salt = parts[currentPart++];
        paramsBuilder.withSalt(Stringifys.toBytes(salt, StringifyFormat.BASE64));

        String hash = parts[currentPart];
        byte[] hashBytes = Stringifys.toBytes(hash, StringifyFormat.BASE64);

        paramsBuilder.withKeyBitSize(hashBytes.length * 8).withPassword(rawPassword.toCharArray());

        return new Pair<byte[], Argon2KeySpec>(hashBytes, paramsBuilder.build());
    }

    @Override
    public boolean check(String rawPassword, String encryptedPassword) {
        if (encryptedPassword == null) {
            return false;
        }
        Pair<byte[], Argon2KeySpec> p = extract(rawPassword, encryptedPassword);
        byte[] actualHash = p.getKey();
        Argon2KeySpec keySpec = p.getValue();

        byte[] expectedHash = new PBKDFEngine(new Argon2DerivedKeyGeneratorFactory()).apply("argon2", keySpec).getEncoded();

        return Objs.deepEquals(expectedHash, actualHash);
    }
}
