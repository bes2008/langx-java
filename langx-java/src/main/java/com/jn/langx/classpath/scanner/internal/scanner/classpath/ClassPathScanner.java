package com.jn.langx.classpath.scanner.internal.scanner.classpath;


import com.jn.langx.classpath.scanner.ClassFilter;
import com.jn.langx.classpath.scanner.FilterResource;
import com.jn.langx.classpath.scanner.ResourceFilter;
import com.jn.langx.classpath.scanner.core.ClassPathScanException;
import com.jn.langx.io.resource.ClassPathResource;
import com.jn.langx.io.resource.Location;
import com.jn.langx.classpath.scanner.internal.EnvironmentDetection;
import com.jn.langx.classpath.scanner.internal.ResourceAndClassScanner;
import com.jn.langx.classpath.scanner.internal.scanner.classpath.jboss.JBossVFSv2UrlResolver;
import com.jn.langx.exception.UnsupportedEnvironmentException;
import com.jn.langx.io.resource.Resource;
import com.jn.langx.util.io.file.Files;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;

/**
 * ClassPath scanner.
 */
public class ClassPathScanner implements ResourceAndClassScanner {

    private static final Logger LOG = LoggerFactory.getLogger(ClassPathScanner.class);

    /**
     * The ClassLoader for loading migrations on the classpath.
     */
    private final ClassLoader classLoader;

    /**
     * Cache location lookups.
     */
    private final Map<Location, List<URL>> locationUrlCache = new HashMap<Location, List<URL>>();

    /**
     * Cache location scanners.
     */
    private final Map<String, ClassPathLocationScanner> locationScannerCache = new HashMap<String, ClassPathLocationScanner>();

    /**
     * Cache resource names.
     */
    private final Map<ClassPathLocationScanner, Map<URL, Set<String>>> resourceNameCache = new HashMap<ClassPathLocationScanner, Map<URL, Set<String>>>();

    /**
     * Creates a new Classpath scanner.
     *
     * @param classLoader The ClassLoader for loading migrations on the classpath.
     */
    public ClassPathScanner(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public List<Resource> scanForResources(Location path, ResourceFilter predicate) {

        try {
            List<Resource> resources = new ArrayList<Resource>();

            Set<String> resourceNames = findResourceNames(path, predicate);
            for (String resourceName : resourceNames) {
                resources.add(new ClassPathResource(resourceName, classLoader));
                LOG.trace("... found resource: {}", resourceName);
            }

            return resources;

        } catch (IOException e) {
            throw new ClassPathScanException(e);
        }
    }

    @Override
    public List<Class<?>> scanForClasses(Location location, ClassFilter predicate) {

        try {
            List<Class<?>> classes = new ArrayList<Class<?>>();

            Set<String> resourceNames = findResourceNames(location, FilterResource.bySuffix(".class"));

            LOG.debug("scanning for classes at {} found {} resources to check", location, resourceNames.size());
            for (String resourceName : resourceNames) {
                String className = toClassName(resourceName);
                try {
                    Class<?> clazz = classLoader.loadClass(className);
                    if (predicate.isMatch(clazz)) {
                        classes.add(clazz);
                        LOG.trace("... matched class: {} ", className);
                    }
                } catch (NoClassDefFoundError err) {
                    // This happens on class that inherits from an other class which are no longer in the classpath
                    // e.g. "public class MyTestRunner extends BlockJUnit4ClassRunner" and junit was in scope "provided"
                    LOG.debug("... class " + className + " could not be loaded and will be ignored.", err);

                } catch (ClassNotFoundException err) {
                    // This happens on class that inherits from an other class which are no longer in the classpath
                    // e.g. "public class MyTestRunner extends BlockJUnit4ClassRunner" and junit was in scope "provided"
                    LOG.debug("... class " + className + " could not be loaded and will be ignored.", err);
                }
            }

            return classes;

        } catch (IOException e) {
            throw new ClassPathScanException(e);
        }
    }

    /**
     * Converts this resource name to a fully qualified class name.
     *
     * @param resourceName The resource name.
     * @return The class name.
     */
    private String toClassName(String resourceName) {
        String nameWithDots = resourceName.replace("/", ".");
        return nameWithDots.substring(0, (nameWithDots.length() - ".class".length()));
    }

    /**
     * Finds the resources names present at this location and below on the classpath starting with this prefix and
     * ending with this suffix.
     */
    private Set<String> findResourceNames(Location location, ResourceFilter predicate) throws IOException {

        Set<String> resourceNames = new TreeSet<String>();

        List<URL> locationsUrls = getLocationUrlsForPath(location);
        for (URL locationUrl : locationsUrls) {
            LOG.debug("scanning URL: {}", locationUrl.toExternalForm());

            UrlResolver urlResolver = createUrlResolver(locationUrl.getProtocol());
            URL resolvedUrl = urlResolver.toStandardJavaUrl(locationUrl);

            String protocol = resolvedUrl.getProtocol();
            ClassPathLocationScanner classPathLocationScanner = createLocationScanner(protocol);
            if (classPathLocationScanner == null) {
                String scanRoot = Files.toFile(resolvedUrl).getAbsolutePath();
                LOG.warn("Unable to scan location: {} (unsupported protocol: {})", scanRoot, protocol);
            } else {
                Set<String> names = resourceNameCache.get(classPathLocationScanner).get(resolvedUrl);
                if (names == null) {
                    names = classPathLocationScanner.findResourceNames(location.getPath(), resolvedUrl);
                    resourceNameCache.get(classPathLocationScanner).put(resolvedUrl, names);
                }
                resourceNames.addAll(names);
            }
        }

        return filterResourceNames(resourceNames, predicate);
    }

    /**
     * Gets the physical location urls for this logical path on the classpath.
     *
     * @param location The location on the classpath.
     * @return The underlying physical URLs.
     * @throws IOException when the lookup fails.
     */
    private List<URL> getLocationUrlsForPath(Location location) throws IOException {
        if (locationUrlCache.containsKey(location)) {
            return locationUrlCache.get(location);
        }

        LOG.debug("determining location urls for {} using ClassLoader {} ...", location, classLoader);

        List<URL> locationUrls = new ArrayList<URL>();

        if (classLoader.getClass().getName().startsWith("com.ibm")) {
            // WebSphere
            Enumeration<URL> urls = classLoader.getResources(location.toString());
            if (!urls.hasMoreElements()) {
                LOG.warn("Unable to resolve location " + location);
            }
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                locationUrls.add(new URL(URLDecoder.decode(url.toExternalForm(), "UTF-8")));
            }
        } else {
            Enumeration<URL> urls = classLoader.getResources(location.getPath());
            if (!urls.hasMoreElements()) {
                LOG.warn("Unable to resolve location " + location);
            }

            while (urls.hasMoreElements()) {
                locationUrls.add(urls.nextElement());
            }
        }

        locationUrlCache.put(location, locationUrls);

        return locationUrls;
    }

