package com.jn.langx.util.net.uri.component;

class FullPathComponentBuilder implements PathComponentBuilder {

    private final StringBuilder path = new StringBuilder();

    public void append(String path) {
        this.path.append(path);
    }

    @Override
    public PathComponent build() {
        if (this.path.length() == 0) {
            return null;
        }
        String sanitized = getSanitizedPath(this.path);
        return new FullPathComponent(sanitized);
    }

    private static String getSanitizedPath(final StringBuilder path) {
        int index = path.indexOf("//");
        if (index >= 0) {
            StringBuilder sanitized = new StringBuilder(path);
            while (index != -1) {
                sanitized.deleteCharAt(index);
                index = sanitized.indexOf("//", index);
            }
            return sanitized.toString();
        }
        return path.toString();
    }

    public void removeTrailingSlash() {
        int index = this.path.length() - 1;
        if (this.path.charAt(index) == '/') {
            this.path.deleteCharAt(index);
        }
    }

    @Override
    public FullPathComponentBuilder cloneBuilder() {
        FullPathComponentBuilder builder = new FullPathComponentBuilder();
        builder.append(this.path.toString());
        return builder;
    }
}

