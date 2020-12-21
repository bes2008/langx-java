package com.jn.langx.util.collection.multivalue;

import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Supplier;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

public class MultiValueMapAdapter<K, V> extends CommonMultiValueMap<K, V> implements Serializable {


    public MultiValueMapAdapter(Map<K, Collection<V>> map) {
        super(map, new Supplier<K, Collection<V>>() {
            @Override
            public Collection<V> get(K input) {
                return Collects.emptyArrayList();
            }
        });
    }


}