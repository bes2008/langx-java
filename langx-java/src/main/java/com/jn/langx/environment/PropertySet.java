package com.jn.langx.environment;

import com.jn.langx.util.Emptys;
import com.jn.langx.util.Objs;

public abstract class PropertySet<SRC> {
    private String name;
    private SRC source;
    public PropertySet(String name){
        this(name, (SRC)Emptys.EMPTY_OBJECTS);
    }
    public PropertySet(String name, SRC source){
        this.name=name;
        this.source=source;
    }

    public String getName() {
        return name;
    }

    public SRC getSource() {
        return source;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj==this){
            return true;
        }
        if(!(obj instanceof PropertySet)){
            return false;
        }
        PropertySet that=(PropertySet)obj;
        if(!Objs.equals(this.name, that.name)){
            return false;
        }

        return true;
    }

    public int hashCode(){
        return Objs.hash(this.name);
    }

    public abstract Object getProperty(String key);

    public boolean hasProperty(String key){
        return getProperty(key)!=null;
    }
}
