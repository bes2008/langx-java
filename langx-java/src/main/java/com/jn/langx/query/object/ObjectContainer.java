package com.jn.langx.query.object;

import com.jn.langx.Accessor;
import com.jn.langx.accessor.Accessors;
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
            if (node == null) {
               break;
            }

            String subExpr = segments[i];
            Object o = node.get(subExpr);
            if(o == null){
                node = null;
            }else{
                node = Accessors.of(o);
            }
        }
        return null;
    }
}
