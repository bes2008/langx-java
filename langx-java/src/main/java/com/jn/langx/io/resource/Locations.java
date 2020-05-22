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

    public static boolean isClasspathLocation(@NonNull Location location){
        Resource resource = newResource(location.getLocation());
        if (resource == null) {
            return false;
        }

        if (resource instanceof ClassPathResource) {
            return true;
        }

        return false;
    }

    public static boolean isUrlLocation(@NonNull Location location){
        Resource resource = newResource(location.getLocation());
        if (resource == null) {
            return false;
        }

        if (resource instanceof UrlResource) {
            return true;
        }

        return false;
    }
}
