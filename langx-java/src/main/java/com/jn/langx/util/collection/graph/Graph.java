package com.jn.langx.util.collection.graph;


import com.jn.langx.util.StringJoiner;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.function.Function;

import java.util.*;

/**
 * A directed graph data structure.
 *
 * @param <T>
 */
@SuppressWarnings("unchecked")
public class Graph<T> {
    /**
     * Color used to mark unvisited nodes
     */
    public static final int VISIT_COLOR_WHITE = 1;
    /**
     * Color used to mark nodes as they are first visited in DFS order
     */
    public static final int VISIT_COLOR_GREY = 2;
    /**
     * Color used to mark nodes after descendants are completely visited
     */
    public static final int VISIT_COLOR_BLACK = 3;
    /**
     * Map<String, Vertex> of graph vertices
     */
    protected Map<String, Vertex<T>> vertices;
    /**
     * Vector<Edge> of edges in the graph
     */
    protected List<Edge<T>> edges;
    /**
     * The vertex identified as the root of the graph
     */
    protected Vertex<T> rootVertex;

    /**
     * Construct a new graph without any vertices or edges
     */
    public Graph() {
        vertices = new LinkedHashMap<String, Vertex<T>>();
        edges = new ArrayList<Edge<T>>();
    }

    /**
     * Are there any verticies in the graph
     *
     * @return true if there are no verticies in the graph
     */
    public boolean isEmpty() {
        return vertices.size() == 0;
    }


    /**
     * Get the vertex count.
     *
     * @return the number of verticies in the graph.
     */
    public int size() {
        return vertices.size();
    }

    /**
     * Get the root vertex
     *
     * @return the root vertex if one is set, null if no vertex has been set as the root.
     */
    public Vertex<T> getRootVertex() {
        return rootVertex;
    }

    /**
     * Set a vertex vertex. If vertex does no exist in the graph it is added.
     *
     * @param vertex - the vertex to set as the vertex and optionally add if it
     *               does not exist in the graph.
     */
    public void setRootVertex(Vertex<T> vertex) {
        this.rootVertex = vertex;
        addVertex(vertex);
    }


    public boolean addVertex(String name, T data) {
        return addVertex(new Vertex<T>(name, data));
    }

    /**
     * Add a vertex to the graph
     *
     * @param v the Vertex to add
     * @return true if the vertex was added, false if it was already in the graph.
     */
    public boolean addVertex(Vertex<T> v) {
        if (!hasVertex(v.getName())) {
            vertices.put(v.getName(), v);
            return true;
        }
        return false;
    }

    public boolean removeVertex(String name) {
        Vertex vertex = getVertex(name);
        if (vertex != null) {
            return removeVertex(vertex);
        }
        return false;
    }


    /**
     * Remove a vertex from the graph
     *
     * @param v the Vertex to remove
     * @return true if the Vertex was removed
     */
    public boolean removeVertex(Vertex<T> v) {
        if (!hasVertex(v.getName())) {
            return false;
        }

        vertices.remove(v.getName());
        if (v == rootVertex) {
            rootVertex = null;
        }

        // Remove the edges associated with v
        for (int n = 0; n < v.getOutgoingEdgeCount(); n++) {
            Edge<T> e = v.getOutgoingEdge(n);
            v.remove(e);
            Vertex<T> to = e.getTo();
            to.remove(e);
            edges.remove(e);
        }
        for (int n = 0; n < v.getIncomingEdgeCount(); n++) {
            Edge<T> e = v.getIncomingEdge(n);
            v.remove(e);
            Vertex<T> predecessor = e.getFrom();
            predecessor.remove(e);
        }
        return true;
    }


    /**
     * Get the graph vertices
     *
     * @return the graph vertices
     */
    public List<Vertex<T>> getVertices() {
        return new ArrayList<Vertex<T>>(vertices.values());
    }

    public List<String> getVerticNames() {
        return Pipeline.of(getVertices()).map(new Function<Vertex<T>, String>() {
            @Override
            public String apply(Vertex<T> vertex) {
                return vertex.getName();
            }
        }).asList();
    }

    /**
     * Get the given Vertex.
     *
     * @param n the index [0, size()-1] of the Vertex to access
     * @return the nth Vertex
     */
    public Vertex<T> getVertex(int n) {
        return getVertices().get(n);
    }


    /**
     * Search the verticies for one with name.
     *
     * @param name - the vertex name
     * @return the first vertex with a matching name, null if no
     * matches are found
     */
    public Vertex<T> getVertex(String name) {
        return vertices.get(name);
    }

