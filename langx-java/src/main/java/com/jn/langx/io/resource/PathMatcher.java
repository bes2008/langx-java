package com.jn.langx.io.resource;

import com.jn.langx.Matcher;

/**
 * PathMatcher接口继承了Matcher接口，专门用于路径匹配操作
 * 它的目的是提供一个标准的方式来检查给定的路径是否匹配特定的条件或模式
 * 这个接口主要在需要对资源路径进行过滤或匹配操作时使用，例如在Web应用中匹配URL路径
 */
public interface PathMatcher extends Matcher<String, Boolean> {
    /**
     * 判断给定路径是否匹配此PathMatcher定义的模式或条件
     *
     * @param path 待匹配的路径字符串，例如文件路径或URL路径
     * @return 如果路径匹配此PathMatcher定义的模式或条件，则返回true；否则返回false
     */
    @Override
    Boolean matches(String path);
}
