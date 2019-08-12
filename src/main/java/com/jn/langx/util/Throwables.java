package com.jn.langx.util;

import java.io.PrintWriter;
import java.io.StringWriter;

public class Throwables {
    public static String getStackTraceAsString(Throwable throwable){
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        try {
            throwable.printStackTrace(printWriter);
            return stringWriter.toString();
        }finally {
            IOs.close(printWriter);
        }
    }

    public static void log(Throwable ex){
        // it;
    }
}
