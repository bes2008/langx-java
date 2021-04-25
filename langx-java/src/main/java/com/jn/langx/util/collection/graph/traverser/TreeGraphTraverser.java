package com.jn.langx.util.collection.graph.traverser;

import com.jn.langx.util.collection.graph.*;

import java.util.Map;

/**
 * spanningTree
 * <p>
 * 深度优先遍历，但遍历时，先遍历父节点
 *
 * @param <T>
 */
public class TreeGraphTraverser<T> extends AbstractGraphTraverser<T> {

    protected void traverse(Map<String, VisitStatus> visitStatusMap, Graph<T> graph, Vertex<T> v, Edge<T> edge, VertexConsumer<T> consumer) {
        if (consumer != null) {
            consumer.accept(graph, v, edge);
        }
        Graphs.finishVisit(visitStatusMap, v.getName());
        for (int i = 0; i < v.getOutgoingEdgeCount(); i++) {
            Edge<T> e = v.getOutgoingEdge(i);
            if (Graphs.isNotVisited(visitStatusMap, e.getTo().getName())) {
                if (consumer != null) {
                    consumer.accept(graph, v, e);
                }
                Graphs.finishVisit(visitStatusMap, v.getName());
                traverse(visitStatusMap, graph, e.getTo(), e, consumer);
            }
        }
    }
}
