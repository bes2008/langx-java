package com.jn.langx.util.collection.graph;

import java.util.Map;

public interface GraphTraverser<T> {

    void traverse(Graph<T> graph, String vertexName, VertexConsumer<T> consumer);
    /**
     * Called by the graph traversal methods when a vertex is first visited.
     *
     * @param graph      - the graph
     * @param vertexName - the vertex being visited.
     */
    void traverse(Map<String, VisitStatus> visitStatusMap,Graph<T> graph, String vertexName, VertexConsumer<T> consumer);
}
