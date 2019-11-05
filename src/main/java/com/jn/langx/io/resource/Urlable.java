package com.jn.langx.io.resource;

import java.io.IOException;
import java.net.URL;

public interface Urlable {
    URL getUrl() throws IOException;
}
