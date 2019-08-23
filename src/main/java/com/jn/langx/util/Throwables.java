package com.jn.langx.util;

import com.jn.langx.util.io.IOs;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

public class Throwables {
    public static String getStackTraceAsString(Throwable throwable) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        try {
            throwable.printStackTrace(printWriter);
            return stringWriter.toString();
        } finally {
            IOs.close(printWriter);
        }
    }


    public static Throwable throwIfError(Throwable ex){
        if(ex instanceof Error){
            throw (Error)ex;
        }
        return ex;
    }

    public static Throwable throwIfRuntimeException(Throwable ex){
        if(ex instanceof RuntimeException){
            throw (RuntimeException)ex;
        }
        return ex;
    }

    public static Throwable throwIfIOException(Throwable ex) throws IOException{
        if(ex instanceof IOException){
            throw (IOException)ex;
        }
        return ex;
    }

    public static void throwAsRuntimeException(Throwable ex){
        if(ex instanceof RuntimeException){
            throw (RuntimeException)ex;
        }
        throw new RuntimeException(ex);
    }

    public static Throwable getRootCause(Throwable ex){
        while (ex .getCause()!=null){
            ex = ex.getCause();
        }
        return ex;
    }

    /**
     * step1 : get root cause
     * step2 : throwIfIOException(root cause)
     * @param ex
     * @return
     */
    public static Throwable throwRootCauseIfIOException(Throwable ex) throws IOException{
        return throwIfIOException(getRootCause(ex));
    }

    public static void log(Throwable ex) {
        // it;
    }
}
