package com.jn.langx.util.collection.iter;

import com.jn.langx.util.Objects;
import com.jn.langx.util.collection.Arrs;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;

public class Iterables {
    public boolean isIterable(Object obj){
        if(Objects.isNull(obj)){
            return false;
        }
        if(Arrs.isArray(obj)){
            return true;
        }
        if(obj instanceof Iterable){
            return true;
        }
        if(obj instanceof Map){
            return true;
        }
        if(obj instanceof Iterator){
            return true;
        }
        if(obj instanceof Enumeration){
            return true;
        }
        return false;
    }
}
