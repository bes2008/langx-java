package com.jn.langx.io.resource;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.util.net.URLs;

public class Locations {
    private Locations(){

    }
    public static Location parseLocation(String location) {
        Resource resource = new DefaultResourceLoader().loadResource(location);
        if (resource == null) {
            return null;
        }
        if (resource instanceof AbstractLocatableResource) {
            return ((AbstractLocatableResource) resource).getLocation();
        }
        return null;
    }

    public static <V extends Resource> V newResource(String location) {
        return new DefaultResourceLoader().loadResource(location);
    }

    public static boolean isFileLocation(@NonNull Location location) {
        return FileResource.PREFIX.equals(location.getPrefix());
    }

    public static boolean isClasspathLocation(@NonNull Location location) {
        return ClassPathResource.PREFIX.equals(location.getPrefix());
    }

    public static boolean isUrlLocation(@NonNull Location location) {
        Resource resource = newResource(location.getLocation());
        if (resource == null) {
            return false;
        }

        return resource instanceof UrlResource;
    }

    public static boolean isJarLocation(Location location) {
        String l = location.getLocation();
        return l.startsWith(URLs.URL_PREFIX_JAR);
    }

    public static boolean isJarFileLocation(Location location) {
        String l = location.getLocation();
        return l.startsWith(URLs.URL_PREFIX_JAR_FILE);
    }

    public static boolean isJarEntryLocation(Location location) {
        String l = location.getLocation();
        return l.startsWith(URLs.URL_PREFIX_JAR_FILE) && l.contains(URLs.JAR_URL_SEPARATOR) && !l.endsWith(URLs.JAR_URL_SEPARATOR);
    }

    /**
     * 获取 root 下指定的 location 代表的资源的相对路径
     *
     * @param location the full path
     * @param root     the root
     * @return the relative path
     */
    public static String getRelativePath(Location root, Location location) {
        if (root == null && location == null) {
            throw new IllegalArgumentException("At least one of root and location cannot be empty");
        }
        String rootLocation = root == null ? "" : root.getLocation();
        String resourceLocation = location == null ? "" : location.getLocation();
        if (resourceLocation.startsWith(rootLocation)) {
            return resourceLocation.substring(rootLocation.length());
        }
        return null;
    }

    public static Location newLocation(Location root, String relativePath) {
        if (root == null) {
            return new Location(null, relativePath);
        }
        if (relativePath == null) {
            return root;
        }
        return new Location(root.getPrefix(), root.getPath() + root.getPathSeparator() + relativePath);
    }
}
