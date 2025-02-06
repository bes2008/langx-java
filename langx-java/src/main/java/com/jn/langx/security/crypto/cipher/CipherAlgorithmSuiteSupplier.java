package com.jn.langx.security.crypto.cipher;

import com.jn.langx.Ordered;
import com.jn.langx.util.function.Supplier0;

import java.util.List;

/**
 * 密码算法套件供应商接口，继承自Supplier0和Ordered接口
 * 该接口主要用于提供一组密码算法套件，允许在不同场景下灵活选择加密算法
 *
 * @since 4.2.7
 */
public interface CipherAlgorithmSuiteSupplier extends Supplier0<List<CipherAlgorithmSuite>>, Ordered {
    /**
     * 获取密码算法套件列表
     *
     * @return 密码算法套件的列表
     */
    @Override
    List<CipherAlgorithmSuite> get();
}
