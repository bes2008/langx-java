package com.jn.langx.util.collection.graph;

import java.util.Map;

/**
 * The GraphTraverser interface defines methods for traversing a graph.
 * It is used to perform uniform traversals on graph structures, providing two different traversal methods.
 *
 * @param <T> The type of data stored in the vertices of the graph.
 */
public interface GraphTraverser<T> {

    /**
     * Traverses the graph using a specified starting vertex.
     * This method will visit all vertices in the graph starting from the specified vertex and perform a specified operation.
     *
     * @param graph      The graph to be traversed.
     * @param vertexName The name of the starting vertex for the traversal.
     * @param consumer   A consumer that performs an operation on each visited vertex.
     */
    void traverse(Graph<T> graph, String vertexName, VertexConsumer<T> consumer);

    /**
     * Traverses the graph using a specified starting vertex and visit status map.
     * This method allows for more control over the traversal process by using a visit status map to determine the state of vertices.
     *
     * @param visitStatusMap A map that records the visit status of vertices, used to avoid revisiting vertices.
     * @param graph          The graph to be traversed.
     * @param vertexName     The name of the starting vertex for the traversal.
     * @param consumer       A consumer that performs an operation on each visited vertex.
     */
    void traverse(Map<String, VisitStatus> visitStatusMap, Graph<T> graph, String vertexName, VertexConsumer<T> consumer);
}
