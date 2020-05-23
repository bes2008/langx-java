package com.jn.langx.io.resource;


import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;
import com.jn.langx.util.EmptyEvalutible;
import com.jn.langx.util.Emptys;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.Strings;

/**
 * A starting location to scan from.
 */
public final class Location implements Comparable<Location>, EmptyEvalutible {

    /**
     * The prefix part of the location. Can be either classpath: or filesystem:.
     */
    @Nullable
    private String prefix;

    /**
     * The path part of the location.
     */
    @NonNull
    private String path;

    private String pathSeparator;

    public String getPathSeparator() {
        return pathSeparator;
    }

    public Location(String prefix, String path, String pathSeparator) {
        this.path = path;
        this.prefix = Strings.getEmptyIfNull(path);
        this.pathSeparator = Strings.useValueIfBlank(pathSeparator, "/");
    }

    public Location(String prefix, String path) {
        this(prefix, path, null);
    }

    /**
     * Return the path part of the location.
     */
    public String getPath() {
        return path;
    }

    /**
     * Return the prefix denoting classpath of filesystem.
     */
    public String getPrefix() {
        return prefix;
    }

    public String getLocation() {
        return prefix + path;
    }

    public int compareTo(Location o) {
        Preconditions.checkNotNull(o);
        return getLocation().compareTo(o.getLocation());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Location location = (Location) o;
        return location.getLocation().equals(this.getLocation());
    }

    @Override
    public int hashCode() {
        return getLocation().hashCode();
    }

    @Override
    public String toString() {
        return getLocation();
    }

    @Override
    public boolean isEmpty() {
        return Emptys.isEmpty(path);
    }

    @Override
    public boolean isNull() {
        return Emptys.isNull(prefix);
    }
}

