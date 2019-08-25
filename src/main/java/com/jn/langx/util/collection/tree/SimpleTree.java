package com.jn.langx.util.collection.tree;

import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Consumer2;

import java.util.*;

@SuppressWarnings("all")
public class SimpleTree implements Tree<TreeNode> {
    private static final long serialVersionUID = -9051148743662948065L;
    private List<TreeNode> nodes = new ArrayList<TreeNode>();
    private transient Map<String, TreeNode> nodeMap = new HashMap<String, TreeNode>();

    public SimpleTree() {
        this(null);
    }

    public SimpleTree(Collection<TreeNode> nodes) {
        if (nodes != null) {
            this.nodes.addAll(nodes);
        }
    }

    @Override
    public void addNode(TreeNode node) {
        this.nodes.add(node);
        this.nodeMap.put(node.getId(), node);
    }

    @Override
    public void addNode(String pid, TreeNode node) {
        node.setPid(pid);
        addNode(node);
    }

    @Override
    public void addNodes(List<TreeNode> nodes) {
        if (nodes != null) {
            for (TreeNode node : nodes) {
                addNode(node);
            }
        }
    }

    @Override
    public void addNodes(String pid, List<TreeNode> nodes) {
        for (TreeNode node : nodes) {
            addNode(pid, node);
        }
    }

    @Override
    public void removeNode(TreeNode node, boolean recursion) {
        this.nodes.remove(node);

        this.nodeMap.remove(node.getId());


        if (recursion) {
            Collection<TreeNode> children = getChildren(node.getId());
            if ((children != null) && (!children.isEmpty())) {
                Iterator<TreeNode> iter = children.iterator();
                while (iter.hasNext()) {
                    TreeNode n = (TreeNode) iter.next();
                    removeNode(n, recursion);
                }
            }
        }
    }

    @Override
    public void removeNode(TreeNode node) {
        removeNode(node, false);
    }

    @Override
    public List<TreeNode> removeChildNodes(String pid) {
        if (pid == null) {
            return Collections.emptyList();
        }
        List<TreeNode> removed = new LinkedList<TreeNode>();
        removeChildNodes(pid, removed);
        return removed;
    }

    public void removeChildNodes(String pid, List<TreeNode> removed) {
        TreeNode node = this.nodeMap.get(pid);
        Collection<TreeNode> children = getChildren(node.getId());
        if ((children != null) && (!children.isEmpty())) {
            Iterator<TreeNode> iter = children.iterator();
            while (iter.hasNext()) {
                TreeNode n = iter.next();
                removeChildNodes(n.getId(), removed);
                removeNode(n, false);
            }
            removed.addAll(children);
        }
    }

    @Override
    public TreeNode getNodeById(String id) {
        return this.nodeMap.get(id);
    }

    @Override
    public Collection<TreeNode> getChildren(String id) {
        TreeNode n = this.nodeMap.get(id);
        if (n != null) {
            List<TreeNode> children = new LinkedList<TreeNode>();
            Iterator<TreeNode> iter = this.nodes.iterator();
            while (iter.hasNext()) {
                TreeNode node = iter.next();
                if (id.equals(node.getPid())) {
                    children.add(node);
                }
            }
            return children;
        }
        return null;
    }

    @Override
    public Collection<TreeNode> getNodes() {
        return this.nodes;
    }


    @Override
    public List<TreeNode> getNodesAsArray() {
        return Collects.asList(Collects.toArray(this.nodes, TreeNode[].class));
    }

    @Override
    public void forEach(final Consumer2<Tree, TreeNode> cb) throws Throwable {
        Iterator<TreeNode> iter = this.nodes.iterator();
        while (iter.hasNext()) {
            TreeNode node = iter.next();
            cb.accept(this, node);
        }
    }

    @Override
    public TreeNode getParentNode(String treeNodeId) {
        TreeNode node = getNodeById(treeNodeId);
        if (node != null) {
            return getParentNode(node);
        }
        return null;
    }

    @Override
    public TreeNode getParentNode(TreeNode treeNode) {
        return getNodeById(treeNode.getPid());
    }

    @Override
    public void sort(Comparator<TreeNode> comparator) {
        Collections.sort(this.nodes, comparator);
    }

    @Override
    public List<TreeNode> getRootNodes() {
        CommonTree cTree = new CommonTree();
        cTree.addNodes(this.nodes);
        List<TreeNode> roots = cTree.getRootNodes();
        cTree.clear();
        return roots;
    }

    @Override
    public void clear() {
        this.nodes.clear();
        this.nodeMap.clear();
    }

}