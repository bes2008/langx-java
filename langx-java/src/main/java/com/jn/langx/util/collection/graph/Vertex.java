package com.jn.langx.util.collection.graph;


import com.jn.langx.AbstractNamed;
import com.jn.langx.annotation.Nullable;
import com.jn.langx.util.Objs;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.function.Function;
import com.jn.langx.util.hash.HashCodeBuilder;

import java.util.List;

/**
 * A named graph vertex with optional data.
 *
 * @param <T>
 */
public class Vertex<T> extends AbstractNamed {
    private List<Edge<T>> incomingEdges = Collects.emptyArrayList();
    private List<Edge<T>> outgoingEdges = Collects.emptyArrayList();
    @Nullable
    private T data;

    /**
     * Calls this(null, null).
     */
    public Vertex() {
        this(null, null);
    }

    /**
     * Create a vertex with the given name and no data
     *
     * @param n
     */
    public Vertex(String n) {
        this(n, null);
    }

    /**
     * Create a Vertex with name n and given data
     *
     * @param n    - name of vertex
     * @param data - data associated with vertex
     */
    public Vertex(String n, T data) {
        setName(n);
        setData(data);
    }


    /**
     * @return the possibly null data of the vertex
     */
    public T getData() {
        return this.data;
    }

    /**
     * @param data The data to set.
     */
    public void setData(T data) {
        this.data = data;
    }

    /**
     * Add an edge to the vertex. If edge.from is this vertex, its an
     * outgoing edge. If edge.to is this vertex, its an incoming
     * edge. If neither from or to is this vertex, the edge is
     * not added.
     *
     * @param e - the edge to add
     * @return true if the edge was added, false otherwise
     */
    public boolean addEdge(Edge<T> e) {
        if (e.getFrom() == this) {
            outgoingEdges.add(e);
        } else if (e.getTo() == this) {
            incomingEdges.add(e);
        } else {
            return false;
        }
        return true;
    }

    /**
     * Add an outgoing edge ending at to.
     *
     * @param to     - the destination vertex
     * @param weight the edge weight
     */
    public void addOutgoingEdge(Vertex<T> to, int weight) {
        Edge<T> out = new Edge<T>(this, to, weight);
        addEdge(out);
    }

    /**
     * Add an incoming edge starting at from
     *
     * @param from   - the starting vertex
     * @param weight the edge weight
     */
    public void addIncomingEdge(Vertex<T> from, int weight) {
        Edge<T> incoming = new Edge<T>(from, this, weight);
        addEdge(incoming);
    }

    /**
     * Check the vertex for either an incoming or outgoing edge
     * mathcing e.
     *
     * @param e the edge to check
     * @return true it has an edge
     */
    public boolean hasEdge(Edge<T> e) {
        if (e.getFrom() == this) {
            return outgoingEdges.contains(e);
        } else if (e.getTo() == this) {
            return incomingEdges.contains(e);
        } else {
            return false;
        }
    }

    /**
     * Remove an edge from this vertex
     *
     * @param e - the edge to remove
     * @return true if the edge was removed, false if the
     * edge was not connected to this vertex
     */
    public boolean remove(Edge<T> e) {
        if (e.getFrom() == this) {
            outgoingEdges.remove(e);
        } else if (e.getTo() == this) {
            incomingEdges.remove(e);
        } else {
            return false;
        }
        return true;
    }

    /**
     * @return the count of incoming edges
     */
    public int getIncomingEdgeCount() {
        return incomingEdges.size();
    }

    /**
     * Get the ith incoming edge
     *
     * @param i the index into incoming edges
     * @return ith incoming edge
     */
    public Edge<T> getIncomingEdge(int i) {
        return incomingEdges.get(i);
    }

    /**
     * Get the incoming edges
     *
     * @return incoming edge list
     */
    public List<Edge<T>> getIncomingEdges() {
        return this.incomingEdges;
    }

    public List<Vertex<T>> getIncomingVertices() {
        List<Edge<T>> edges = getIncomingEdges();
        return Pipeline.of(edges).map(new Function<Edge<T>, Vertex<T>>() {
            @Override
            public Vertex<T> apply(Edge<T> edge) {
                return edge.getFrom();
            }
        }).asList();
    }

    public List<String> getIncomingVertexNames(){
        return Pipeline.of(getIncomingVertices()).map(new Function<Vertex<T>, String>() {
            @Override
            public String apply(Vertex<T> vertex) {
                return vertex.getName();
            }
        }).asList();
    }

