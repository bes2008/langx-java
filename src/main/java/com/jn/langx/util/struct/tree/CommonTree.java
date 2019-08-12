package com.jn.langx.util.struct.tree;

import java.util.*;


public class CommonTree implements Tree<TreeNode> {
    private static final long serialVersionUID = 8663945462770387472L;
    private transient Map<String, TreeNode> nodeMap = new HashMap();
    private List<TreeNode> nodes = new LinkedList();

    public CommonTree() {
        this(null);
    }

    public CommonTree(Collection<TreeNode> nodes) {
        if (nodes != null) {
            this.nodes.addAll(nodes);
        }
    }

    @Override
    public void addNode(TreeNode node) {
        String pid = node.getPid();
        addNode(pid, node);
    }

    @Override
    public void addNode(String pid, TreeNode node) {
        if (pid == null) {
            pid = node.getPid();
        }
        node.setPid(pid);

        TreeNode parentNode = (TreeNode) this.nodeMap.get(pid);
        if (parentNode != null) {
            parentNode.addChildNode(node);
        } else {
            List<TreeNode> rootNodes = this.nodes;


            TreeNode pnode = null;
            boolean isChild = false;
            for (TreeNode rootNode : rootNodes) {
                if (rootNode.getId().equals(node.getPid())) {
                    pnode = rootNode;
                    break;
                }
            }
            if (pnode != null) {
                isChild = true;
                pnode.addChildNode(node);
            }


            boolean isParent = node.getIsParent();
            if (!isParent) {
                Iterator<TreeNode> iter = rootNodes.iterator();
                while (iter.hasNext()) {
                    TreeNode rootNode = (TreeNode) iter.next();
                    if (node.getId().equals(rootNode.getPid())) {
                        node.setIsParent(true);
                        node.addChildNode(rootNode);
                        iter.remove();
                    }
                }
            }

            if (!isChild) {
                this.nodes.add(node);
            }
        }

        this.nodeMap.put(node.getId(), node);
    }

    @Override
    public void addNodes(List<TreeNode> nodes) {
        for (TreeNode node : nodes) {
            addNode(node);
        }
    }

    @Override
    public void addNodes(String pid, List<TreeNode> nodes) {
        TreeNode parentNode = (TreeNode) this.nodeMap.get(pid);
        if (parentNode != null) {
            for (TreeNode node : nodes) {
                node.setPid(pid);
            }
            parentNode.addChildNodes(nodes);
        } else {
            nodes.addAll(nodes);
        }
    }

    @Override
    public void removeNode(TreeNode node) {
        removeNode(node, false);
    }

    @Override
    public void removeNode(TreeNode node, boolean recursion) {
        String pid = node.getPid();
        TreeNode parentNode = (TreeNode) this.nodeMap.get(pid);

        if (parentNode == null) {
            this.nodes.remove(node);
        } else {
            parentNode.removeChildNode(node);
        }

        this.nodeMap.remove(node.getId());

        if (recursion) {
            Collection children = node.getChildren();
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
    public Collection<TreeNode> removeChildNodes(String pid) {
        TreeNode parentNode = (TreeNode) this.nodeMap.get(pid);
        if (parentNode == null) {
            return Collections.EMPTY_LIST;
        }
        Collection<TreeNode> children = parentNode.removeChildNodes();
        if ((children != null) && (!children.isEmpty())) {
            Iterator<TreeNode> iter = children.iterator();
            while (iter.hasNext()) {
                TreeNode n = (TreeNode) iter.next();
                removeNode(n, true);
            }
        }
        return children;
    }

    @Override
    public TreeNode getNodeById(String id) {
        return (TreeNode) this.nodeMap.get(id);
    }

    @Override
    public Collection<TreeNode> getNodes() {
        return this.nodes;
    }

    @Override
    public List<TreeNode> getNodesAsArray() {
        List<TreeNode> descendants = new LinkedList();
        for (TreeNode node : this.nodes) {
            node.extractDescendants(descendants, true);
        }
        return descendants;
    }

    @Override
    public void forEach(Callback cb) throws Throwable {
        forEachCollection(this.nodes, cb);
    }

    private void forEachCollection(Collection collection, Callback cb) throws Throwable {
        Iterator<TreeNode> iter = collection.iterator();
        while (iter.hasNext()) {
            TreeNode node = (TreeNode) iter.next();
            cb.call(this, node);
            Collection children = node.getChildren();
            if ((children != null) && (!children.isEmpty())) {
                forEachCollection(children, cb);
            }
        }
    }

    @Override
    public TreeNode getParentNode(String treeNodeId) {
        TreeNode node = getNodeById(treeNodeId);
        if (node == null) {
            throw new TreeNodeNotFoundException(treeNodeId);
        }
        return getParentNode(node);
    }

    @Override
    public TreeNode getParentNode(TreeNode treeNode) {
        return getNodeById(treeNode.getPid());
    }

    @Override
    public void sort(final Comparator<TreeNode> comparator) {
        try {
            forEachCollection(this.nodes, new Callback() {
                private List<TreeNode> nodes;

                @Override
                public void call(Tree tree, TreeNode node)
                        throws Throwable {
                    Collections.sort(this.nodes, comparator);
                    setNodes(node.getChildren());
                }

                public void setNodes(Collection<TreeNode> nodes) {
                    this.nodes = new LinkedList();
                    if ((nodes != null) && (!nodes.isEmpty())) {
                        this.nodes.addAll(nodes);
                    }
                }
            });
        } catch (Throwable localThrowable) {
        }
    }

    @Override
    public List<TreeNode> getRootNodes() {
        return this.nodes;
    }

    @Override
    public Collection<TreeNode> getChildren(String id) {
        TreeNode node = (TreeNode) this.nodeMap.get(id);
        if (node != null) {
            return node.getChildren();
        }
        return null;
    }

    @Override
    public void clear() {
        this.nodes.clear();
        this.nodeMap.clear();
    }
}