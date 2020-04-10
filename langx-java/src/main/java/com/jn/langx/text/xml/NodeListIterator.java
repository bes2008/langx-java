package com.jn.langx.text.xml;

import com.jn.langx.util.collection.iter.UnmodifiableIterator;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class NodeListIterator extends UnmodifiableIterator<Node> {
    private NodeList nodeList;
    private int currentIndex = 0;

    public NodeListIterator(NodeList nodeList) {
        this.nodeList = nodeList;
    }

    @Override
    public boolean hasNext() {
        return currentIndex < nodeList.getLength();
    }

    @Override
    public Node next() {
        Node node = nodeList.item(currentIndex);
        this.currentIndex++;
        return node;
    }
}
