package com.xfggh.blog.req;

/**
 * 请求参数类
 */
public class EbookQueryReq extends PageReq {
    private Long id;

    private String name;

    private Long categoryId;
    private int type; // 区分 分类一 id 和 分类二 id

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
