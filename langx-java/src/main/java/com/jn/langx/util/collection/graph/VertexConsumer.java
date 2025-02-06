package com.jn.langx.util.collection.graph;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;

/**
 * The VertexConsumer interface is used to consume vertices in a graph.
 * It provides a standard way to process each vertex and its associated edges in a graph structure.
 *
 * @param <T> Represents the type of data stored in the vertices of the graph, supporting generic types.
 */
public interface VertexConsumer<T> {
    /**
     * Processes a specified vertex in the graph.
     *
     * @param graph The graph object, cannot be null, provides access to the entire graph structure.
     * @param vertex The vertex object to be processed, cannot be null, represents the specific vertex being processed.
     * @param edge The edge object associated with the vertex, can be null, represents the edge connected to the vertex, if any.
     */
    void accept(@NonNull Graph<T> graph, @NonNull Vertex<T> vertex, @Nullable Edge<T> edge);
}
