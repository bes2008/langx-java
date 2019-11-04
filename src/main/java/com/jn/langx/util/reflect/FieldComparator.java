package com.jn.langx.util.reflect;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.comparator.ComparableComparator;
import com.jn.langx.util.comparator.DelegatableComparator;
import com.jn.langx.util.reflect.type.Primitives;

import java.lang.reflect.Field;
import java.util.Comparator;

@SuppressWarnings({"unchecked"})
public class FieldComparator implements DelegatableComparator {
    private Field field;
    private Comparator delegate;

    public FieldComparator(@NonNull Field field,
                           @Nullable Comparator fieldComparator) {
        Preconditions.checkNotNull(field);
        this.field = field;
        if (fieldComparator == null) {
            Class fieldClass = field.getType();
            if (Reflects.isSubClassOrEquals(Comparable.class,Primitives.wrap(fieldClass))) {
                fieldComparator = new ComparableComparator();
            }
        }
        setDelegate(fieldComparator);
    }

    public FieldComparator(@NonNull Class clazz, @NonNull String fieldName, @Nullable Comparator fieldComparator) {
        this(Reflects.getAnyField(clazz, fieldName), fieldComparator);
    }

    @Override
    public int compare(Object o1, Object o2) {
        Object v1 = Reflects.getFieldValue(field, o1, true, false);
        Object v2 = Reflects.getFieldValue(field, o2, true, false);
        return delegate.compare(v1, v2);
    }

    @Override
    public Comparator getDelegate() {
        return delegate;
    }

    @Override
    public void setDelegate(@NonNull Comparator delegate) {
        this.delegate = delegate;
    }
}
