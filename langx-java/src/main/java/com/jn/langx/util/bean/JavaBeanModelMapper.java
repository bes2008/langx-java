package com.jn.langx.util.bean;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.function.Function;
import com.jn.langx.util.reflect.Reflects;

import java.util.List;

/**
 * @param <Target>
 * @since 4.3.7
 */
@SuppressWarnings("rawtypes")
public class JavaBeanModelMapper<Target> implements ModelMapper<Object, Target> {
    protected Class<Target> targetClass;
    private List<String> ignoreFields;

    public void setTargetClass(Class<Target> targetClass) {
        this.targetClass = targetClass;
    }

    public void setIgnoreFields(List<String> ignoreFields) {
        this.ignoreFields = ignoreFields;
    }

    @Override
    public Target map(Object source) {
        Target target = Reflects.newInstance(targetClass);
        Beans.copyProperties(source, target, Collects.toArray(ignoreFields, String[].class));
        return target;
    }

    public static <Target> Target map(@NonNull Object source, @NonNull Class targetClass, String... ignoredFields) {
        Preconditions.checkNotNullArgument(source, "source");
        Preconditions.checkNotNullArgument(targetClass, "targetClass");
        JavaBeanModelMapper<Target> modelMapper = new JavaBeanModelMapper<Target>();
        modelMapper.setTargetClass(targetClass);
        modelMapper.setIgnoreFields(Collects.asList(ignoredFields));
        Target target = modelMapper.map(source);
        return target;
    }

    public static <Source, Target> List<Target> mapBeans(List<Source> sources, @NonNull final Class targetClass, final String... ignoredFields) {
        return Pipeline.of(sources)
                .map(new Function<Source, Target>() {
                    @Override
                    public Target apply(Source source) {
                        return map(source, targetClass, ignoredFields);
                    }
                }).asList();
    }
}
