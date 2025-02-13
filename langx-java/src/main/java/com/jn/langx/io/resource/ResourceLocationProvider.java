package com.jn.langx.io.resource;

import com.jn.langx.Provider;

/**
 * ResourceLocationProvider接口扩展了Provider接口，用于根据资源ID获取资源的位置信息
 * 这个接口主要用来标准化资源位置的获取方式，使得可以通过资源ID以一致的方式获取到资源的位置
 *
 * @param <ID> 资源的唯一标识符类型，例如字符串、数字等
 */
public interface ResourceLocationProvider<ID> extends Provider<ID, Location> {
    /**
     * 根据资源ID获取资源的位置信息
     *
     * @param resourceId 资源的唯一标识符，用于定位特定的资源
     * @return Location对象，表示资源的位置信息如果找不到对应的资源，则返回null
     */
    @Override
    Location get(ID resourceId);
}
