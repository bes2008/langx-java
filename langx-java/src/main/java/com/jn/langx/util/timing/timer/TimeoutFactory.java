package com.jn.langx.util.timing.timer;

/**
 * TimeoutFactory是一个泛型接口，用于创建Timeout实例。
 * 它允许在特定的Timer实例上安排一个TimerTask，并指定一个截止时间。
 *
 * @since 4.0.5
 * @param <TIMER> 这个泛型参数表示Timer类型的子类，允许接口的实现者指定具体的Timer类型。
 * @param <TIMEOUT> 这个泛型参数表示Timeout类型的子类，允许接口的实现者指定具体的Timeout类型。
 */
public interface TimeoutFactory<TIMER extends Timer, TIMEOUT extends Timeout> {
    /**
     * 创建一个Timeout实例。
     *
     * @param timer 一个Timer实例，用于安排计时任务。
     * @param task 一个TimerTask实例，表示需要执行的任务。
     * @param deadline 一个long类型的值，表示任务的截止时间。
     * @return 返回一个TIMEOUT类型的实例，表示安排好的计时任务。
     */
    TIMEOUT create(TIMER timer, TimerTask task, long deadline);
}
