package com.jn.langx.security.pbe.pbkdf;

import com.jn.langx.util.function.Function3;

import java.security.SecureRandom;

/**
 * 基于一个 password 来生成 secret key
 */
public interface PBKDF extends Function3<String, PBKDFKeySpec, SecureRandom, DerivedPBEKey> {
    DerivedPBEKey apply(String pbeAlgorithm, PBKDFKeySpec keySpec, SecureRandom secureRandom);
}
