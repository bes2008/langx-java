package com.jn.langx.io.resource;

import com.jn.langx.annotation.NonNull;

public class Locations {
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

    public static Resource newResource(String location) {
        return new DefaultResourceLoader().loadResource(location);
    }

    public static boolean isFileLocation(@NonNull Location location) {
        Resource resource = newResource(location.getLocation());
        if (resource == null) {
            return false;
        }

        if (resource instanceof FileResource) {
            return true;
        }

        return false;
    }

    public static boolean isClasspathLocation(@NonNull Location location) {
        Resource resource = newResource(location.getLocation());
        if (resource == null) {
            return false;
        }

        if (resource instanceof ClassPathResource) {
            return true;
        }

        return false;
    }

    public static boolean isUrlLocation(@NonNull Location location) {
        Resource resource = newResource(location.getLocation());
        if (resource == null) {
            return false;
        }

        if (resource instanceof UrlResource) {
            return true;
        }

        return false;
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

    public static Location newLocation(Location root, String namespace) {
        if (root == null) {
            return new Location(null, namespace);
        }
        if (namespace == null) {
            return root;
        }
        return new Location(root.getPrefix(), root.getPath() + root.getPathSeparator() + namespace);
    }
}
