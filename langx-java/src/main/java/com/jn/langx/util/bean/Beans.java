package com.jn.langx.util.bean;

import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.function.Function;
import com.jn.langx.util.function.Functions;
import com.jn.langx.util.function.Predicate;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class Beans {
    public static <BEAN, CI extends Iterable<BEAN>, O> List<O> getFieldList(CI beans, Function<BEAN, O> fieldExtractor) {
        return Pipeline.of(beans).map(fieldExtractor).asList();
    }

    public static <BEAN, CI extends Iterable<BEAN>, O> List<O> getFieldList(CI beans, Predicate<BEAN> predicate, Function<BEAN, O> fieldExtractor) {
        return Pipeline.of(beans).filter(predicate).map(fieldExtractor).asList();
    }

    public static <BEAN, CI extends Iterable<BEAN>, O> Set<O> getFieldSet(CI collection, Function<BEAN, O> function) {
        return Pipeline.of(collection).map(function).collect(Collects.<O>toHashSet(true));
    }

    public static <KEY, BEAN> Map<KEY, BEAN> asHashMap(Iterable<BEAN> collection, Function<BEAN, KEY> keySupplier) {
        return Collects.collect(collection, Collects.toHashMap(keySupplier, Functions.<BEAN>noopFunction(), false));
    }

    public static <KEY, BEAN> Map<KEY, BEAN> asLinkedHashMap(Iterable<BEAN> collection, Function<BEAN, KEY> keySupplier) {
        return Collects.collect(collection, Collects.toHashMap(keySupplier, Functions.<BEAN>noopFunction(), true));
    }

    public static <KEY, BEAN> Map<KEY, BEAN> asTreeMap(Iterable<BEAN> collection, Function<BEAN, KEY> keySupplier) {
        return Collects.collect(collection, Collects.toHashMap(keySupplier, Functions.<BEAN>noopFunction(), true));
    }
}
