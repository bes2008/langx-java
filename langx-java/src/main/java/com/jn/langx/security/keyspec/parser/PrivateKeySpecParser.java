package com.jn.langx.security.keyspec.parser;

import com.jn.langx.factory.Factory;

import java.security.spec.KeySpec;

public interface PrivateKeySpecParser<KS extends KeySpec> extends Factory<byte[], KS> {
    @Override
    KS get(byte[] derEncodedBytes);
}
