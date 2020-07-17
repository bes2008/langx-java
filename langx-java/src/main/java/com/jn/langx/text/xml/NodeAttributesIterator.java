package com.jn.langx.text.xml;

import com.jn.langx.util.collection.iter.UnmodifiableIterator;
import org.w3c.dom.Attr;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class NodeAttributesIterator extends UnmodifiableIterator<Attr> {
    private NamedNodeMap nodeMap;
    private int currentIndex = 0;

    public NodeAttributesIterator(Node node) {
        nodeMap = node.getAttributes();
    }

    public NodeAttributesIterator(NamedNodeMap namedNodeMap) {
        this.nodeMap = namedNodeMap;
    }

    @Override
    public boolean hasNext() {
        if(nodeMap==null){
            return false;
        }
        return nodeMap.getLength() > currentIndex;
    }

    @Override
    public Attr next() {
        if(nodeMap==null){
            return null;
        }
        return (Attr)nodeMap.item(currentIndex++);
    }
}
