package com.jn.langx.classpath.cp;

import com.jn.langx.classpath.*;
import com.jn.langx.io.resource.Location;
import com.jn.langx.io.resource.Locations;
import com.jn.langx.io.resource.Resource;
import com.jn.langx.util.Emptys;
import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.function.Function;
import com.jn.langx.util.function.Predicate;

import java.util.List;
import java.util.Set;

public abstract class AbstractClasspath implements Classpath {
    @Override
    public ClassFile findClassFile(String classname) {
        Resource resource = findResource(Classpaths.classNameToPath(classname));
        if (resource == null) {
            return null;
        }
        return new ResourceClassFile(resource);
    }

    @Override
    public List<ClassFile> scanClassFiles(String packageName, ResourceFilter filter) {
        List<Resource> resources = scanResources(packageName, filter);
        return Pipeline.of(resources).map(new Function<Resource, ClassFile>() {
            @Override
            public ClassFile apply(Resource resource) {
                if (resource == null) {
                    return null;
                }
                return new ResourceClassFile(resource);
            }
        }).clearNulls().asList();
    }

    @Override
    public List<Resource> scanResources(String namespace, ResourceFilter filter) {
        namespace = Classpaths.getCanonicalFilePath(namespace);
        Set<Location> locations = scanResourceLocations(namespace, filter);
        final Location root = getRoot();
        return Pipeline.of(locations).map(new Function<Location, Resource>() {
            @Override
            public Resource apply(Location location) {
                String relativePath = Locations.getRelativePath(root, location);
                if (relativePath == null) {
                    return null;
                } else {
                    return findResource(relativePath);
                }
            }
        }).clearNulls().asList();
    }

    /**
     * 如果 namespace 是 null,则代表直接在 root 下递归检索
     *
     * @param namespace root下的 namespace
     * @param filter    filter
     * @return 搜索到的资源
     */
    @Override
    public Set<Location> scanResourceLocations(final String namespace, ResourceFilter filter) {
        Pipeline<Location> pipeline = Pipeline.of(allResources());

        final Location root = getRoot();
        final Location namespaceLocation = Locations.newLocation(root, namespace);

        if (Emptys.isNotEmpty(namespace)) {
            pipeline = pipeline.filter(
                    new Predicate<Location>() {
                        @Override
                        public boolean test(Location location) {
                            return Locations.getRelativePath(root, namespaceLocation) != null;
                        }
                    }
            );
        }
        if (filter != null) {
            pipeline = pipeline.filter(filter);
        }
        return pipeline.asSet(true);
    }


}
