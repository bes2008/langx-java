package com.jn.langx.util.concurrent.promise;

class PromiseExceptions {

    static RuntimeException toRuntimeException(Object rejectedResult) {
        if (rejectedResult instanceof Throwable) {
            if (rejectedResult instanceof RuntimeException) {
                return (RuntimeException) rejectedResult;
            }
            throw new RuntimeException((Throwable) rejectedResult);
        }
        return new ValuedException(rejectedResult);
    }

}
