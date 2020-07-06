package com.jn.langx.util.pagination;

public class PagingRequest<E> {
    PagingResult<E> result;
    private Boolean count = null;
    // begin 1
    private int pageNo = 1;
    // pageSize < 0, the limit is Integer.MAX
    // pageSize =0, is Empty paging request, the limit is 0
    // pageSize > 0, the limit is pageSize
    private int pageSize;

    /**
     * Nothing to do, will not do query, the result is empty list
     */
    public boolean isEmptyRequest() {
        return this.pageSize == 0;
    }

    /**
     * Get all matched records with out paging limit
     */
    public boolean isGetAllRequest() {
        return this.pageSize < 0 && pageNo == 1;
    }

    public boolean isGetAllFromNonZeroOffsetRequest() {
        return this.pageSize < 0 && pageNo > 1;
    }

    public boolean isValidRequest() {
        return this.pageSize > 0 || isGetAllFromNonZeroOffsetRequest();
    }

    public PagingRequest limit(int pageNo, int pageSize) {
        return this.setPageNo(pageNo).setPageSize(pageSize);
    }

    public Boolean getCount() {
        return count;
    }

    public PagingRequest setCount(Boolean count) {
        this.count = count;
        return this;
    }

    public int getPageNo() {
        return pageNo;
    }

    public PagingRequest setPageNo(int pageNo) {
        this.pageNo = pageNo;
        return this;
    }

    public int getPageSize() {
        return pageSize;
    }

    public PagingRequest setPageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public long offset() {
        if (isEmptyRequest()) {
            return 0L;
        }
        if (isGetAllRequest()) {
            return 0L;
        }
        if (isGetAllFromNonZeroOffsetRequest()) {
            setPageSize(10);
        }
        if (isValidRequest()) {
            if (pageNo <= 0) {
                pageNo = 1;
            }
            return (pageNo - 1) * pageSize;
        }
        return -1;
    }
}
