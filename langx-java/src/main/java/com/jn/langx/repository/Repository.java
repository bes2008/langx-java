package com.jn.langx.repository;

/**
 * Repository接口定义了对实体进行基本操作的标准方法
 * 它是一个泛型接口，允许在不同的上下文中对不同类型的实体进行操作
 *
 * @param <E> 实体的类型，允许操作任何类型的实体
 * @param <ID> 实体标识符的类型，支持使用任何类型作为实体的标识符
 */
public interface Repository<E, ID> {

    /**
     * 根据实体的ID获取实体
     *
     * @param id 实体的唯一标识符
     * @return 返回找到的实体，如果找不到则返回null
     */
    E getById(ID id);

    /**
     * 向数据存储中添加一个新的实体
     *
     * @param entity 要添加的新实体
     */
    void add(E entity);

    /**
     * 更新数据存储中的一个实体
     *
     * @param entity 要更新的实体，该实体应包含更新后的信息
     */
    void update(E entity);

    /**
     * 根据实体的ID从数据存储中移除实体
     *
     * @param id 要移除的实体的唯一标识符
     */
    void removeById(ID id);

}
