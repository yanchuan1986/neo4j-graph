package com.dx.graph.neo4j.web.common;

/**
 * Created by wuling on 2017/5/17.
 */
public class PageParam extends CallbackParam {

    private Integer pageNo = 1; // 当前第几页
    private Integer pageSize = 10; // 一页展示多少条数
    private Integer itemCount; // 总记录数
    private Integer pageCount; // 总页数
    private Integer startOffset;

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        if(pageNo < 1)
            pageNo = 1;
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        if (pageSize > 200)
            pageSize = 200;
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        if(pageSize < 1)
            pageSize = 1;
        this.pageSize = pageSize;
    }

    public Integer getItemCount() {
        return itemCount;
    }

    public void setItemCount(Integer itemCount) {
        this.itemCount = itemCount;
    }

    public Integer getPageCount() {
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    public Integer getStartOffset() {
        setStartOffset(computeStartOffset());
        return startOffset;
    }

    public void setStartOffset(Integer startOffset) {
        this.startOffset = startOffset;
    }

    private Integer computeStartOffset() {
        return (pageNo - 1) * pageSize;
    }

}
