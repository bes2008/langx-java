package com.jn.langx.util.collection.graph;

import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Supplier;

import java.util.Map;

public class Graphs {
    public final static Integer NOT_VISITED = 0;
    public final static Integer VISITING = 1;
    public final static Integer VISITED = 2;

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

    public static boolean isNotVisited(Map<String, VisitStatus> statusMap, String name){
        return getVisitStatus(statusMap,name) == VisitStatus.NOT_VISITED;
    }

    public static void beginVisit(Map<String, VisitStatus> statusMap, String name){
        statusMap.put(name, VisitStatus.VISITING);
    }

    public static void finishVisit(Map<String, VisitStatus> statusMap, String name){
        statusMap.put(name, VisitStatus.VISITED);
    }
}
