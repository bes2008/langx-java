package com.jn.langx.util.enums.base;

import com.jn.langx.Named;

/**
 * 常用枚举接口，继承自 Named 接口
 * 该接口用于定义系统中通用的枚举类型，提供枚举值的代码和显示文本
 */
public interface CommonEnum extends Named {
    /**
     * 获取枚举值的代码
     *
     * @return 枚举值的代码
     */
    int getCode();

    /**
     * 获取枚举值的显示文本
     *
     * @return 枚举值的显示文本
     */
    String getDisplayText();
}
