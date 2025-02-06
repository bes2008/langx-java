package com.jn.langx.security.crypto.provider;

/**
 * 定义了一个接口，用于配置语言安全提供者
 * 通过实现这个接口，开发者可以自定义语言安全提供者的配置方式
 */
public interface LangxSecurityProviderConfigurer {
    /**
     * 配置语言安全提供者的方法
     *
     * @param provider 语言安全提供者实例，通过这个实例可以进行安全配置
     * 这个方法允许调用者根据需要修改或增强语言安全提供者的配置
     */
    void configure(LangxSecurityProvider provider);
}
