package com.jn.langx.util.struct;

import com.jn.langx.annotation.NotNull;

/**
 * @param <E> any type
 */
public class NameValuePair<E> {
    @NotNull
    private String name;
    @NotNull
    private E value;

    public NameValuePair() {
    }

    public NameValuePair(String name, E value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public E getValue() {
        return value;
    }

    public void setValue(E value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        NameValuePair<?> that = (NameValuePair<?>) object;
        return name.equals(that.name) &&
                value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return name.hashCode() << 4 + value.hashCode();
    }
}
