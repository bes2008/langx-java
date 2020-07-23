package com.jn.langx.test.security.messagedigest;

import java.security.Provider;

public class XYZProvider extends Provider {
    public XYZProvider(){
        super("XYZ", 1.0, "XYZ Security Provider v1.0");
        put("MessageDigest.XYZ", XYZMessageDigest.class.getName());
    }
}