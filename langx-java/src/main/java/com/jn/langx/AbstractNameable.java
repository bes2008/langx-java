package com.jn.langx;

import com.jn.langx.text.StringTemplates;
import com.jn.langx.util.struct.CharData;
import com.jn.langx.validation.TextValidatorBuilder;

public abstract class AbstractNameable implements Nameable {
    protected String name;

    @Override
    public void setName(String name) {
        // 只能是大小写字母或者数字
        if(!TextValidatorBuilder.newBuilder().required().validChars(CharData.ALPHABET_DIGITS).build().validate(name).isValid()) {
            throw new IllegalArgumentException(StringTemplates.formatWithPlaceholder("name must be only any letter in [0-9a-zA-Z], your name is: {}", name));
        }
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
