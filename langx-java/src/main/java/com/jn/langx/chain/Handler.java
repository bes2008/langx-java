package com.jn.langx.chain;


import com.jn.langx.lifecycle.Destroyable;
import com.jn.langx.lifecycle.Initializable;
import com.jn.langx.util.function.Handler3;

/**
 * Handler接口定义了处理请求和响应的合同，它继承自Handler3接口并实现了Initializable和Destroyable接口
 * 这个接口的主要作用是处理给定的请求，并生成相应的响应，它允许在处理链中通过链式调用方式执行多个处理步骤
 *
 * @param <REQ>  请求类型，表示处理链中传入的请求对象类型
 * @param <RESP> 响应类型，表示处理链中返回的响应对象类型
 */
public interface Handler<REQ, RESP> extends Handler3<REQ, RESP, Chain<REQ, RESP>>, Initializable, Destroyable {
    /**
     * 处理给定的请求并生成响应这个方法允许通过链式调用来整合多个处理步骤，形成一个处理链
     *
     * @param request  请求对象，包含需要处理的请求信息
     * @param response 响应对象，包含对请求的响应信息
     * @param chain    处理链对象，允许当前处理步骤结束后将控制权转交给链中的下一个处理者
     */
    void handle(REQ request, RESP response, Chain<REQ, RESP> chain);
}
