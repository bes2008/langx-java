package com.jn.langx.util.collection.tree;

import com.jn.langx.util.function.Consumer2;

import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
/**
 * Tree接口定义了树形数据结构的基本操作
 * 它允许用户添加、移除和获取树节点，以及对树节点进行排序和遍历
 * 该接口继承自Serializable，以支持对象的序列化
 */
public interface Tree extends Serializable {
    /**
     * 向树中添加一个节点
     *
     * @param paramTreeNode 要添加的树节点
     */
    void addNode(TreeNode paramTreeNode);

    /**
     * 在指定父节点下添加一个树节点
     *
     * @param paramString 父节点的标识符
     * @param paramTreeNode 要添加的树节点
     */
    void addNode(String paramString, TreeNode paramTreeNode);

    /**
     * 向树中添加多个节点
     *
     * @param paramList 要添加的树节点列表
     */
    void addNodes(List<TreeNode> paramList);

    /**
     * 在指定父节点下添加多个树节点
     *
     * @param paramString 父节点的标识符
     * @param paramList 要添加的树节点列表
     */
    void addNodes(String paramString, List<TreeNode> paramList);

    /**
     * 从树中移除指定节点，并选择性地移除其子节点
     *
     * @param paramTreeNode 要移除的树节点
     * @param paramBoolean 如果为true，则同时移除该节点的所有子节点；否则仅移除该节点
     */
    void removeNode(TreeNode paramTreeNode, boolean paramBoolean);

    /**
     * 从树中移除指定节点，保留其子节点
     *
     * @param paramTreeNode 要移除的树节点
     */
    void removeNode(TreeNode paramTreeNode);

    /**
     * 移除指定节点的所有子节点，并返回这些子节点的集合
     *
     * @param paramString 要移除子节点的父节点标识符
     * @return 被移除的子节点的集合
     */
    Collection<TreeNode> removeChildNodes(String paramString);

    /**
     * 根据节点标识符获取节点
     *
     * @param paramString 节点的标识符
     * @return 找到的树节点，如果未找到则返回null
     */
    TreeNode getNodeById(String paramString);

    /**
     * 获取指定节点的所有子节点
     *
     * @param paramString 父节点的标识符
     * @return 子节点的集合
     */
    Collection<TreeNode> getChildren(String paramString);

    /**
     * 获取树中的所有节点
     *
     * @return 树节点的集合
     */
    Collection<TreeNode> getNodes();

    /**
     * 以数组形式获取树中的所有节点
     *
     * @return 树节点的列表
     */
    List<TreeNode> getNodesAsArray();

    /**
     * 根据节点标识符获取节点的父节点
     *
     * @param paramString 节点的标识符
     * @return 节点的父节点，如果未找到则返回null
     */
    TreeNode getParentNode(String paramString);

    /**
     * 获取指定树节点的父节点
     *
     * @param paramTreeNode 要查询父节点的树节点
     * @return 指定节点的父节点，如果未找到则返回null
     */
    TreeNode getParentNode(TreeNode paramTreeNode);

    /**
     * 对树中的每个节点执行给定的操作
     *
     * @param paramCallback 要执行的操作，接受树和节点作为参数
     * @throws Throwable 如果操作执行过程中抛出异常
     */
    void forEach(Consumer2<Tree, TreeNode> paramCallback) throws Throwable;

    /**
     * 使用给定的比较器对树中的节点进行排序
     *
     * @param paramComparator 比较器，用于比较树节点
     */
    void sort(Comparator<TreeNode> paramComparator);

    /**
     * 获取树的所有根节点
     *
     * @return 根节点的列表
     */
    List<TreeNode> getRootNodes();

    /**
     * 清空树，移除所有节点
     */
    void clear();
}
