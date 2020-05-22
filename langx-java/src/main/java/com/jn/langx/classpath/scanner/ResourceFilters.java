package com.jn.langx.classpath.scanner;

/**
 * Some common resource matching predicates.
 */
public class ResourceFilters {

    /**
     * Return a resource matcher that matches by both prefix and suffix.
     */
    public static ResourceFilter byPrefixSuffix(String prefix, String suffix) {
        return new ByPrefixSuffix(prefix, suffix);
    }

    /**
     * Return a resource matcher that matches by suffix.
     */
    public static ResourceFilter bySuffix(String suffix) {
        return new BySuffix(suffix);
    }

    /**
     * Return a resource matcher that matches by prefix.
     */
    public static ResourceFilter byPrefix(String prefix) {
        return new ByPrefix(prefix);
    }

    private ResourceFilters() {
    }

    private static class ByPrefixSuffix implements ResourceFilter {

        private final String prefix;
        private final String suffix;

        ByPrefixSuffix(String prefix, String suffix) {
            this.prefix = prefix;
            this.suffix = suffix;
        }

        @Override
        public boolean test(String resourceName) {
            // resources always use '/' as separator
            String fileName = resourceName.substring(resourceName.lastIndexOf('/') + 1);
            return fileName.startsWith(prefix) && fileName.endsWith(suffix);
        }
    }

    private static class BySuffix implements ResourceFilter {

        private final String suffix;

        BySuffix(String suffix) {
            this.suffix = suffix;
        }

        @Override
        public boolean test(String resourceName) {
            return resourceName.endsWith(suffix);
        }
    }

    private static class ByPrefix implements ResourceFilter {

        private final String prefix;

        ByPrefix(String prefix) {
            this.prefix = prefix;
        }

        @Override
        public boolean test(String resourceName) {
            return resourceName.startsWith(prefix);
        }
    }
}
