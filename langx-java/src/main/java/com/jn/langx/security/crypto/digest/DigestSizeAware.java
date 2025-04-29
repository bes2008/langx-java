package com.jn.langx.security.crypto.digest;

/**
 * BufferSizeAware接口用于获取缓冲区大小信息
 * 实现该接口的类必须提供获取缓冲区大小的方法
 */
public interface DigestSizeAware {
    /**
     * 获取缓冲区大小
     *
     * @return 缓冲区的大小，以字节为单位
     */
    int getDigestSize();
}
