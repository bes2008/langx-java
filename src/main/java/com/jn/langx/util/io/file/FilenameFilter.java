package com.jn.langx.util.io.file;

import com.jn.langx.util.function.Predicate2;

import java.io.File;

/**
 * paramters:
 * 1)File: the directory
 * 2)String: the filename
 *
 * @author jinuo.fang
 */
public interface FilenameFilter extends java.io.FilenameFilter, Predicate2<File, String> {
}
