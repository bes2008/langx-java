package com.jn.langx.util.function;

/**
 * Function0接口定义了一个无参数、无返回值的函数式接口。
 * 这个接口主要用于在不需要任何输入参数，也不需要返回任何结果的场景下使用。
 * 例如，它可以用于表示一个简单的操作或行为，而这个操作不需要任何上下文信息。
 */
public interface Function0 {
    /**
     * apply方法是Function0接口中定义的抽象方法。
     * 它没有任何输入参数，也不返回任何值。
     * 实现这个接口的类需要提供这个方法的具体实现，以执行特定的操作。
     */
    void apply();
}
