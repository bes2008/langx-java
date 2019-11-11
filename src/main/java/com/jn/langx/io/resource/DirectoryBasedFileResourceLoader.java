/*
 * Copyright 2019 the original author or authors.
 *
 * Licensed under the LGPL, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at  http://www.gnu.org/licenses/lgpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jn.langx.io.resource;

import com.jn.langx.text.StringTemplates;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.function.Function;
import com.jn.langx.util.io.file.FileFilter;
import com.jn.langx.util.io.file.filter.ExistsFileFilter;
import com.jn.langx.util.io.file.filter.IsDirectoryFileFilter;
import com.jn.langx.util.io.file.filter.ReadableFileFilter;

import java.io.File;
import java.util.List;

public class DirectoryBasedFileResourceLoader implements ResourceLoader {
    private String directory;

    private ResourceLoader delegate;

    public DirectoryBasedFileResourceLoader(String directory) {
        this(directory, null);
    }

    public DirectoryBasedFileResourceLoader(String directory, ClassLoader classLoader) {
        Preconditions.checkNotNull(directory, "directory is null");
        Preconditions.checkTrue(new ExistsFileFilter().test(new File(directory)), StringTemplates.formatWithPlaceholder("directory {} is not exists", directory));
        Preconditions.checkTrue(new IsDirectoryFileFilter().test(new File(directory)), StringTemplates.formatWithPlaceholder("directory {} is not a directory", directory));
        Preconditions.checkTrue(new ReadableFileFilter().test(new File(directory)), StringTemplates.formatWithPlaceholder("directory {} is not readable", directory));
        this.directory = directory;

        this.delegate = new DefaultResourceLoader(classLoader);
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    @Override
    public FileResource loadResource(String filename) {
        return delegate.<FileResource>loadResource(FileResource.PREFIX + directory + File.separator + filename);
    }

    @Override
    public ClassLoader getClassLoader() {
        return delegate.getClassLoader();
    }

    public List<File> listFiles() {
        File dir = new File(directory);
        return Pipeline.of(dir.listFiles()).map(new Function<File, File>() {
            @Override
            public File apply(File file) {
                return file.getAbsoluteFile();
            }
        }).asList();
    }

    public List<File> listFiles(FileFilter fileFilter) {
        File dir = new File(directory);
        return Pipeline.of(dir.listFiles((java.io.FileFilter) fileFilter)).map(new Function<File, File>() {
            @Override
            public File apply(File file) {
                return file.getAbsoluteFile();
            }
        }).asList();
    }
}
