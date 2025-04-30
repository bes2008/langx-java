package com.jn.langx.util.collection.table;

import com.jn.langx.util.Preconditions;

public final class Cell<R, C, V> {
    private R rowKey;
    private C columnName;
    private V value;

    public Cell(R rowKey, C columnName, V value) {
        Preconditions.checkNotNull(rowKey);
        Preconditions.checkNotNull(columnName);
        this.rowKey = rowKey;
        this.columnName = columnName;
        this.value = value;
    }

    public R getRowKey() {
        return rowKey;
    }

    public C getColumnName() {
        return columnName;
    }

    public V getValue() {
        return value;
    }
}
