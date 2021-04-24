package com.jn.langx.util.collection.graph;

public interface GraphTraverser<T> {
    /**
     * Called by the graph traversal methods when a vertex is first visited.
     *
     * @param graph      - the graph
     * @param vertexName - the vertex being visited.
     */
    void traverse(Graph<T> graph, String vertexName, VertexConsumer<T> consumer);
}
