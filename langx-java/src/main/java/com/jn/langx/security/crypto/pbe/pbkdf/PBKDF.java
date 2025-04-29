package com.jn.langx.security.crypto.pbe.pbkdf;

import com.jn.langx.util.function.Function2;

/**
 * PBKDF接口继承了Function2接口，用于定义一个基于密码的密钥派生函数
 * 它接受一个字符串和一个PBKDFKeySpec对象作为输入，输出一个DerivedPBEKey对象
 * 主要用途是基于给定的密码和密钥规范来生成一个密钥，用于加密算法
 *
 * @see com.jn.langx.security.crypto.pbe.pbkdf.PBKDFEngine
 * @since 5.3.9
 * @deprecated
 */
@Deprecated
public interface PBKDF extends Function2<String, PBKDFKeySpec, DerivedPBEKey> {
    /**
     * 应用密钥派生函数来生成密钥
     *
     * @param pbeAlgorithm 加密算法的名称，例如"PBEWithMD5AndDES"
     * @param keySpec      包含密钥派生所需参数的规范，如盐值、迭代次数等
     * @return DerivedPBEKey 表示派生出来的密钥
     */
    DerivedPBEKey apply(String pbeAlgorithm, PBKDFKeySpec keySpec);
}
