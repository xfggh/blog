package com.xfggh.blog.controller;

import com.xfggh.blog.entity.Ebook;
import com.xfggh.blog.service.EbookService;
import com.xfggh.blog.util.CommonResp;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/ebook")
public class EbookController {

    @Resource
    private EbookService ebookService;

    @GetMapping("/list")
    public CommonResp<List<Ebook>> list() {
        return ebookService.list();
    }
}
