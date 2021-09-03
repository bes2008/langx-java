package com.jn.langx.io.resource;

import com.jn.langx.Provider;

public interface ResourceLocationProvider<ID> extends Provider<ID, Location> {
    @Override
    Location get(ID resourceId);
}
