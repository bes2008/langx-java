package com.jn.langx.io.stream;

import com.jn.langx.util.Objs;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Consumer;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class IOStreamPipeline {
    private List<IOStreamInterceptor> interceptors = Collects.emptyArrayList();

    public void addFirst(IOStreamInterceptor interceptor) {
        interceptors.add(0, interceptor);
    }

    public void addLast(IOStreamInterceptor interceptor) {
        interceptors.add(interceptor);
    }

    public void beforeWrite(OutputStream outputStream, final byte[] b, final int off, final int len) {
        if (Objs.isNotEmpty(this.interceptors)) {
            boolean continueExecute = true;
            for (int i = 0; continueExecute && i < interceptors.size(); i++) {
                IOStreamInterceptor interceptor = this.interceptors.get(i);
                continueExecute = interceptor.beforeWrite(outputStream, b, off, len);
            }
        }
    }

    public void afterWrite(OutputStream outputStream, final byte[] b, final int off, final int len) {
        if (Objs.isNotEmpty(this.interceptors)) {
            boolean continueExecute = true;
            for (int i = 0; continueExecute && i < interceptors.size(); i++) {
                IOStreamInterceptor interceptor = this.interceptors.get(i);
                continueExecute = interceptor.afterWrite(outputStream, b, off, len);
            }
        }
    }

    public void beforeRead(final InputStream inputStream, final byte[] b, final int off, final int len) {
        if (Objs.isNotEmpty(this.interceptors)) {
            boolean continueExecute = true;
            for (int i = 0; continueExecute && i < interceptors.size(); i++) {
                IOStreamInterceptor interceptor = this.interceptors.get(i);
                continueExecute = interceptor.beforeRead(inputStream, b, off, len);
            }
        }
    }

    public void afterRead(final InputStream inputStream, final byte[] b, final int off, final int len) {
        if (Objs.isNotEmpty(this.interceptors)) {
            boolean continueExecute = true;
            for (int i = 0; continueExecute && i < interceptors.size(); i++) {
                IOStreamInterceptor interceptor = this.interceptors.get(i);
                continueExecute = interceptor.afterRead(inputStream, b, off, len);
            }
        }
    }

    public static IOStreamPipeline of(IOStreamInterceptor... interceptors){
        return of(Collects.asList(interceptors));
    }

    public static IOStreamPipeline of(List<? extends IOStreamInterceptor> interceptors){
        final IOStreamPipeline pipeline = new IOStreamPipeline();
        Collects.forEach(interceptors, new Consumer<IOStreamInterceptor>(){
            @Override
            public void accept(IOStreamInterceptor interceptor) {
                pipeline.addLast(interceptor);
            }
        });
        return pipeline;
    }
}
