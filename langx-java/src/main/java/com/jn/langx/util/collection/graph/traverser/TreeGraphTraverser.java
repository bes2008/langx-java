package com.jn.langx.util.collection.graph.traverser;

import com.jn.langx.util.collection.graph.*;

import java.util.Map;

/**
 * 深度优先遍历，但遍历时，先遍历父节点
 * @param <T>
 */
public class TreeGraphTraverser<T> implements GraphTraverser<T> {
    @Override
    public void traverse(Graph<T> graph, String vertexName, VertexConsumer<T> consumer) {
        Vertex v = graph.getVertex(vertexName);
        if (v == null) {
            throw new IllegalArgumentException("the vertex (" + vertexName + ") is not exists");
        }
        Map<String, VisitStatus> visitStatusMap = Graphs.newVisitStatusMap();
        spanningTreeTraverse(visitStatusMap, graph, v, null, consumer);
    }

    private void spanningTreeTraverse(Map<String, VisitStatus> visitStatusMap, Graph<T> graph, Vertex v, Edge<T> edge, VertexConsumer<T> consumer) {
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
                spanningTreeTraverse(visitStatusMap, graph, e.getTo(), e, consumer);
            }
        }
    }
}
