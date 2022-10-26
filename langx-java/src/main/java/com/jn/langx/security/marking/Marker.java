package com.jn.langx.security.marking;

import com.jn.langx.util.transformer.ConditionTransformer;

public abstract class Marker<DATA> extends ConditionTransformer<DATA, DATA> {

    public int getOrder() {
        return 0;
    }

    @Override
    public abstract DATA doTransform(DATA input);
}
