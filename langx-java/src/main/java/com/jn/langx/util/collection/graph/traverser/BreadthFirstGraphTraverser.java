package com.jn.langx.util.collection.graph.traverser;

import com.jn.langx.util.collection.graph.*;

import java.util.LinkedList;
import java.util.Map;

/**
 * 广度优先遍历，先从最高层遍历，逐层遍历
 *
 * @param <T>
 */
public class BreadthFirstGraphTraverser<T> extends AbstractGraphTraverser<T> {

    @Override
    protected void traverse(Map<String, VisitStatus> visitStatusMap, Graph<T> graph, Vertex<T> v, Edge<T> edge, VertexConsumer<T> consumer) {
        LinkedList<Vertex<T>> q = new LinkedList<Vertex<T>>();

        q.add(v);
        if (consumer != null) {
            consumer.accept(graph, v, null);
        }
        Graphs.finishVisit(visitStatusMap, v.getName());
        while (!q.isEmpty()) {
            v = q.removeFirst();
            for (int i = 0; i < v.getOutgoingEdgeCount(); i++) {
                Edge<T> e = v.getOutgoingEdge(i);
                Vertex<T> to = e.getTo();
                if (Graphs.isNotVisited(visitStatusMap, to.getName())) {
                    q.add(to);
                    if (consumer != null) {
                        consumer.accept(graph, to, e);
                    }
                    Graphs.finishVisit(visitStatusMap, to.getName());
                }
            }
        }
    }
}
