package com.jn.langx.util.io;

import com.jn.langx.util.Strings;

public class Consoles {
    public static void println(Object ... args){
        System.out.println(Strings.join("\t",args));
    }
}
