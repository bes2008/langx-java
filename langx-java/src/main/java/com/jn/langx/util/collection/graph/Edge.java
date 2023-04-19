package com.jn.langx.util.collection.graph;


import com.jn.langx.text.StringTemplates;

/**
 * A directed, weighted edge in a graph
 */
public class Edge<T> {
    private Vertex<T> from;
    private Vertex<T> to;
    private String label;

    private int weight;

    /**
     * Create a zero cost edge between from and to
     *
     * @param from the starting vertex
     * @param to   the ending vertex
     */
    public Edge(Vertex<T> from, Vertex<T> to) {
        this(from, to, 0);
    }

    public Edge(Vertex<T> from, Vertex<T> to, String label) {
        this(from, to, label, 0);
    }

    public Edge(Vertex<T> from, Vertex<T> to, int weight) {
        this(from, to, null, weight);
    }

    /**
     * Create an edge between from and to with the given cost.
     *
     * @param from   the starting vertex
     * @param to     the ending vertex
     * @param weight the weight of the edge
     */
    public Edge(Vertex<T> from, Vertex<T> to, String label, int weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
        setLabel(label);
    }

    /**
     * Get the ending vertex
     *
     * @return ending vertex
     */
    public Vertex<T> getTo() {
        return to;
    }

    /**
     * Get the starting vertex
     *
     * @return starting vertex
     */
    public Vertex<T> getFrom() {
        return from;
    }

    /**
     * Get the cost of the edge
     *
     * @return cost of the edge
     */
    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }


    /**
     * String rep of edge
     *
     * @return string rep with from/to vertex names and cost
     */
    public String toString() {
        return StringTemplates.formatWithPlaceholder("from: {}, to: {}, label: {}, weight:{}", from.getName(), to.getName(), label, weight);
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
