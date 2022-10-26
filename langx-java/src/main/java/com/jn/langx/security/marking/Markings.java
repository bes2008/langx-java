package com.jn.langx.security.marking;

public class Markings {
    public static String marking(Marker<String> marker, String text) {
        String ret = marker.doTransform(text);
        return ret;
    }

    public static Object marking(Marker marker, Object obj) {
        Object ret = marker.doTransform(obj);
        return ret;
    }
}
