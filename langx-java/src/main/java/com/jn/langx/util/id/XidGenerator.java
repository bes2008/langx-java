package com.jn.langx.util.id;

import com.jn.langx.IdGenerator;
import com.jn.langx.annotation.NonNull;

import java.util.Date;

/**
 * @since 4.4.6
 */
public class XidGenerator implements IdGenerator<Object> {
    @Override
    public String get(Object o) {
        if(o instanceof Date){
            return get((Date)o);
        }
        return get();
    }

    private String get(@NonNull Date date){
        return new Xid(date).toString();
    }

    @Override
    public String get() {
        return get(new Date());
    }
}
