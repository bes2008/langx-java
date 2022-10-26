package com.jn.langx.security.marking;

import com.jn.langx.ConditionTransformer;

public abstract class AbstractMarking<DATA> extends ConditionTransformer<DATA, DATA> {

    public int getOrder() {
        return 0;
    }

    @Override
    public abstract DATA doTransform(DATA input);
}
