package com.jn.langx.propertyset;

import com.jn.langx.AbstractNameable;
import com.jn.langx.util.Emptys;
import com.jn.langx.util.Objs;
/**
 * @since 5.3.8
 */
public abstract class AbstractPropertySet<SRC> extends AbstractNameable implements PropertySet<SRC> {
    private SRC source;
    public AbstractPropertySet(String name){
        this(name, (SRC) Emptys.EMPTY_OBJECTS);
    }
    public AbstractPropertySet(String name, SRC source){
        this.setName(name);
        this.source=source;
    }

    @Override
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
        if(!Objs.equals(this.getName(), that.getName())){
            return false;
        }

        return true;
    }

    public int hashCode(){
        return Objs.hash(this.name);
    }

    public boolean containsProperty(String key){
        return getProperty(key) != null;
    }
}