    /**
     * Creates an appropriate URL resolver scanner for this url protocol.
     *
     * @param protocol The protocol of the location url to scan.
     * @return The url resolver for this protocol.
     */
    private UrlResolver createUrlResolver(String protocol) {
        if (new EnvironmentDetection(classLoader).isJBossVFSv2() && protocol.startsWith("vfs")) {
            return new JBossVFSv2UrlResolver();
        }

        return new DefaultUrlResolver();
    }

    /**
     * Creates an appropriate location scanner for this url protocol.
     *
     * @param protocol The protocol of the location url to scan.
     * @return The location scanner or {@code null} if it could not be created.
     */
    private ClassPathLocationScanner createLocationScanner(String protocol) {
        if (locationScannerCache.containsKey(protocol)) {
            return locationScannerCache.get(protocol);
        }

        if ("file".equals(protocol)) {
            FileSystemClassPathLocationScanner locationScanner = new FileSystemClassPathLocationScanner();
            locationScannerCache.put(protocol, locationScanner);
            resourceNameCache.put(locationScanner, new HashMap<URL, Set<String>>());
            return locationScanner;
        }

        if ("jar".equals(protocol)
                || "zip".equals(protocol) //WebLogic
                || "wsjar".equals(protocol) //WebSphere
                ) {
            JarFileClassPathLocationScanner locationScanner = new JarFileClassPathLocationScanner();
            locationScannerCache.put(protocol, locationScanner);
            resourceNameCache.put(locationScanner, new HashMap<URL, Set<String>>());
            return locationScanner;
        }

        EnvironmentDetection featureDetector = new EnvironmentDetection(classLoader);
        if (featureDetector.isJBossVFSv3() && "vfs".equals(protocol)) {
            throw new UnsupportedEnvironmentException("JBoss VFS v3");
        }
        if (featureDetector.isOsgi() && (
                "bundle".equals(protocol) // Felix
                        || "bundleresource".equals(protocol)) //Equinox
                ) {
            throw new UnsupportedEnvironmentException("OSGI");
        }

        return null;
    }

    /**
     * Filters this list of resource names to only include the ones whose filename matches this prefix and this suffix.
     */
    private Set<String> filterResourceNames(Set<String> resourceNames, ResourceFilter predicate) {

        Set<String> filteredResourceNames = new TreeSet<String>();
        for (String resourceName : resourceNames) {
            if (predicate.test(resourceName)) {
                filteredResourceNames.add(resourceName);
            }
        }
        return filteredResourceNames;
    }
}
