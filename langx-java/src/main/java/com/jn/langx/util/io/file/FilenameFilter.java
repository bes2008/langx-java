package com.jn.langx.util.io.file;

import com.jn.langx.util.function.Predicate2;

import java.io.File;

/**
 * This interface extends the functionality of java.io.FilenameFilter and Predicate2<File, String>,
 * providing a file name filtering interface that can be used to filter files based on a directory and a filename.
 * It is mainly used to implement custom file filtering logic, allowing for precise control over file filtering processes.
 *
 * paramters:
 * 1)File: the directory
 * 2)String: the filename
 *
 * @author jinuo.fang
 */
public interface FilenameFilter extends java.io.FilenameFilter, Predicate2<File, String> {
}
