package com.jn.langx.util.collection.graph.traverser;

import com.jn.langx.util.collection.graph.*;

import java.util.Map;

public abstract class AbstractGraphTraverser<T> implements GraphTraverser<T> {
    @Override
    public void traverse(Graph<T> graph, String vertexName, VertexConsumer<T> consumer) {
        Vertex<T> vertex = graph.getVertex(vertexName);
        if (vertex == null) {
            throw new IllegalArgumentException("the vertex (" + vertexName + ") is not exists");
        }

        Map<String, VisitStatus> visitStatusMap = Graphs.newVisitStatusMap();
        traverse(visitStatusMap, graph, vertex, null, consumer);
    }

    protected abstract void traverse(Map<String, VisitStatus> visitStatusMap, Graph<T> graph, Vertex<T> vertex, Edge<T> edge, VertexConsumer<T> consumer);
}
