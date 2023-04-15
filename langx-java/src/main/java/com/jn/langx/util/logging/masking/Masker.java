package com.jn.langx.util.logging.masking;

import com.jn.langx.Named;
import com.jn.langx.util.transformer.ConditionTransformer;

public abstract class Masker<DATA> extends ConditionTransformer<DATA, String> implements Named {

    public int getOrder() {
        return 0;
    }

    @Override
    public abstract String doTransform(DATA input);

    @Override
    public abstract String getName();
}
