package com.jn.langx.plugin;

import com.jn.langx.Named;
import com.jn.langx.Ordered;
import com.jn.langx.lifecycle.Destroyable;
import com.jn.langx.lifecycle.Initializable;

/**
 * 插件接口定义，适用于各种可插拔组件
 * 该接口扩展了Initializable、Destroyable、Named和Ordered接口，以支持初始化、销毁、命名和排序功能
 *
 * @param <E> 插件所关联的实体类型，例如可以是特定的环境或应用程序实例
 */
public interface Plugin<E> extends Initializable, Destroyable, Named, Ordered {
    /**
     * 判断插件是否适用于给定的实体
     *
     * @param e 实体对象，插件将针对其进行适用性检查
     * @return 如果插件适用于该实体，则返回true；否则返回false
     */
    boolean availableFor(E e);
}
