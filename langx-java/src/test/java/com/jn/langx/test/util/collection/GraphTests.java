package com.jn.langx.test.util.collection;

import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.collection.graph.DAG;
import com.jn.langx.util.collection.graph.Graphs;
import com.jn.langx.util.collection.graph.Vertex;
import com.jn.langx.util.function.Function;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class GraphTests {

    @Test
    public void test() {
        DAG dag = new DAG();
        dag.addVertex("A", "A");
        dag.addVertex("B1", "B1");
        dag.addVertex("B2", "B2");
        dag.addVertex("C1", "C1");
        dag.addVertex("C2", "C2");

        dag.addEdge("A", "B1");
        dag.addEdge("A", "B2");

        dag.addEdge("B1", "C1");
        dag.addEdge("B2", "C1");
        dag.addEdge("B1", "C2");
        dag.addEdge("B2", "C2");
        dag.addEdge("B2", "B1");

        System.out.println(dag);

        Assert.assertEquals(dag.hasVertex("a"), false);
        Assert.assertEquals(dag.getRootVertex(), null);
        System.out.println(dag.getVertexNames());
        System.out.println(dag.getVertices());
        System.out.println(dag.getEdges());


        System.out.println("============================");

        List<Vertex> vertices = Graphs.dfsSort(dag, "A");
        List<String> vertexNames = Pipeline.of(vertices).map(new Function<Vertex, String>() {
            @Override
            public String apply(Vertex input) {
                return input.getName();
            }
        }).asList();
        System.out.println("dfs A:\t" + vertexNames);

        vertices = Graphs.dfsSort(dag);
        vertexNames = Pipeline.of(vertices).map(new Function<Vertex, String>() {
            @Override
            public String apply(Vertex input) {
                return input.getName();
            }
        }).asList();
        System.out.println("dfs:\t" + vertexNames);

        System.out.println("============================");

        vertices = Graphs.tdfsSort(dag, "A");
        vertexNames = Pipeline.of(vertices).map(new Function<Vertex, String>() {
            @Override
            public String apply(Vertex input) {
                return input.getName();
            }
        }).asList();
        System.out.println("tdfs A:\t" + vertexNames);

        vertices = Graphs.tdfsSort(dag);
        vertexNames = Pipeline.of(vertices).map(new Function<Vertex, String>() {
            @Override
            public String apply(Vertex input) {
                return input.getName();
            }
        }).asList();
        System.out.println("tdfs:\t" + vertexNames);

        System.out.println("============================");

        vertices = Graphs.bfsSort(dag, "A");
        vertexNames = Pipeline.of(vertices).map(new Function<Vertex, String>() {
            @Override
            public String apply(Vertex input) {
                return input.getName();
            }
        }).asList();
        System.out.println("bfs A:\t" + vertexNames);

        vertices = Graphs.bfsSort(dag);
        vertexNames = Pipeline.of(vertices).map(new Function<Vertex, String>() {
            @Override
            public String apply(Vertex input) {
                return input.getName();
            }
        }).asList();
        System.out.println("bfs:\t" + vertexNames);

    }


}
