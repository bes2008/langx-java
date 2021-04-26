package com.jn.langx.util.collection.graph;

import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.graph.traverser.DeepFirstGraphTraverser;
import com.jn.langx.util.function.Consumer;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class GraphTopologicalSorter {

    /**
     * @param graph
     * @return List of String (vertex labels)
     */

    public static <T> List<Vertex<T>> sort(final Graph<T> graph) {
        final List<Vertex<T>> ret = new ArrayList<Vertex<T>>();

        Collects.forEach(graph.getVertices(), new Consumer<Vertex<T>>() {
            @Override
            public void accept(Vertex<T> vertex) {
                ret.addAll(sort(graph, vertex.getName()));
            }
        });
        return ret;
    }

    public static <T> List<Vertex<T>> sort(Graph<T> graph, final String vertexName) {
        // we need to use addFirst method so we will use LinkedList explicitly
        final List<Vertex<T>> retValue = new LinkedList<Vertex<T>>();
        VertexConsumer<T> consumer = new VertexConsumer<T>() {
            @Override
            public void accept(Graph<T> graph, Vertex<T> vertex, Edge<T> edge) {
                retValue.add(vertex);
            }
        };
        DeepFirstGraphTraverser<T> dfs = new DeepFirstGraphTraverser<T>();
        dfs.traverse(graph, vertexName, consumer);
        return retValue;
    }

}
