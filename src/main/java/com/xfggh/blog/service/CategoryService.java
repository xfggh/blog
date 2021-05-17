package com.xfggh.blog.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xfggh.blog.BlogApplication;
import com.xfggh.blog.entity.Category;
import com.xfggh.blog.entity.CategoryExample;
import com.xfggh.blog.mapper.CategoryMapper;
import com.xfggh.blog.req.CategorySaveReq;
import com.xfggh.blog.resp.CommonResp;
import com.xfggh.blog.resp.CategoryQueryResp;
import com.xfggh.blog.resp.PageResp;
import com.xfggh.blog.util.CopyUtil;
import com.xfggh.blog.util.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService {
    @Resource
    private CategoryMapper categoryMapper;

    @Resource
    private SnowFlake snowFlake;

    private static final Logger LOG = LoggerFactory.getLogger(BlogApplication.class);

    public List<CategoryQueryResp> list(){

        List<Category> categoryList = new ArrayList<>();

        // 查询条件
        CategoryExample categoryExample = new CategoryExample();
        // categoryExample.setOrderByClause("");

        categoryList = categoryMapper.selectByExample(categoryExample);

        List<CategoryQueryResp> categoryRespList = new ArrayList<>();

        // 复制列表
        categoryRespList = CopyUtil.copyList(categoryList, CategoryQueryResp.class);

        return categoryRespList;
    }


    public CommonResp save(CategorySaveReq categorySaveReq){
        CommonResp commonResp = new CommonResp();
        try{
            Category category = CopyUtil.copy(categorySaveReq, Category.class);

            if(ObjectUtils.isEmpty(categorySaveReq.getId())){
                category.setId(snowFlake.nextId());
                categoryMapper.insert(category);
            }else{
                categoryMapper.updateByPrimaryKey(category);
            }
        }catch (Exception e){
            commonResp.setMessage(e.getMessage());
            commonResp.setSuccess(false);
            LOG.error(e.getMessage());
        }

        return commonResp;
    }

    public void delete(long id){
        categoryMapper.deleteByPrimaryKey(id);
    }


}
