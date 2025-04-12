package com.jn.langx.validation;

import com.jn.langx.util.collection.Maps;
import com.jn.langx.validation.rule.ValidationResult;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * 只提供JavaBean中原生字段、String、Enum字段的校验
 *
 * @since 5.5.0
 */
public class JavaBeanValidator implements Validator<Object> {
    private Map<Field, Validator> fieldValidators = Maps.newHashMap();
    private ValidateMode validateMode = ValidateMode.QUICK_FAIL;

    @Override
    public void setValidateMode(ValidateMode mode) {
        this.validateMode = mode;
    }

    @Override
    public ValidateMode getValidateMode() {
        return this.validateMode;
    }

    @Override
    public ValidationResult validate(Object javaBean) {
        return null;
    }
}
