package com.jn.langx.util.reflect.parameter;

import com.jn.langx.util.function.Supplier;
import com.jn.langx.util.reflect.Parameter;

public interface ParameterSupplier<E,  O extends Parameter<E>> extends Supplier<ParameterMeta, O> {
}
