package com.jn.langx.util.collection.table;

import com.jn.langx.util.collection.Collects;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Table<R, C, V> {
    protected Map<R, Map<C, V>> map;

    public Table() {
        this.map = new LinkedHashMap<R, Map<C, V>>();
    }


    public Table(Map<R, Map<C, V>> tableCells) {
        for (Map.Entry<R, Map<C, V>> rowEntry : tableCells.entrySet()) {
            Map<C, V> rowCells = rowEntry.getValue();
            addRow(rowEntry.getKey(), rowCells);
        }
    }

    public final void addRow(R rowKey, Map<C, V> row) {
        for (Map.Entry<C, V> entry : row.entrySet()) {
            setCell(rowKey, entry.getKey(), entry.getValue());
        }
    }

    public final void addRow(List<Cell<R, C, V>> rowCells) {
        for (Cell<R, C, V> cell : rowCells) {
            if (cell != null) {
                setCell(cell.getRowKey(), cell.getColumnName(), cell.getValue());
            }
        }
    }

    public final void addRow(Cell<R, C, V>... rowCells) {
        addRow(Collects.asList(rowCells));
    }

    public final void setCell(R rowKey, C columnName, V value) {
        Map<C, V> row = getRow(rowKey, true);
        row.put(columnName, value);
    }

    public final void setCell(Cell<R, C, V> cell) {
        setCell(cell.getRowKey(), cell.getColumnName(), cell.getValue());
    }

    public final Cell<R, C, V> getCell(R rowKey, C columnName) {
        Map<C, V> rowCells = getRow(rowKey, false);
        if (rowCells == null) {
            return null;
        }
        if (rowCells.containsKey(columnName)) {
            return new Cell<R, C, V>(rowKey, columnName, rowCells.get(columnName));
        }
        return null;
    }

    public final V getCellValue(R rowKey, C columnName) {
        Cell<R, C, V> cell = getCell(rowKey, columnName);
        if (cell == null) {
            return null;
        }
        return cell.getValue();
    }

    public final void removeRow(R rowKey) {
        map.remove(rowKey);
    }

    public final boolean hasRow(R rowKey) {
        return map.containsKey(rowKey);
    }

    public final void removeCell(R rowKey, C columnName) {
        Map<C, V> row = getRow(rowKey, false);
        if (row != null) {
            row.remove(columnName);
        }
    }

    public List<Cell<R, C, V>> getRow(R rowKey) {
        Map<C, V> row = getRow(rowKey, false);
        if (row == null) {
            return null;
        }
        List<Cell<R, C, V>> cells = Collects.newArrayList();
        for (Map.Entry<C, V> entry : row.entrySet()) {
            cells.add(new Cell<R, C, V>(rowKey, entry.getKey(), entry.getValue()));
        }
        return cells;
    }

    /**
     * 获取行
     *
     * @param rowKey         key
     * @param createIfAbsent 是否创建
     * @return 行
     */
    protected final Map<C, V> getRow(R rowKey, boolean createIfAbsent) {
        Map<C, V> row = map.get(rowKey);
        if (row == null && createIfAbsent) {
            row = createEmptyRow();
            map.put(rowKey, row);
        }
        return row;
    }

    public void clear() {
        this.map.clear();
    }

    public int rows() {
        return map.size();
    }

    protected Map<C, V> createEmptyRow() {
        return new LinkedHashMap<C, V>();
    }
}
