package com.jn.langx.util.collection.graph;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;

public interface VertexConsumer<T> {
    void accept(@NonNull Graph<T> graph, @NonNull Vertex<T> vertex, @Nullable Edge<T> edge);
}
