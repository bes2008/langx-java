package com.jn.langx.util.collect;

import com.jn.langx.util.Emptys;

public class Arrs {
    public static boolean isArray(Object o){
        return Emptys.isNull(o) ? false : o.getClass().isArray();
    }

    public static String[] wrapAsArray(String string){
        if(Emptys.isNull(string)){
            return new String[0];
        }
        return new String[]{string};
    }

    public static Number[] wrapAsArray(Number number){
        if(Emptys.isNull(number)){
            return new Number[0];
        }
        return new Number[]{number};
    }

    public static Boolean[] wrapAsArray(Boolean bool){
        if(Emptys.isNull(bool)){
            return new Boolean[0];
        }
        return new Boolean[]{bool};
    }

    public static Object[] wrapAsArray(Object o){
        if(Emptys.isNull(o)){
            return new Object[0];
        }
        return new Object[]{o};
    }


}
