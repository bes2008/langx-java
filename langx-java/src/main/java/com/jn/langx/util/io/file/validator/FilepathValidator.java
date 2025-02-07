package com.jn.langx.util.io.file.validator;


/**
 * 文件路径验证器接口
 * 用于定义验证文件名和文件路径合法性的方法
 */
public interface FilepathValidator {
    /**
     * 检查字符串是否是合法的文件名
     * 此方法仅用于判定文件名，不包括路径信息
     *
     * @param name 待验证的文件名字符串
     * @return 如果文件名合法返回 true，否则返回 false
     */
    boolean isLegalFilename(String name);

    /**
     * 检查字符串是否是合法的文件路径
     * 此方法用于判定完整的文件路径（包括文件名）是否合法
     *
     * @param path 待验证的文件路径字符串
     * @return 如果文件路径合法返回 true，否则返回 false
     */
    boolean isLegalFilepath(String path);
}
