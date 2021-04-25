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

    protected void traverse(Map<String, VisitStatus> visitStatusMap, Graph<T> graph, Vertex<T> vertex, Edge<T> edge, VertexConsumer<T> consumer) {
        if (consumer != null) {
            consumer.accept(graph, vertex, edge);
        }
        Graphs.finishVisit(visitStatusMap, vertex.getName());
        for (int i = 0; i < vertex.getOutgoingEdgeCount(); i++) {
            Edge<T> e = vertex.getOutgoingEdge(i);
            if (Graphs.isNotVisited(visitStatusMap, e.getTo().getName())) {
                if (consumer != null) {
                    consumer.accept(graph, vertex, e);
                }
                Graphs.finishVisit(visitStatusMap, vertex.getName());
                traverse(visitStatusMap, graph, e.getTo(), e, consumer);
            }
        }
    }
}
