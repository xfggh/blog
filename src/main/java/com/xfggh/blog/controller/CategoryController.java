package com.xfggh.blog.controller;

import com.xfggh.blog.req.CategorySaveReq;
import com.xfggh.blog.resp.CommonResp;
import com.xfggh.blog.resp.CategoryQueryResp;
import com.xfggh.blog.resp.PageResp;
import com.xfggh.blog.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Resource
    private CategoryService categoryService;

    @GetMapping("/list")
    public CommonResp<List<CategoryQueryResp>> list() {
        CommonResp<List<CategoryQueryResp>> commonResp = new CommonResp<>();

        List<CategoryQueryResp> categoryQueryRespList = categoryService.list();
        commonResp.setContent(categoryQueryRespList);

        return commonResp;
    }

    @PostMapping("/save")
    public CommonResp save(@Valid @RequestBody CategorySaveReq categorySaveReq){
        CommonResp commonResp = new CommonResp();
        commonResp = categoryService.save(categorySaveReq);
        return commonResp;
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp delete(@PathVariable long id){
        CommonResp commonResp = new CommonResp();
        categoryService.delete(id);
        return commonResp;
    }
}
