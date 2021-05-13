package com.xfggh.blog.req;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

public class PageReq {
    // @NotNull(message = "【页码】不能为空")
    private int pageNum;

    // @NotNull(message = "【每条页数】不能为空")
    @Max(value = 1000, message = "【每条页数】不能超过1000")
    private int pageSize;

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
