package com.jn.langx.util.collection.tree;

import com.jn.langx.util.function.Consumer2;

import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

public interface Tree<T extends TreeNode> extends Serializable {
    void addNode(TreeNode paramTreeNode);

    void addNode(String paramString, TreeNode paramTreeNode);

    void addNodes(List<TreeNode> paramList);

    void addNodes(String paramString, List<TreeNode> paramList);

    void removeNode(TreeNode paramTreeNode, boolean paramBoolean);

    void removeNode(TreeNode paramTreeNode);

    Collection<TreeNode> removeChildNodes(String paramString);

    TreeNode getNodeById(String paramString);

    Collection<TreeNode> getChildren(String paramString);

    Collection<TreeNode> getNodes();

    List<TreeNode> getNodesAsArray();

    TreeNode getParentNode(String paramString);

    TreeNode getParentNode(TreeNode paramTreeNode);

    void forEach(Consumer2<Tree, TreeNode> paramCallback) throws Throwable;

    void sort(Comparator<TreeNode> paramComparator);

    List<TreeNode> getRootNodes();

    void clear();
}