    public boolean hasVertex(String name) {
        return getVertex(name) != null;
    }

    /**
     * Search the verticies for one with data.
     *
     * @param data    - the vertex data to match
     * @param compare - the comparator to perform the match
     * @return the first vertex with a matching data, null if no
     * matches are found
     */
    public Vertex<T> findVertexByData(T data, Comparator<T> compare) {
        Vertex<T> match = null;
        for (Vertex<T> v : vertices.values()) {
            if (compare.compare(data, v.getData()) == 0) {
                match = v;
                break;
            }
        }
        return match;
    }

    public boolean addEdge(String from, String to) throws IllegalArgumentException {
        return this.addEdge(from, to, 0);
    }

    public boolean addEdge(String from, String to, int weight) throws IllegalArgumentException {
        return this.addEdge(from, to, null, weight);
    }

    public boolean addEdge(String from, String to, String label, int weight) throws IllegalArgumentException {
        if (!hasVertex(from)) {
            throw new IllegalArgumentException("from vertex " + from + " is not in graph");
        }
        if (!hasVertex(to)) {
            throw new IllegalArgumentException("to vertex " + to + " is not in graph");
        }
        return addEdge(getVertex(from), getVertex(to), label, weight);
    }

    public boolean addEdge(Vertex<T> from, Vertex<T> to) {
        return this.addEdge(from, to, 0);
    }

    public boolean addEdge(Vertex<T> from, Vertex<T> to, int weight) {
        return this.addEdge(from, to, null, weight);
    }

    /**
     * Insert a directed, weighted Edge<T> into the graph.
     *
     * @param from   - the Edge<T> starting vertex
     * @param to     - the Edge<T> ending vertex
     * @param weight - the Edge<T> weight
     * @return true if the Edge<T> was added, false if from already has this Edge<T>
     */
    public boolean addEdge(Vertex<T> from, Vertex<T> to, String label, int weight) {
        if (!hasVertex(from.getName())) {
            addVertex(from);
        }
        if (!hasVertex(to.getName())) {
            addVertex(to);
        }
        Edge<T> e = new Edge<T>(from, to, label, weight);
        if (from.findEdge(to) != null)
            return false;
        else {
            from.addEdge(e);
            to.addEdge(e);
            edges.add(e);
            return true;
        }
    }

    /**
     * Insert a bidirectional (两个方向的) Edge<T> in the graph
     *
     * @param from   - the Edge<T> starting vertex
     * @param to     - the Edge<T> ending vertex
     * @param weight - the Edge<T> weight/cost
     * @return true if edges between both nodes were added, false otherwise
     * @throws IllegalArgumentException if from/to are not verticies in
     *                                  the graph
     */
    public boolean addBiEdge(Vertex<T> from, Vertex<T> to, int weight) {
        return addEdge(from, to, weight) && addEdge(to, from, weight);
    }

    /**
     * Get the graph edges
     *
     * @return the graph edges
     */
    public List<Edge<T>> getEdges() {
        return Collects.newArrayList(this.edges);
    }

    public boolean removeEdge(String from, String to) {
        if (hasVertex(from) && hasVertex(to)) {
            return removeEdge(getVertex(from), getVertex(to));
        }
        return false;
    }

    /**
     * Remove an Edge<T> from the graph
     *
     * @param from - the Edge<T> starting vertex
     * @param to   - the Edge<T> ending vertex
     * @return true if the Edge<T> exists, false otherwise
     */
    public boolean removeEdge(Vertex<T> from, Vertex<T> to) {
        Edge<T> e = from.findEdge(to);
        if (e == null)
            return false;
        else {
            from.remove(e);
            to.remove(e);
            edges.remove(e);
            return true;
        }
    }

    public boolean hasEdge(String from, String to) {
        Vertex<T> fromVertex = getVertex(from);
        Vertex<T> toVertex = getVertex(to);
        if (fromVertex == null || toVertex == null) {
            return false;
        }
        return fromVertex.getOutgoingVertices().contains(toVertex);
    }

    public boolean hasBiEdge(String from, String to) {
        return hasEdge(from, to) && hasEdge(to, from);
    }


    public String toString() {
        StringJoiner joiner = new StringJoiner(",", "Graph[", "]");
        for (Vertex<T> v : vertices.values()) {
            joiner.add(v.toString());
        }
        return joiner.toString();
    }

}
