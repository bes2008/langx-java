package com.jn.langx.util.struct.tree;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


public class TreeNode<T extends TreeNode>
        implements Serializable {
    private static final long serialVersionUID = 3465696230080207245L;
    private String id;
    private String pid;
    private String name;
    private boolean isParent = false;
    private Collection<T> children;

    public TreeNode(String id, String pid, String name) {
        this(id, pid, name, false);
    }


    public TreeNode() {
    }

    public TreeNode(String id, String pid, String name, boolean isParent) {
        this(id, pid, name, isParent, null);
    }

    public TreeNode(String id, String pid, String name, boolean isParent, Collection<T> children) {
        if (id == null) {
            throw new IllegalArgumentException("node id is null");
        }
        this.id = id;
        this.pid = pid;
        this.name = name;
        if (name == null) {
            this.name = id;
        }
        this.isParent = isParent;
        if (isParent) {
            this.children = children;
        }
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        if (id != null) {
            this.id = id;
        }
    }

    public String getPid() {
        return this.pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public boolean getIsParent() {
        return this.isParent;
    }

    public void setIsParent(boolean isParent) {
        this.isParent = isParent;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        if (name != null) {
            this.name = name;
        }
    }

    public Collection<T> getChildren() {
        return this.children;
    }

    public void setChildren(Collection<T> children) {
        this.children = children;
    }

    public void addChildNode(T treeNode) {
        if (treeNode != null) {
            if (this.children == null) {
                this.children = new LinkedList();
            }
            this.children.add(treeNode);
        }
    }

    public void addChildNodes(List<T> treeNodes) {
        if ((treeNodes != null) && (!treeNodes.isEmpty())) {
            if (this.children == null) {
                this.children = new LinkedList();
            }
            this.children.addAll(treeNodes);
        }
    }

    public void removeChildNode(T treeNode) {
        if (this.children != null) {
            this.children.remove(treeNode);
        }
    }

    public List<T> removeChildNodes() {
        if ((this.children == null) || (this.children.isEmpty())) {
            return Collections.EMPTY_LIST;
        }
        List<T> ret = new LinkedList(this.children);
        this.children.clear();
        return ret;
    }

    public void clear() {
        if (this.children != null) {
            this.children.clear();
        }
    }

    public TreeNode getChildNodeById(String childNodeId) {
        if ((this.children == null) || (childNodeId == null)) {
            return null;
        }
        for (TreeNode node : this.children) {
            if (node.getId().equals(childNodeId)) {
                return node;
            }
        }
        return null;
    }

    public TreeNode getDescendant(String id) {
        if (getId().equals(id)) {
            return this;
        }

        Collection<T> children = getChildren();
        if (children != null) {
            for (TreeNode child : children) {
                TreeNode _node = child.getDescendant(id);
                if (_node != null) {
                    return _node;
                }
            }
        }
        return null;
    }

    public void extractDescendants(List<TreeNode> descendants, boolean containsSelf) {
        if (containsSelf) {
            descendants.add(this);
        }
        Collection<T> children = getChildren();
        if (children != null) {
            for (TreeNode child : children) {
                child.extractDescendants(descendants, true);
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if ((o == null) || (getClass() != o.getClass())) {
            return false;
        }
        TreeNode<?> treeNode = (TreeNode) o;

        return this.id.equals(treeNode.id);
    }
    @Override
    public int hashCode() {
        return this.id.hashCode();
    }
}
