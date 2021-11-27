package com.jn.langx.util.io.file;

import com.jn.langx.Filter;
import com.jn.langx.annotation.NonNull;
import com.jn.langx.util.Emptys;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.function.Predicate;
import com.jn.langx.util.io.file.filter.AbstractFileFilter;
import com.jn.langx.util.io.file.filter.warp.FilePredicateToFilterFilter;
import com.jn.langx.util.io.file.filter.warp.FilenameFilterToFileFilter;
import com.jn.langx.util.io.file.filter.warp.FilterToFileFilter;
import com.jn.langx.util.io.file.filter.warp.JdkFileFilterToFileFilter;

import java.io.File;
import java.util.List;

public class FileFilters {
    public static FileFilter allFileFilter(@NonNull FileFilter... predicates) {
        return allFileFilter(Collects.newArrayList(predicates));
    }

    public static FileFilter allFileFilter(@NonNull List<? extends FileFilter> predicates) {
        Preconditions.checkTrue(Emptys.isNotEmpty(predicates));
        final Pipeline<FileFilter> pipeline = Pipeline.<FileFilter>of(predicates);
        return new AbstractFileFilter() {
            @Override
            public boolean accept(final File e) {
                return pipeline.allMatch(new Predicate<FileFilter>() {
                    @Override
                    public boolean test(FileFilter fileFilter) {
                        return fileFilter.test(e);
                    }
                });
            }

            @Override
            public boolean accept(File dir, String name) {
                return accept(new File(dir, name));
            }

        };
    }


    public static FileFilter anyFileFilter(@NonNull FileFilter... predicates) {
        return anyFileFilter(Collects.newArrayList(predicates));
    }

    public static FileFilter anyFileFilter(@NonNull List<? extends FileFilter> predicates) {
        Preconditions.checkTrue(Emptys.isNotEmpty(predicates));
        final Pipeline<FileFilter> pipeline = Pipeline.<FileFilter>of(predicates);
        return new AbstractFileFilter() {
            @Override
            public boolean accept(final File e) {
                return pipeline.anyMatch(new Predicate<FileFilter>() {
                    @Override
                    public boolean test(FileFilter fileFilter) {
                        return fileFilter.test(e);
                    }
                });
            }

            @Override
            public boolean accept(File dir, String name) {
                return accept(new File(dir, name));
            }
        };
    }

    /**
     * @since 4.1.0
     */
    public static FileFilter wrap(Predicate<File> filePredicate){
        return new FilePredicateToFilterFilter(filePredicate);
    }
    /**
     * @since 4.1.0
     */
    public static FileFilter wrap(FilenameFilter filter){
        return new FilenameFilterToFileFilter(filter);
    }
    /**
     * @since 4.1.0
     */
    public static FileFilter wrap(java.io.FilenameFilter filter){
        return new FilenameFilterToFileFilter(filter);
    }
    /**
     * @since 4.1.0
     */
    public static FileFilter wrap(Filter<File> filter){
        return new FilterToFileFilter(filter);
    }
    /**
     * @since 4.1.0
     */
    public static FileFilter wrap(java.io.FileFilter fileFilter){
        return new JdkFileFilterToFileFilter(fileFilter);
    }
}
