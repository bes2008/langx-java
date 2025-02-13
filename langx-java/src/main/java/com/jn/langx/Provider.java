package com.jn.langx;

/**
 * Provider接口扩展了Factory接口，用于定义一个可以获取输出的提供者。
 * 它代表了一个能够根据输入参数生成或提供特定输出的组件。
 * 该接口的主要用途是为输入值提供一个标准的访问方式，从而获取相应的输出值
 *
 * @param <I> 输入类型，表示提供给提供者的输入值的类型
 * @param <O> 输出类型，表示提供者生成或提供的输出值的类型
 */
public interface Provider<I, O> extends Factory<I, O> {
    /**
     * 根据提供的输入值获取相应的输出值
     * 此方法覆盖了Factory接口中的get方法，提供了更具体的实现
     *
     * @param input 输入值，用于确定要获取的输出值的类型和内容
     * @return 根据输入值生成或提供的输出值
     */
    @Override
    O get(I input);
}
