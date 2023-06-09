package com.jn.langx.util.io.file.validator;


public interface FilepathValidator {
    /**
     * 是合法的文件名，只用于判定文件名
     *
     * @return 合法返回 true
     */
    boolean isLegalFilename(String name);

    /**
     * 是合法的文件路径，用于判定文件路径我文件名
     *
     * @return 合法返回 true
     */
    boolean isLegalFilepath(String path);
}