    /**
     * @return the count of incoming edges
     */
    public int getOutgoingEdgeCount() {
        return outgoingEdges.size();
    }

    /**
     * Get the ith outgoing edge
     *
     * @param i the index into outgoing edges
     * @return ith outgoing edge
     */
    public Edge<T> getOutgoingEdge(int i) {
        return outgoingEdges.get(i);
    }

    /**
     * Get the outgoing edges
     *
     * @return outgoing edge list
     */
    public List<Edge<T>> getOutgoingEdges() {
        return this.outgoingEdges;
    }


    public List<Vertex<T>> getOutgoingVertices() {
        List<Edge<T>> edges = getIncomingEdges();
        return Pipeline.of(edges).map(new Function<Edge<T>, Vertex<T>>() {
            @Override
            public Vertex<T> apply(Edge<T> edge) {
                return edge.getTo();
            }
        }).asList();
    }

    public List<String> getOutgoingVertexNames(){
        return Pipeline.of(getOutgoingVertices()).map(new Function<Vertex<T>, String>() {
            @Override
            public String apply(Vertex<T> vertex) {
                return vertex.getName();
            }
        }).asList();
    }

    /**
     * Search the outgoing edges looking for an edge whose's
     * edge.to == dest.
     *
     * @param dest the destination
     * @return the outgoing edge going to dest if one exists,
     * null otherwise.
     */
    public Edge<T> findEdge(Vertex<T> dest) {
        for (Edge<T> e : outgoingEdges) {
            if (e.getTo() == dest) {
                return e;
            }
        }
        return null;
    }

    /**
     * Search the outgoing edges for a match to e.
     *
     * @param e - the edge to check
     * @return e if its a member of the outgoing edges, null
     * otherwise.
     */
    public Edge<T> findEdge(Edge<T> e) {
        if (outgoingEdges.contains(e)) {
            return e;
        } else {
            return null;
        }
    }

    /**
     * What is the cost from this vertex to the dest vertex.
     *
     * @param dest - the destination vertex.
     * @return Return Integer.MAX_VALUE if we have no edge to dest,
     * 0 if dest is this vertex, the cost of the outgoing edge
     * otherwise.
     */
    public int weight(Vertex<T> dest) {
        if (dest == this) {
            return 0;
        }

        Edge<T> e = findEdge(dest);
        int weight = Integer.MAX_VALUE;
        if (e != null) {
            weight = e.getWeight();
        }
        return weight;
    }

    /**
     * Is there an outgoing edge ending at dest.
     *
     * @param dest - the vertex to check
     * @return true if there is an outgoing edge ending`
     * at vertex, false otherwise.
     */
    public boolean hasEdge(Vertex<T> dest) {
        return findEdge(dest) != null;
    }

    public boolean isRoot() {
        return getIncomingEdgeCount() == 0;
    }

    public boolean isLeaf() {
        return getOutgoingEdgeCount() == 0;
    }

    public boolean isIsolated() {
        return isRoot() && isLeaf();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vertex<?> vertex = (Vertex<?>) o;
        return Objs.equals(this.name, vertex.name);
    }


    @Override
    public int hashCode() {
        return new HashCodeBuilder().with(name).with(data).build();
    }

    /**
     * @return a string form of the vertex with in and out
     * edges.
     */
    public String toString() {
        StringBuilder tmp = new StringBuilder("Vertex(");
        tmp.append(name);
        tmp.append(", data=");
        tmp.append(data);
        tmp.append("), in:[");
        for (int i = 0; i < incomingEdges.size(); i++) {
            Edge<T> e = incomingEdges.get(i);
            if (i > 0) {
                tmp.append(',');
            }
            tmp.append('{');
            tmp.append(e.getFrom().name);
            tmp.append(',');
            tmp.append(e.getWeight());
            tmp.append('}');
        }
        tmp.append("], out:[");
        for (int i = 0; i < outgoingEdges.size(); i++) {
            Edge<T> e = outgoingEdges.get(i);
            if (i > 0) {
                tmp.append(',');
            }
            tmp.append('{');
            tmp.append(e.getTo().name);
            tmp.append(',');
            tmp.append(e.getWeight());
            tmp.append('}');
        }
        tmp.append(']');
        return tmp.toString();
    }
}
