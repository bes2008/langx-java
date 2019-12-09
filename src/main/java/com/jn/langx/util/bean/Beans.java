package com.jn.langx.util.bean;

import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.function.Function;

import java.util.List;
import java.util.Set;

public class Beans {
    public static <BEAN, CI extends Iterable<BEAN>, O> List<O> getFieldList(CI collection, Function<BEAN, O> function) {
        return Pipeline.of(collection).map(function).asList();
    }

    public static <BEAN, CI extends Iterable<BEAN>, O> Set<O> getFieldSet(CI collection, Function<BEAN, O> function) {
        return Pipeline.of(collection).map(function).collect(Collects.<O>toHashSet(true));
    }
}
