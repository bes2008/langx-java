package com.jn.langx.accessor;

import com.jn.langx.util.BasedStringAccessor;
import com.jn.langx.util.Numbers;
import com.jn.langx.util.Objs;
import com.jn.langx.util.collection.Pipeline;

import java.util.Collection;
import java.util.List;

/**
 * @since 4.6.10
 */
public class StringKeyCollectionAccessor extends BasedStringAccessor<String, Collection> {

    @Override
    public void setTarget(Collection target) {
        super.setTarget(Pipeline.of(target).asList());
    }

    @Override
    public void remove(String key) {
        getTarget().remove(toIndex(key));
    }

    private int toIndex(String key){
        return Numbers.createInteger(key);
    }

    @Override
    public Object get(String key) {
        return ((List)getTarget()).get(toIndex(key));
    }

    @Override
    public String getString(String key, String defaultValue) {
        Object element = ((List)getTarget()).get(toIndex(key));
        if(Objs.isNotNull(element)){
            return element.toString();
        }else{
            return defaultValue;
        }
    }

    @Override
    public void set(String key, Object value) {
        ((List)getTarget()).set(toIndex(key),value);
    }
}
