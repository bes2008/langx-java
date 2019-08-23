package com.jn.langx.util.io.file;

import com.jn.langx.Filter;
import com.jn.langx.util.function.Predicate;

import java.io.File;

public interface FileFilter extends java.io.FileFilter, Filter<File>, Predicate<File>, FilenameFilter {

}
