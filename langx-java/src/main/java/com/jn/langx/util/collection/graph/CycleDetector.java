package com.jn.langx.util.collection.graph;


import java.util.*;

public class CycleDetector {

    public static List<String> hasCycle(final Graph graph) {
        final List<Vertex> vertices = graph.getVertices();
        final Map<Vertex, Integer> vertexStateMap = new HashMap<Vertex, Integer>();
        List<String> retValue = null;
        for (Vertex vertex : vertices) {
            if (isNotVisited(vertex, vertexStateMap)) {
                retValue = introducesCycle(vertex, vertexStateMap);

                if (retValue != null) {
                    break;
                }
            }
        }

        return retValue;
    }

    /**
     * This method will be called when an edge leading to given vertex was added and we want to check if introduction of
     * this edge has not resulted in apparition of cycle in the graph
     *
     * @param vertex
     * @param vertexStateMap
     * @return
     */
    public static List<String> introducesCycle(final Vertex vertex, final Map<Vertex, Integer> vertexStateMap) {
        final LinkedList<String> cycleStack = new LinkedList<String>();

        final boolean hasCycle = dfsVisit(vertex, cycleStack, vertexStateMap);

        if (hasCycle) {
            // we have a situation like: [b, a, c, d, b, f, g, h].
            // Label of Vertex which introduced the cycle is at the first position in the list
            // We have to find second occurrence of this label and use its position in the list
            // for getting the sublist of vertex labels of cycle participants
            //
            // So in our case we are searching for [b, a, c, d, b]
            final String label = cycleStack.getFirst();

            final int pos = cycleStack.lastIndexOf(label);

            final List<String> cycle = cycleStack.subList(0, pos + 1);

            Collections.reverse(cycle);

            return cycle;
        }

        return null;
    }

    public static List<String> introducesCycle(final Vertex vertex) {
        final Map<Vertex, Integer> vertexStateMap = new HashMap<Vertex, Integer>();

        return introducesCycle(vertex, vertexStateMap);
    }

    /**
     * @param vertex
     * @param vertexStateMap
     * @return
     */
    private static boolean isNotVisited(final Vertex vertex, final Map<Vertex, Integer> vertexStateMap) {
        final Integer state = vertexStateMap.get(vertex);

        return (state == null) || Graphs.NOT_VISITED.equals(state);
    }

    /**
     * @param vertex
     * @param vertexStateMap
     * @return
     */
    private static boolean isVisiting(final Vertex vertex, final Map<Vertex, Integer> vertexStateMap) {
        final Integer state = vertexStateMap.get(vertex);

        return Graphs.VISITING.equals(state);
    }

    private static boolean dfsVisit(final Vertex vertex, final LinkedList<String> cycle,
                                    final Map<Vertex, Integer> vertexStateMap) {
        cycle.addFirst(vertex.getName());

        vertexStateMap.put(vertex, Graphs.VISITING);
        List<Vertex> outgoing = vertex.getOutgoingVertices();
        for (Vertex v : outgoing) {
            if (isNotVisited(v, vertexStateMap)) {
                final boolean hasCycle = dfsVisit(v, cycle, vertexStateMap);

                if (hasCycle) {
                    return true;
                }
            } else if (isVisiting(v, vertexStateMap)) {
                cycle.addFirst(v.getName());

                return true;
            }
        }
        vertexStateMap.put(vertex, Graphs.VISITED);

        cycle.removeFirst();

        return false;
    }
}
