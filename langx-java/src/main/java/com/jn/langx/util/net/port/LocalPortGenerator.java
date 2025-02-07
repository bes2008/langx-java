package com.jn.langx.util.net.port;

import com.jn.langx.util.function.Supplier0;

/**
 * 本地端口生成器接口，继承自Supplier0<Integer>接口
 * 该接口的目的是提供一个标准方法来生成或获取一个Integer类型的本地端口号
 * 由于网络通信需要确定的端口号，因此这个接口抽象出了获取端口号的功能，
 * 允许具体的实现去定义如何生成或选择这些端口号
 */
public interface LocalPortGenerator extends Supplier0<Integer> {

}
