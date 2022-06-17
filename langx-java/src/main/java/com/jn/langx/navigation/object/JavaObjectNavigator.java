package com.jn.langx.navigation.object;

import com.jn.langx.Accessor;
import com.jn.langx.accessor.Accessors;
import com.jn.langx.navigation.Navigator;
import com.jn.langx.util.Objs;
import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.logging.Loggers;
import com.jn.langx.util.reflect.Reflects;
import org.slf4j.Logger;

import java.util.List;

public class JavaObjectNavigator implements Navigator<Object> {
    private static final Logger logger = Loggers.getLogger(JavaObjectNavigator.class);
    private String separator;

    public JavaObjectNavigator() {
        this("/");
    }

    public JavaObjectNavigator(String separator) {
        this.separator = Strings.isEmpty(separator) ? "/" : separator;
    }

    @Override
    public <T> T get(Object context, String expression) {
        if (context == null) {
            return null;
        }

        String[] segments = Strings.split(expression, separator);
        return navigate(context, Collects.asList(segments));
    }

    @Override
    public <E> List<E> getList(Object context, String expression) {
        return Pipeline.<E>of(get(context, expression)).asList();
    }

    private <T> T navigate(Object context, List<String> segments) {
        Object currentObject = context;
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
    public <T> void set(Object context, String expression, T value) {
        String[] segments = Strings.split(expression, separator);
        if (Objs.isEmpty(segments)) {
            return;
        }
        String key = segments[segments.length - 1];
        List<String> parentExprSegments = Collects.asList(segments).subList(0, segments.length - 1);

        Object parent = navigate(context, parentExprSegments);
        Accessor accessor = Accessors.of(parent);
        if (accessor != null) {
            accessor.set(key, value);
        }
    }
}
