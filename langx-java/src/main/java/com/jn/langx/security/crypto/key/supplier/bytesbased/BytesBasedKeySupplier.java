package com.jn.langx.security.crypto.key.supplier.bytesbased;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;
import com.jn.langx.security.crypto.key.supplier.KeySupplier;

import java.security.Key;
import java.security.Provider;

/**
 * 定义一个基于字节数组的密钥供应商接口，用于生成指定算法的密钥
 * 该接口扩展了 KeySupplier 接口，专门针对字节数组作为密钥源的情况
 * 它允许调用者提供字节数组、算法名称和可选的加密服务提供者来生成密钥对象
 *
 * @param <O> 生成的密钥对象类型，必须是 Key 接口的实现
 */
public interface BytesBasedKeySupplier<O extends Key> extends KeySupplier<byte[], String, Provider, O> {
    /**
     * 使用给定的字节数组、算法名称和加密服务提供者生成密钥对象
     * 此方法允许通过指定精确的算法和可选的加密服务提供者来生成密钥对象
     * 如果提供者为 null，将使用默认的提供者
     *
     * @param bytes 密钥的字节数组，不能为空
     * @param algorithm 用于生成密钥的算法名称，不能为空
     * @param provider 可选的加密服务提供者，如果不需要指定提供者，可以为 null
     * @return 生成的密钥对象，类型为 O
     */
    @Override
    O get(@NonNull byte[] bytes, @NonNull String algorithm, @Nullable Provider provider);
}
