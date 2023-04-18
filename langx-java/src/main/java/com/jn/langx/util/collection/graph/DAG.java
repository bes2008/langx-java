package com.jn.langx.util.collection.graph;

import java.util.List;

public class DAG<T> extends Graph<T> {
    @Override
    public boolean addEdge(Vertex<T> from, Vertex<T> to, int weight) {
        if (!hasVertex(from.getName())) {
            addVertex(from);
        }
        if (!hasVertex(to.getName())) {
            addVertex(to);
        }
        Edge<T> e = new Edge<T>(from, to, weight);
        if (from.findEdge(to) != null) {
            // 已有该 edge，则不再添加
            return false;
        } else {
            from.addEdge(e);
            to.addEdge(e);
            final List<String> cycle = Graphs.detectCycle(to);

            if (cycle != null) {
                // remove edge which introduced cycle
                removeEdge(from, to);
                final String msg = "Edge between '" + from + "' and '" + to + "' introduces to cycle in the graph";
                throw new CycleDetectedException(msg, cycle);
            }
            edges.add(e);
            return true;
        }
    }
}
