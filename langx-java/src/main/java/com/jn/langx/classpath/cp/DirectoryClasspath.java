package com.jn.langx.classpath.cp;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.classpath.Classpaths;
import com.jn.langx.io.resource.DirectoryBasedFileResourceLoader;
import com.jn.langx.io.resource.FileResource;
import com.jn.langx.io.resource.Location;
import com.jn.langx.io.resource.Resource;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Consumer;

import java.io.File;
import java.util.Set;

/**
 * 普通的目录，不会扫描它下面的 jar,zip文件
 *
 * @see JarDirectoryClasspath
 */
public class DirectoryClasspath extends AbstractClasspath {
    private DirectoryBasedFileResourceLoader loader;
    private Location root;

    public DirectoryClasspath(String rootDirectory) {
        this.root = new Location(FileResource.PREFIX, rootDirectory, "/");
        this.loader = new DirectoryBasedFileResourceLoader(rootDirectory);
    }

    @Override
    public Resource findResource(String relativePath, boolean isClass) {
        relativePath = Classpaths.getPath(relativePath, isClass, root.getPathSeparator());
        return loader.loadResource(relativePath);
    }

    @Override
    public Location getRoot() {
        return root;
    }

    @Override
    public Set<Location> allResources() {
        File rootFile = new File(root.getPath());
        Set<Location> locations = Collects.emptyTreeSet();
        if (rootFile.exists() && rootFile.isDirectory()) {
            scan(locations, rootFile);
        }
        return locations;
    }

    private void scan(@NonNull final Set<Location> results, @NonNull final File current) {
        if (current.isFile()) {
            results.add(new Location(FileResource.PREFIX, current.getAbsolutePath()));
        } else {
            Collects.forEach(current.listFiles(), new Consumer<File>() {
                @Override
                public void accept(File file) {
                    scan(results, file);
                }
            });
        }
    }
}
