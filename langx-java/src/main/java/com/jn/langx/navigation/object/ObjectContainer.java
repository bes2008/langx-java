package com.jn.langx.navigation.object;

import com.jn.langx.Accessor;
import com.jn.langx.accessor.Accessors;
import com.jn.langx.navigation.Container;
import com.jn.langx.util.Objs;
import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.logging.Loggers;
import com.jn.langx.util.reflect.Reflects;
import org.slf4j.Logger;

import java.util.List;


public class ObjectContainer implements Container {
    private static final Logger logger = Loggers.getLogger(ObjectContainer.class);
    private Object containerObject;
    private String separator;

    public ObjectContainer(Object containerObject) {
        this(containerObject, "/");
    }

    public ObjectContainer(Object containerObject, String separator) {
        this.containerObject = containerObject;
        this.separator = Strings.isEmpty(separator) ? "/" : separator;
    }

    @Override
    public <T> T get(String expression) {
        String[] segments = Strings.split(expression, separator);
        return get(Collects.asList(segments));
    }

    private <T> T get(List<String> segments) {
        Object currentObject = this.containerObject;
        for (int i = 0; currentObject != null && i < segments.size(); i++) {
            String subExpr = segments.get(i);
            Accessor accessor = Accessors.of(currentObject);
            if (accessor != null) {
                currentObject = accessor.get(subExpr);
            } else {
                logger.warn("count'd find a accessor for class: {}, may be it is not a container object", Reflects.getFQNClassName(currentObject.getClass()));
                currentObject = null;
            }
        }
        return (T) currentObject;
    }

    @Override
    public <T> void set(String expression, T value) {
        String[] segments = Strings.split(expression, separator);
        if (Objs.isEmpty(segments)) {
            return;
        }
        String key = segments[segments.length - 1];
        List<String> parentExprSegments = Collects.asList(segments).subList(0, segments.length - 1);

        Object parent = get(parentExprSegments);
        Accessor accessor = Accessors.of(parent);
        if (accessor != null) {
            accessor.set(key, value);
        }
    }
}
