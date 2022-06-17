package com.jn.langx.query.object;

import com.jn.langx.Accessor;
import com.jn.langx.query.Container;
import com.jn.langx.util.Strings;


public class ObjectContainer implements Container {
    private Accessor<String, ?> root;
    private String separator = "/";

    @Override
    public <T> T select(String expression) {
        String[] segments = Strings.split(expression, separator);
        Accessor<String, ?> node = root;
        for (int i = 0; i < segments.length; i++) {
            String segment = segments[i];
            if (node == null) {
                return null;
            }
            Object o = node.get(segment);
            if(o == null){
                node = null;
            }else{

            }
        }
        return null;
    }
}
