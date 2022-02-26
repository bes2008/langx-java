package com.jn.langx.util.collection.graph;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.util.Objs;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.collection.graph.traverser.BreadthFirstGraphTraverser;
import com.jn.langx.util.collection.graph.traverser.DeepFirstGraphTraverser;
import com.jn.langx.util.collection.graph.traverser.TreeGraphTraverser;
import com.jn.langx.util.comparator.IntegerComparator;
import com.jn.langx.util.function.Consumer;
import com.jn.langx.util.function.Function;
import com.jn.langx.util.function.Predicate;
import com.jn.langx.util.function.Supplier;

import java.util.*;

public class Graphs {

    private static final Supplier<String, VisitStatus> NOT_VISITED_SUPPLIER = new Supplier<String, VisitStatus>() {
        @Override
        public VisitStatus get(String name) {
            return VisitStatus.NOT_VISITED;
        }
    };

    public static Map<String, VisitStatus> newVisitStatusMap() {
        return Collects.emptyNonAbsentHashMap(NOT_VISITED_SUPPLIER);
    }

    public static VisitStatus getVisitStatus(Map<String, VisitStatus> statusMap, String name) {
        return Collects.wrapAsNonAbsentMap(statusMap, NOT_VISITED_SUPPLIER).get(name);
    }

    public static boolean isNotVisited(Map<String, VisitStatus> statusMap, String name) {
        return getVisitStatus(statusMap, name) == VisitStatus.NOT_VISITED;
    }

    public static boolean isVisiting(Map<String, VisitStatus> statusMap, String name) {
        return getVisitStatus(statusMap, name) == VisitStatus.VISITING;
    }

    public static void beginVisit(Map<String, VisitStatus> statusMap, String name) {
        statusMap.put(name, VisitStatus.VISITING);
    }

    public static void finishVisit(Map<String, VisitStatus> statusMap, String name) {
        statusMap.put(name, VisitStatus.VISITED);
    }

