package com.jn.langx.util.struct;

import com.jn.langx.Transformer;
import com.jn.langx.text.StringTemplates;
import com.jn.langx.util.Emptys;
import com.jn.langx.util.Objs;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.collection.multivalue.LinkedMultiValueMap;
import com.jn.langx.util.collection.multivalue.MultiValueMap;
import com.jn.langx.util.hash.HashCodeBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Entry<K, V> extends Pair<K, V> {
    public Entry(K key) {
        super(key, null);
    }

    public Entry(K key, V value) {
        super(key, value);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Entry)) {
            return false;
        }
        @SuppressWarnings("rawtypes")
        Entry that = (Entry) obj;
        if (!Objs.equals(getKey(), that.getKey())) {
            return false;
        }
        return Objs.equals(getValue(), that.getValue());
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().with(getKey()).with(getValue()).build();
    }

    @Override
    public String toString() {
        return StringTemplates.formatWithBean("{key: ${key}, value: ${value}}", this);
    }

    public static Entry<String, String> newEntry(String keyValue, String spec) throws IllegalArgumentException {
        return newEntry(keyValue, spec, null);
    }

    public static Entry<String, String> newEntry(String keyValue, String spec, Transformer<String, String> valueTransformer) throws IllegalArgumentException {
        Preconditions.checkArgument(Emptys.isNotEmpty(spec), "argument 'spec' is null .");
        Preconditions.checkArgument(Emptys.isNotEmpty(keyValue), "argument 'keyValue' is null .");
        int index = keyValue.indexOf(spec);
        String key;
        String value;
        if (index == -1) {
            key = keyValue.trim();
            value = "";
        } else {
            key = keyValue.substring(0, index);
            value = keyValue.substring(index + spec.length()).trim();
        }
        if (valueTransformer != null) {
            value = valueTransformer.transform(value);
        }
        return new Entry<String, String>(key, value);
    }

    public static Map<String, String> getMap(String str, String keyValueSpec, String entrySpec) {
        Map<String, String> map = new HashMap<String, String>();
        if (Emptys.isEmpty(str)) {
            return map;
        }
        String[] entryArray = str.split(entrySpec);
        Entry<String, String> entry;
        for (String keyValue : entryArray) {
            try {
                entry = Entry.newEntry(keyValue, keyValueSpec);
            } catch (IllegalArgumentException ex) {
                entry = null;
            }
            if (entry != null) {
                map.put(entry.getKey(), entry.getValue());
            }
        }

        return map;
    }

    public static MultiValueMap<String, String> getMultiValueMap(String str, String keyValueSpec, String entrySpec) {
        return getMultiValueMap(str, keyValueSpec, entrySpec, null);
    }

    public static MultiValueMap<String, String> getMultiValueMap(String str, String keyValueSpec, String entrySpec, Transformer<String, String> valueTransformer) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        if (Emptys.isEmpty(str)) {
            return map;
        }
        String[] entryArray = str.split(entrySpec);
        Entry<String, String> entry;
        for (String keyValue : entryArray) {
            try {
                entry = Entry.newEntry(keyValue, keyValueSpec);
            } catch (IllegalArgumentException ex) {
                entry = null;
            }
            if (entry != null) {
                map.add(entry.getKey(), entry.getValue());
            }
        }

        return map;
    }

    public static List<Map<String, String>> getMapList(String src, String keyValueSpec, String entrySpec, String listSpecFlag) {
        List<String> strList = new ArrayList<String>();
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        if (Emptys.isEmpty(listSpecFlag)) {
            strList.add(src);
        } else {
            int index = src.indexOf(listSpecFlag);
            if (index == -1) {
                return list;
            }

            int nextIndex;
            while ((nextIndex = src.indexOf(listSpecFlag, index + listSpecFlag.length())) != -1) {
                strList.add(src.substring(index, nextIndex));
                index = nextIndex;
            }
            strList.add(src.substring(index));
        }

        for (String str : strList) {
            Map<String, String> map = getMap(str, keyValueSpec, entrySpec);
            list.add(map);
        }

        return list;
    }
}