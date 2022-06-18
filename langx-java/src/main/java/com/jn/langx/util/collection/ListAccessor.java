package com.jn.langx.util.collection;

import com.jn.langx.util.BasedStringAccessor;
import com.jn.langx.util.Objs;

import java.util.List;

public class ListAccessor extends BasedStringAccessor<Integer, List> {
    @Override
    public void remove(Integer index) {
        getTarget().remove(index);
    }

    @Override
    public Object get(Integer index) {
        return getTarget().get(index);
    }

    @Override
    public String getString(Integer index, String defaultValue) {
        Object element = getTarget().get(index);
        if(Objs.isNotNull(element)){
            return element.toString();
        }else{
            return defaultValue;
        }
    }

    @Override
    public void set(Integer index, Object value) {
        getTarget().set(index, value);
    }
}
