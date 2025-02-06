package com.jn.langx.pipeline.simplex;


/**
 * SimplexPipeline接口定义了一个处理简单xes（Simplexes）的管道框架
 * 该框架允许在管道的开始或结束位置添加处理handler，并能够清除所有handler
 * 以及处理消息通过管道中的所有handler
 */
public interface SimplexPipeline {
    /**
     * 在管道的最前面添加一个handler
     *
     * @param handler 要添加的handler实例，不能为空
     */
    void addFirst(SimplexHandler handler);

    /**
     * 在管道的最后面添加一个handler
     *
     * @param handler 要添加的handler实例，不能为空
     */
    void addLast(SimplexHandler handler);

    /**
     * 清除管道中所有的handler
     * 这个方法在需要重置或重新配置管道时非常有用
     */
    void clear();

    /**
     * 将消息传递给管道中的所有handler进行处理
     *
     * @param message 要处理的消息对象，不能为空
     * @return 处理后的消息对象，具体类型取决于管道中handler的处理逻辑
     */
    Object handle(Object message);
}
