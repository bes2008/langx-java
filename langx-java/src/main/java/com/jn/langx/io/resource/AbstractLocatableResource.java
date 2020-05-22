package com.jn.langx.io.resource;

public abstract class AbstractLocatableResource<T> extends AbstractResource<T> implements Locatable, Urlable {
    private Location location;

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public String getPrefix() {
        return location.getPrefix();
    }

    public void setLocation(String prefix, String path){
        this.location = new Location(prefix, path);
    }

    @Override
    public String getPath() {
        return location.getPath();
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return getLocation().toString();
    }
}
