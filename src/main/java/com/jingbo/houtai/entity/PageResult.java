package com.jingbo.houtai.entity;

import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class PageResult<T> implements Serializable {
    private Long totalPages;// 总页数
    private Long totalRecords;// 总记录数
    private Integer pageNo;// 当前页号
    private Integer pageSize;// 每页大小

    private List<T> results;// 查询结果
}
