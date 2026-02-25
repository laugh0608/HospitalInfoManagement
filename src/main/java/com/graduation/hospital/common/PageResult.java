package com.graduation.hospital.common;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 分页结果封装
 */
@Data
public class PageResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<T> content;
    private long totalElements;
    private int totalPages;
    private int pageNumber;
    private int pageSize;
    private boolean first;
    private boolean last;

    public PageResult() {
    }

    public PageResult(List<T> content, long totalElements, int pageNumber, int pageSize) {
        this.content = content;
        this.totalElements = totalElements;
        this.totalPages = (int) Math.ceil((double) totalElements / pageSize);
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.first = pageNumber == 0;
        this.last = pageNumber >= totalPages - 1;
    }

    public static <T> PageResult<T> of(List<T> content, long totalElements, int pageNumber, int pageSize) {
        return new PageResult<>(content, totalElements, pageNumber, pageSize);
    }
}