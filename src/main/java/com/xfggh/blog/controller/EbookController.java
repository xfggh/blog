package com.xfggh.blog.controller;

import com.xfggh.blog.req.EbookQueryReq;
import com.xfggh.blog.req.EbookSaveReq;
import com.xfggh.blog.resp.EbookQueryResp;
import com.xfggh.blog.resp.PageResp;
import com.xfggh.blog.service.EbookService;
import com.xfggh.blog.resp.CommonResp;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/ebook")
public class EbookController {

    @Resource
    private EbookService ebookService;

    @GetMapping("/list")
    public CommonResp<PageResp<EbookQueryResp>> listBy(@Valid EbookQueryReq ebookReq) {
        CommonResp<PageResp<EbookQueryResp>> commonResp = new CommonResp<>();

        PageResp<EbookQueryResp> pageResp = ebookService.list(ebookReq);
        commonResp.setContent(pageResp);

        return commonResp;
    }

    @GetMapping("/all")
    public CommonResp<List<EbookQueryResp>> allEbookList(EbookQueryReq ebookReq) {
        CommonResp<List<EbookQueryResp>> commonResp = new CommonResp<>();

        List<EbookQueryResp> list = ebookService.allEbookList(ebookReq);
        commonResp.setContent(list);

        return commonResp;
    }

    @PostMapping("/save")
    public CommonResp save(@Valid @RequestBody EbookSaveReq ebookSaveReq){
        CommonResp commonResp = new CommonResp();
        commonResp = ebookService.save(ebookSaveReq);
        return commonResp;
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp delete(@PathVariable long id){
        CommonResp commonResp = new CommonResp();
        ebookService.delete(id);
        return commonResp;
    }
}
