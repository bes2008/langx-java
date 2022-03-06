package com.jn.langx.beans;

import com.jn.langx.util.Objs;

import java.beans.PropertyDescriptor;

public class PropertyDescriptors {
    /**
     * Compare the given {@code PropertyDescriptors} and return {@code true} if
     * they are equivalent, i.e. their read method, write method, property type,
     * property editor and flags are equivalent.
     * @see PropertyDescriptor#equals(Object)
     */
    public static boolean equals(PropertyDescriptor pd, PropertyDescriptor otherPd) {
        return (Objs.deepEquals(pd.getReadMethod(), otherPd.getReadMethod()) &&
                Objs.deepEquals(pd.getWriteMethod(), otherPd.getWriteMethod()) &&
                Objs.deepEquals(pd.getPropertyType(), otherPd.getPropertyType()) &&
                Objs.deepEquals(pd.getPropertyEditorClass(), otherPd.getPropertyEditorClass()) &&
                pd.isBound() == otherPd.isBound() && pd.isConstrained() == otherPd.isConstrained());
    }

}
