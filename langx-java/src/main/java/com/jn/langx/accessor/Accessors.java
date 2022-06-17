package com.jn.langx.accessor;

import com.jn.langx.Accessor;
import com.jn.langx.annotation.NonNull;
import com.jn.langx.util.Preconditions;

public class Accessors {

    private Accessors(){
    }

    public static <T> Accessor<String,T> of(@NonNull Object object){
        Preconditions.checkNotNull(object);
        return null;
    }
}
