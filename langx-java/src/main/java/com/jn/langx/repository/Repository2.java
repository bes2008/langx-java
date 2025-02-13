package com.jn.langx.repository;

import java.util.List;

/**
 * Repository2 接口继承了 Repository 接口，并添加了额外的操作方法。
 * 它为实体的集合操作和查询提供了更丰富的接口。
 *
 * @param <E> 实体类类型
 * @param <ID> 实体的标识符类型
 */
public interface Repository2<E, ID> extends Repository<E, ID> {
    /**
     * 检查指定ID的实体是否存在。
     *
     * @param id 要检查的实体ID
     * @return 如果实体存在则返回true，否则返回false
     */
    boolean has(ID id);

    /**
     * 计算实体的数量。
     *
     * @return 实体的数量
     */
    long count();

    /**
     * 根据多个ID获取实体列表。
     *
     * @param ids 实体ID的集合
     * @return 对应ID的实体列表
     */
    List<E> getByIds(Iterable<ID> ids);

    /**
     * 添加多个实体到存储中。
     *
     * @param entities 要添加的实体集合
     */
    void addAll(Iterable<E> entities);

    /**
     * 更新多个实体的信息。
     *
     * @param entities 要更新的实体集合
     */
    void update(Iterable<E> entities);

    /**
     * 根据多个ID删除实体。
     *
     * @param id 要删除的实体ID集合
     */
    void removeByIds(Iterable<ID> id);

    /**
     * 根据ID获取实体。
     * 此方法继承自Repository接口。
     *
     * @param id 实体的唯一标识符
     * @return 对应ID的实体，如果不存在则返回null
     */
    @Override
    E getById(ID id);

    /**
     * 向存储中添加一个实体。
     * 此方法继承自Repository接口。
     *
     * @param entity 要添加的实体
     */
    @Override
    void add(E entity);

    /**
     * 更新一个实体的信息。
     * 此方法继承自Repository接口。
     *
     * @param entity 要更新的实体
     */
    @Override
    void update(E entity);

    /**
     * 根据ID删除一个实体。
     * 此方法继承自Repository接口。
     *
     * @param id 要删除的实体的ID
     */
    @Override
    void removeById(ID id);
}
