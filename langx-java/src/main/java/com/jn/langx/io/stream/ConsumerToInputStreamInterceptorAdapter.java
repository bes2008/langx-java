package com.jn.langx.io.stream;

import com.jn.langx.util.function.Consumer4;
import com.jn.langx.util.function.Function;

import java.io.InputStream;

class ConsumerToInputStreamInterceptorAdapter extends InputStreamInterceptor {

    private Consumer4<InputStream, byte[], Integer, Integer> consumer;

    public ConsumerToInputStreamInterceptorAdapter(Consumer4<InputStream, byte[], Integer, Integer> consumer) {
        this.consumer = consumer;
    }

    @Override
    public boolean beforeRead(InputStream inputStream, byte[] b, int off, int len) {
        return true;
    }

    @Override
    public boolean afterRead(InputStream inputStream, byte[] b, int off, int len) {
        consumer.accept(inputStream, b, off, len);
        return true;
    }

    static Function<Consumer4<InputStream, byte[], Integer, Integer>, InputStreamInterceptor> consumerToInterceptorMapper = new Function<Consumer4<InputStream, byte[], Integer, Integer>, InputStreamInterceptor>() {
        @Override
        public InputStreamInterceptor apply(final Consumer4<InputStream, byte[], Integer, Integer> consumer) {
            return new ConsumerToInputStreamInterceptorAdapter(consumer);
        }
    };

}
