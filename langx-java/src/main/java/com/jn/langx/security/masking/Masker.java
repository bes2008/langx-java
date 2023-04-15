package com.jn.langx.security.masking;

import com.jn.langx.Named;
import com.jn.langx.util.transformer.ConditionTransformer;

/**
 * @since 5.2.0
 */
public abstract class Masker<DATA> extends ConditionTransformer<DATA, String> implements Named {

    public int getOrder() {
        return 0;
    }

    @Override
    public abstract String doTransform(DATA input);

    @Override
    public abstract String getName();
}
