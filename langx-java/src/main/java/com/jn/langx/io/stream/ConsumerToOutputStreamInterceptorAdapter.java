package com.jn.langx.io.stream;

import com.jn.langx.util.function.Consumer4;
import com.jn.langx.util.function.Function;

import java.io.OutputStream;

class ConsumerToOutputStreamInterceptorAdapter extends OutputStreamInterceptor {
    private Consumer4<OutputStream, byte[], Integer, Integer> consumer;

    public ConsumerToOutputStreamInterceptorAdapter(Consumer4<OutputStream, byte[], Integer, Integer> consumer) {
        this.consumer = consumer;
    }

    @Override
    public boolean beforeWrite(OutputStream outputStream, byte[] b, int off, int len) {
        return true;
    }

    @Override
    public boolean afterWrite(OutputStream outputStream, byte[] b, int off, int len) {
        consumer.accept(outputStream, b, off, len);
        return true;
    }

    static Function<Consumer4<OutputStream, byte[], Integer, Integer>, OutputStreamInterceptor> consumerToInterceptorMapper = new Function<Consumer4<OutputStream, byte[], Integer, Integer>, OutputStreamInterceptor>() {
        @Override
        public OutputStreamInterceptor apply(final Consumer4<OutputStream, byte[], Integer, Integer> consumer) {
            return new ConsumerToOutputStreamInterceptorAdapter(consumer);
        }
    };
}
