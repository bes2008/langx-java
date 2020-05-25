package com.jn.langx.classpath.cp;

import com.jn.langx.classpath.Classpaths;
import com.jn.langx.io.resource.DirectoryBasedFileResourceLoader;
import com.jn.langx.io.resource.FileResource;
import com.jn.langx.io.resource.Location;
import com.jn.langx.io.resource.Resource;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Consumer2;
import com.jn.langx.util.function.Function2;
import com.jn.langx.util.io.file.FileFilters;
import com.jn.langx.util.io.file.filter.FilenameSuffixFilter;
import com.jn.langx.util.io.file.filter.IsFileFilter;
import com.jn.langx.util.io.file.filter.ReadableFileFilter;

import java.io.File;
import java.util.List;
import java.util.Set;

/**
 * 扫描指定目录下所有的jar, zip
 * 不会递归扫描子目录
 *
 * @see DirectoryClasspath
 */
public class JarDirectoryClasspath extends AbstractClasspath {
    private List<JarFileClasspath> jars = Collects.emptyArrayList();
    private Location root;

    public JarDirectoryClasspath(String dirName) {
        List<File> files = new DirectoryBasedFileResourceLoader(dirName)
                .listFiles(FileFilters.allFileFilter(
                        new IsFileFilter(),
                        new ReadableFileFilter(),
                        new FilenameSuffixFilter(new String[]{"jar", "zip"}, true)
                ));

        Collects.forEach(files, new Consumer2<Integer, File>() {
            @Override
            public void accept(Integer index, File jarfile) {
                jars.add(new JarFileClasspath(jarfile));
            }
        });

        root = new Location(FileResource.PREFIX, dirName);
    }

    @Override
    public Resource findResource(final String relativePath) {
        final String path = Classpaths.getCanonicalFilePath(relativePath);
        return Collects.firstMap(jars, new Function2<Integer, JarFileClasspath, Resource>() {
            @Override
            public Resource apply(Integer index, JarFileClasspath jarClasspath) {
                return jarClasspath.findResource(path);
            }
        });
    }

    @Override
    public Location getRoot() {
        return root;
    }

    @Override
    public Set<Location> allResources() {
        final Set<Location> locations = Collects.emptyHashSet(true);
        Collects.forEach(jars, new Consumer2<Integer, JarFileClasspath>() {
            @Override
            public void accept(Integer key, JarFileClasspath jarClasspath) {
                locations.addAll(jarClasspath.allResources());
            }
        });
        return locations;
    }
}
