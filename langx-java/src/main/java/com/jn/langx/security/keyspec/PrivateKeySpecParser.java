package com.jn.langx.security.keyspec;

import com.jn.langx.factory.Factory;

import java.security.spec.KeySpec;

/**
 * <pre>
 *    基本编码规则（BER, Basic Encoding Rules） -X.209
 *     规范编码规则（CER, Canonical Encoding Rules）
 *     识别名编码规则（DER, Distinguished Encoding Rules）
 *     压缩编码规则（PER， Packed Encoding Rules）
 *     XML编码规则（XER， XML Encoding Rules）
 *
 *     其中BER、CER、DER、PER都属于二进制编码，相关密钥文件和证书文件一般采用的是DER编码；
 *     </pre>
 *
 * @param <KS>
 */
public interface PrivateKeySpecParser<KS extends KeySpec> extends Factory<byte[], KS> {
    @Override
    KS get(byte[] encodedBytes);
}