    public static List<String> hasCycle(final Graph graph) {
        final List<Vertex> vertices = graph.getVertices();
        final Map<String, VisitStatus> vertexStateMap = Graphs.newVisitStatusMap();
        List<String> retValue = null;
        for (Vertex vertex : vertices) {
            if (Graphs.isNotVisited(vertexStateMap, vertex.getName())) {
                retValue = detectCycle(vertex, vertexStateMap);
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
     */
    public static List<String> detectCycle(final Vertex vertex, final Map<String, VisitStatus> vertexStateMap) {
        final LinkedList<String> cycleStack = new LinkedList<String>();

        final boolean hasCycle = dfsVisitCheckCycle(vertex, cycleStack, vertexStateMap);

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

    public static List<String> detectCycle(final Vertex vertex) {
        final Map<String, VisitStatus> vertexStateMap = Graphs.newVisitStatusMap();
        return detectCycle(vertex, vertexStateMap);
    }


    private static boolean dfsVisitCheckCycle(final Vertex vertex, final LinkedList<String> cycle,
                                              final Map<String, VisitStatus> vertexStateMap) {
        cycle.addFirst(vertex.getName());

        Graphs.beginVisit(vertexStateMap, vertex.getName());
        List<Vertex> outgoing = vertex.getOutgoingVertices();
        for (Vertex v : outgoing) {
            if (Graphs.isNotVisited(vertexStateMap, v.getName())) {
                final boolean hasCycle = dfsVisitCheckCycle(v, cycle, vertexStateMap);
                if (hasCycle) {
                    return true;
                }
            } else if (Graphs.isVisiting(vertexStateMap, v.getName())) {
                cycle.addFirst(v.getName());
                return true;
            }
        }
        Graphs.finishVisit(vertexStateMap, vertex.getName());
        cycle.removeFirst();
        return false;
    }

    public static final DeepFirstGraphTraverser DFS = new DeepFirstGraphTraverser();
    public static final TreeGraphTraverser TDFS = new TreeGraphTraverser();
    public static final BreadthFirstGraphTraverser BFS = new BreadthFirstGraphTraverser();

    public static <T> void traverse(final GraphTraverser traverser, final Graph<T> graph, final VertexConsumer<T> consumer) {
        final Map<String, VisitStatus> visitStatusMap = new LinkedHashMap<String, VisitStatus>();
        Collects.forEach(graph.getVertices(), new Consumer<Vertex<T>>() {
            @Override
            public void accept(Vertex<T> vertex) {
                traverser.traverse(visitStatusMap, graph, vertex.getName(), consumer);
            }
        });
    }

    public static <T> void traverse(@NonNull GraphTraverser traverser, Graph<T> graph, String vertexName, final VertexConsumer<T> consumer) {
        traverser.traverse(graph, vertexName, consumer);
    }

    public static <T> List<Vertex<T>> sort(@NonNull GraphTraverser traverser, Graph<T> graph) {
        final List<Vertex<T>> retValue = new ArrayList<Vertex<T>>();
        VertexConsumer<T> consumer = new VertexConsumer<T>() {
            @Override
            public void accept(Graph<T> graph, Vertex<T> vertex, Edge<T> edge) {
                retValue.add(vertex);
            }
        };
        traverse(traverser, graph, consumer);
        return retValue;
    }

    public static <T> List<Vertex<T>> sort(@NonNull GraphTraverser traverser, Graph<T> graph, String vertexName) {
        final List<Vertex<T>> retValue = new ArrayList<Vertex<T>>();
        VertexConsumer<T> consumer = new VertexConsumer<T>() {
            @Override
            public void accept(Graph<T> graph, Vertex<T> vertex, Edge<T> edge) {
                retValue.add(vertex);
            }
        };
        traverse(traverser, graph, vertexName, consumer);
        return retValue;
    }

    public static <T> List<Vertex<T>> dfsSort(final Graph<T> graph) {
        return sort(DFS, graph);
    }

    public static <T> List<Vertex<T>> dfsSort(Graph<T> graph, final String vertexName) {
        return sort(DFS, graph, vertexName);
    }


    public static <T> List<Vertex<T>> tdfsSort(final Graph<T> graph) {
        final List<Vertex<T>> retValue = new ArrayList<Vertex<T>>();
        VertexConsumer<T> consumer = new VertexConsumer<T>() {
            @Override
            public void accept(Graph<T> graph, Vertex<T> vertex, Edge<T> edge) {
                // 对它的所有outgoing遍历，找到已存在于 retValue 中的，所有依赖的最小索引，然后执行 insert
                int minToIndex = -1;
                List<Integer> indexes = Pipeline.of(vertex.getOutgoingVertices())
                        .map(new Function<Vertex<T>, Integer>() {
                            @Override
                            public Integer apply(Vertex<T> to) {
                                return retValue.lastIndexOf(to);
                            }
                        }).filter(new Predicate<Integer>() {
                            @Override
                            public boolean test(Integer index) {
                                return index >= 0;
                            }
                        }).asList();
                if (Objs.isNotEmpty(indexes)) {
                    if (indexes.size() == 1) {
                        minToIndex = indexes.get(0);
                    } else {
                        minToIndex = Pipeline.of(indexes).min(new IntegerComparator());
                    }
                }

                if (minToIndex < 0) {
                    retValue.add(vertex);
                } else {
                    retValue.add(minToIndex, vertex);
                }
            }
        };
        traverse(TDFS, graph, consumer);
        return Collects.asList(retValue);
    }

    public static <T> List<Vertex<T>> tdfsSort(Graph<T> graph, final String vertexName) {
        final List<Vertex<T>> retValue = new ArrayList<Vertex<T>>();
        VertexConsumer<T> consumer = new VertexConsumer<T>() {
            @Override
            public void accept(Graph<T> graph, Vertex<T> vertex, Edge<T> edge) {
                // 对它的所有outgoing遍历，找到已存在于 retValue 中的，所有依赖的最小索引，然后执行 insert
                int minToIndex = -1;
                List<Integer> indexes = Pipeline.of(vertex.getOutgoingVertices())
                        .map(new Function<Vertex<T>, Integer>() {
                            @Override
                            public Integer apply(Vertex<T> to) {
                                return retValue.lastIndexOf(to);
                            }
                        }).filter(new Predicate<Integer>() {
                            @Override
                            public boolean test(Integer index) {
                                return index >= 0;
                            }
                        }).asList();
                if (Objs.isNotEmpty(indexes)) {
                    if (indexes.size() == 1) {
                        minToIndex = indexes.get(0);
                    } else {
                        minToIndex = Pipeline.of(indexes).min(new IntegerComparator());
                    }
                }

                if (minToIndex < 0) {
                    retValue.add(vertex);
                } else {
                    retValue.add(minToIndex, vertex);
                }
            }
        };
        traverse(TDFS, graph, vertexName, consumer);
        return Collects.asList(retValue);
    }

    public static <T> List<Vertex<T>> bfsSort(Graph<T> graph, final String vertexName) {
        return sort(BFS, graph, vertexName);
    }

    public static <T> List<Vertex<T>> bfsSort(final Graph<T> graph) {
        return sort(BFS, graph);
    }


}
