package com.jn.langx.security.pbe.pbkdf;

import com.jn.langx.util.function.Function2;

/**
 * 基于一个 password 来生成 secret key
 */
public interface PBKDF extends Function2<String, PBKDFKeySpec, DerivedPBEKey> {
    DerivedPBEKey apply(String pbeAlgorithm, PBKDFKeySpec keySpec);
}
