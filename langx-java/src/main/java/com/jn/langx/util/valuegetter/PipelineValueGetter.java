package com.jn.langx.util.valuegetter;

import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Consumer;
import com.jn.langx.util.struct.Holder;

import java.util.List;

public class PipelineValueGetter implements ValueGetter {
    private List<ValueGetter> pipeline = Collects.newArrayList();

    public void addValueGetter(ValueGetter valueGetter){
        pipeline.add(valueGetter);
    }

    public Object get(Object input) {
        final Holder<Object> resultHolder = new Holder<Object>(input);
        Collects.forEach(pipeline, new Consumer<ValueGetter>() {
            @Override
            public void accept(ValueGetter valueGetter) {
                resultHolder.set(valueGetter.get(resultHolder.get()));
            }
        });
        return resultHolder.get();
    }
}
