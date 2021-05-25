package com.jn.langx.util.collection.graph.traverser;

import com.jn.langx.util.collection.graph.*;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractGraphTraverser<T> implements GraphTraverser<T> {
    @Override
    public void traverse(Graph<T> graph, String vertexName, VertexConsumer<T> consumer) {
        Map<String, VisitStatus> visitStatusMap = new HashMap<String, VisitStatus>();
        traverse(visitStatusMap, graph, vertexName, consumer);
    }

    @Override
    public void traverse(Map<String, VisitStatus> visitStatusMap, Graph<T> graph, String vertexName, VertexConsumer<T> consumer) {
        Vertex<T> vertex = graph.getVertex(vertexName);
        if (vertex == null) {
            throw new IllegalArgumentException("the vertex (" + vertexName + ") is not exists");
        }

        traverse(visitStatusMap, graph, vertex, null, consumer);
    }


    protected final void doVisit(Map<String, VisitStatus> visitStatusMap, Graph<T> graph, Vertex<T> vertex, Edge<T> edge, VertexConsumer<T> consumer) {
        if (Graphs.isNotVisited(visitStatusMap, vertex.getName())) {
            if (consumer != null) {
                consumer.accept(graph, vertex, edge);
            }
            Graphs.finishVisit(visitStatusMap, vertex.getName());
        }
    }

    protected abstract void traverse(Map<String, VisitStatus> visitStatusMap, Graph<T> graph, Vertex<T> vertex, Edge<T> edge, VertexConsumer<T> consumer);
}
