package com.jn.langx.util.struct.tree;

import java.util.*;


public class SimpleTree implements Tree<TreeNode> {
    private static final long serialVersionUID = -9051148743662948065L;
    private List<TreeNode> nodes = new ArrayList();
    private transient Map<String, TreeNode> nodeMap = new HashMap();

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

        this.nodes.remove(node.getId());


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
            return Collections.EMPTY_LIST;
        }
        List<TreeNode> removed = new LinkedList();
        removeChildNodes(pid, removed);
        return removed;
    }

    public void removeChildNodes(String pid, List<TreeNode> removed) {
        TreeNode node = (TreeNode) this.nodeMap.get(pid);
        Collection<TreeNode> children = getChildren(node.getId());
        if ((children != null) && (!children.isEmpty())) {
            Iterator<TreeNode> iter = children.iterator();
            while (iter.hasNext()) {
                TreeNode n = (TreeNode) iter.next();
                removeChildNodes(n.getId(), removed);
                removeNode(n, false);
            }
            removed.addAll(children);
        }
    }

    @Override
    public TreeNode getNodeById(String id) {
        return (TreeNode) this.nodeMap.get(id);
    }

    @Override
    public Collection<TreeNode> getChildren(String id) {
        TreeNode n = (TreeNode) this.nodeMap.get(id);
        if (n != null) {
            List<TreeNode> children = new LinkedList();
            Iterator<TreeNode> iter = this.nodes.iterator();
            while (iter.hasNext()) {
                TreeNode node = (TreeNode) iter.next();
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
        if ((this.nodes instanceof List)) {
            return this.nodes;
        }
        return new ArrayList(this.nodes);
    }

    @Override
    public void forEach(Callback cb) throws Throwable {
        Iterator<TreeNode> iter = this.nodes.iterator();
        while (iter.hasNext()) {
            TreeNode node = (TreeNode) iter.next();
            cb.call(this, node);
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