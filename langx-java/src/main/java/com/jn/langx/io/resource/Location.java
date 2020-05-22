package com.jn.langx.io.resource;


import com.jn.langx.util.Preconditions;

/**
 * A starting location to scan from.
 */
public final class Location implements Comparable<Location> {

    /**
     * The prefix part of the location. Can be either classpath: or filesystem:.
     */
    private String prefix;

    /**
     * The path part of the location.
     */
    private String path;

    public Location(String prefix, String path) {
        this.path = path;
        this.prefix = prefix;
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
}

