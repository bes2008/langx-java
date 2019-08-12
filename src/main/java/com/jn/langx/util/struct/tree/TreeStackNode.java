package com.jn.langx.util.struct.tree;


public class TreeStackNode extends TreeNode {
    private boolean open = false;

    public TreeStackNode(String id, String pid, String name) {
        super(id, pid, name);
    }

    public boolean isOpen() {
        return this.open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }
}
