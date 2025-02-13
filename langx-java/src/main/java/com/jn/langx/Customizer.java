package com.jn.langx;

/**
 * 定义一个定制化接口，用于修改或定制特定类型的对象
 * <p>
 * 使用场景：当需要提供一种机制来允许调用者在运行时修改或扩展对象的行为，而又不希望直接修改对象的源代码时
 * 可以使用本接口
 *
 * @param <T> 表示Customizer可以针对的任何对象类型
 */
public interface Customizer<T> {
    /**
     * 对给定的目标对象进行定制化操作
     *
     * @param target 要进行定制化的对象，其具体类型由调用者决定
     */
    void customize(T target);
}
