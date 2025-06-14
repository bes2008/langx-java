package com.jn.langx.util.pagination;

import java.util.List;
@Deprecated
public class PagingResult<E>{
    private int pageNo;
    private int pageSize;
    private long total;
    private List<E> items;

    public int getPageNo() {
        return this.pageNo;
    }

    public PagingResult<E> setPageNo(int pageNo) {
        this.pageNo = pageNo;
        return this;
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public PagingResult<E> setPageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public long getTotal() {
        return this.total;
    }

    public PagingResult<E> setTotal(long total) {
        this.total = total;
        return this;
    }

    public List<E> getItems() {
        return this.items;
    }

    public PagingResult<E> setItems(List<E> items) {
        this.items = items;
        return this;
    }

    public int getMaxPage() {
        return (int)getMaxPageCount(pageSize);
    }

    public long getMaxPageCount(int pageSize) {
        // unknown
        if (this.total < 0) {
            return -1;
        }
        if ((this.total == 0) || (this.pageSize == 0)) {
            return 0;
        }
        if (pageSize < 0) {
            return -1;
        }
        return this.total / this.pageSize + (this.total % this.pageSize == 0 ? 0 : 1);
    }
}
