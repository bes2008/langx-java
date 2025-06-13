package com.jn.langx.navigation.object;

import com.jn.langx.Accessor;
import com.jn.langx.accessor.Accessors;
import com.jn.langx.navigation.Navigator;
import com.jn.langx.navigation.Navigators;
import com.jn.langx.util.Objs;
import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.logging.Loggers;
import com.jn.langx.util.reflect.Reflects;
import com.jn.langx.util.reflect.type.Primitives;
import org.slf4j.Logger;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @since 4.6.10
 */
public class ObjectNavigator implements Navigator<Object> {
    private static final Logger logger = Loggers.getLogger(ObjectNavigator.class);
    private String prefix;
    private String suffix;

    public ObjectNavigator() {
        this("/");
    }

    public ObjectNavigator(String separator) {
        this(null, separator);
    }


    public ObjectNavigator(String prefix, String suffix) {
        this.prefix = prefix;
        this.suffix = Strings.isEmpty(suffix) ? "/" : suffix;
    }

    public <T> Accessor<String, T> getAccessor(Object context, String pathExpression) {
        T t = get(context, pathExpression);
        if (t == null) {
            return null;
        }
        return Accessors.of(t);
    }

    @Override
    public <T> Class<T> getType(Object context, String pathExpression) {
        T t = get(context, pathExpression);
        if (t == null) {
            String parentPath = getParentPath(pathExpression);
            if (Strings.isNotEmpty(parentPath)) {
                Object parent = get(context, pathExpression);

                if (Primitives.isPrimitiveOrPrimitiveWrapperType(parent.getClass())) {
                    return null;
                }
                String leaf = getLeaf(pathExpression);
                Field field = Reflects.getAnyField(parent.getClass(), leaf);
                if (field == null) {
                    return null;
                }
                return (Class<T>) field.getType();
            } else {
                return null;
            }
        } else {
            return (Class<T>) t.getClass();
        }
    }

    @Override
    public <T> T get(Object context, String pathExpression) {
        if (context == null) {
            return null;
        }

        String[] segments = Navigators.getPathSegments(pathExpression, this.prefix, this.suffix);
        if (Collects.contains(segments, "declaredClass")) {
            throw new IllegalArgumentException("illegal path expression, 'declaredClass' is not allowed");
        }
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
                logger.warn("count find a accessor for class: {}, may be it is not a container object", Reflects.getFQNClassName(currentObject.getClass()));
                currentObject = null;
                break;
            }
        }
        return (T) currentObject;
    }

    @Override
    public <T> void set(Object context, String expression, T value) {
        String[] segments = Navigators.getPathSegments(expression, this.prefix, this.suffix);
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

    @Override
    public String getParentPath(String pathExpression) {
        return Navigators.getParentPath(pathExpression, suffix);
    }

    @Override
    public String getLeaf(String pathExpression) {
        return Navigators.getLeaf(pathExpression, suffix);
    }
}
