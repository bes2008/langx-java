package com.jn.langx.io.resource;

import java.util.List;

public interface ResourcePatternLoader extends ResourceLoader {
    List<Resource> getResources(String locationPattern);
}
