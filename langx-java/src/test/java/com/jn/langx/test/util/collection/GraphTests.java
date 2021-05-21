package com.jn.langx.test.util.collection;

import com.jn.langx.util.collection.graph.DAG;
import org.junit.Test;

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

        System.out.println(dag);
    }


}
