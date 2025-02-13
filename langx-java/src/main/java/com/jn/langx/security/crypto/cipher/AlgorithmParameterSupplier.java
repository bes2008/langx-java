package com.jn.langx.security.crypto.cipher;

import java.security.Key;
import java.security.Provider;
import java.security.SecureRandom;

/**
 * AlgorithmParameterSupplier 接口定义了一个用于获取算法参数的供应商接口
 * 它提供了一种根据密钥、算法名称、转换类型、安全提供者和随机源来获取算法参数的机制
 * 主要用于密码学操作中，当需要根据特定条件动态获取算法参数时
 *
 * @see java.security.spec.AlgorithmParameterSpec
 * @see java.security.AlgorithmParameters
 */
public interface AlgorithmParameterSupplier {
    /**
     * 根据提供的参数获取算法参数对象
     * 此方法允许调用者指定密钥、算法名称、转换类型、安全提供者和随机源，以获取相应的算法参数
     * 返回值类型可以是：
     * - {@link java.security.spec.AlgorithmParameterSpec}
     * - {@link java.security.AlgorithmParameters}
     *
     * @param key             用于生成算法参数的密钥
     * @param algorithm       算法名称，指定所需的算法类型
     * @param transform       转换类型，指定算法的具体操作模式（可选）
     * @param provider        安全提供者，指定实现算法的具体提供者（可选）
     * @param secureRandom    随机源，用于生成随机参数（可选）
     * @return Object         返回生成的算法参数对象，具体类型取决于算法和提供者
     */
    Object get(Key key, String algorithm, String transform, Provider provider, SecureRandom secureRandom);
}
