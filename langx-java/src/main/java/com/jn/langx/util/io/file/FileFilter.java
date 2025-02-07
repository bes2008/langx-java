package com.jn.langx.util.io.file;

import com.jn.langx.Filter;
import com.jn.langx.util.function.Predicate;

import java.io.File;

/**
 * FileFilter接口扩展了多个接口以提供灵活的文件过滤机制
 * 它可以根据文件对象、文件名或目录和文件名的组合来过滤文件
 */
public interface FileFilter extends java.io.FileFilter, Filter<File>, Predicate<File>, FilenameFilter {
    /**
     * 接受或拒绝指定的文件或目录
     * 此方法主要用于过滤基于文件对象的文件
     *
     * @param e 要过滤的文件或目录
     * @return 如果文件或目录被接受，则返回true；否则返回false
     */
    boolean accept(File e);

    /**
     * 根据文件对象判断是否通过过滤条件
     * 此方法等同于accept(File e)方法
     *
     * @param value 要测试的文件或目录
     * @return 如果文件或目录通过过滤条件，则返回true；否则返回false
     */
    @Override
    boolean test(File value);

    /**
     * 根据目录和文件名判断是否通过过滤条件
     * 此方法主要用于同时考虑目录上下文和文件名进行过滤
     *
     * @param key 要测试的文件所在目录
     * @param value 要测试的文件名
     * @return 如果文件通过过滤条件，则返回true；否则返回false
     */
    @Override
    boolean test(File key, String value);

    /**
     * 接受或拒绝指定目录中的指定文件名
     * 此方法等同于test(File key, String value)方法
     *
     * @param dir 要过滤的文件所在目录
     * @param name 要过滤的文件名
     * @return 如果文件名被接受，则返回true；否则返回false
     */
    @Override
    boolean accept(File dir, String name);
}
