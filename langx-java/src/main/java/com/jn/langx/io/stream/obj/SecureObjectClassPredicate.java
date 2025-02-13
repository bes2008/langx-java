package com.jn.langx.io.stream.obj;

import com.jn.langx.util.function.Predicate;

import java.io.ObjectStreamClass;

/**
 * 自定义谓词接口，用于检查对象序列化类是否满足特定安全条件
 * 主要用于序列化和反序列化过程中，对允许加载的类进行安全过滤
 *
 * @since 5.2.9
 */
public interface SecureObjectClassPredicate extends Predicate<ObjectStreamClass> {
    /**
     * 检查指定的对象序列化类是否满足安全条件
     *
     * @param objectStreamClass 对象序列化类，代表待检查的类
     * @return 如果类满足安全条件，则返回true；否则返回false
     */
    @Override
    public boolean test(ObjectStreamClass objectStreamClass);
}
