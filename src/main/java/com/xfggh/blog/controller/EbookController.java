package com.xfggh.blog.controller;

import com.xfggh.blog.req.EbookReq;
import com.xfggh.blog.resp.EbookResp;
import com.xfggh.blog.resp.PageResp;
import com.xfggh.blog.service.EbookService;
import com.xfggh.blog.resp.CommonResp;
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
    public CommonResp<PageResp<EbookResp>> listBy(EbookReq ebookReq) {
        CommonResp<PageResp<EbookResp>> commonResp = new CommonResp<>();

        PageResp<EbookResp> pageResp = ebookService.list(ebookReq);
        commonResp.setContent(pageResp);

        return commonResp;
    }

    @GetMapping("/all")
    public CommonResp<List<EbookResp>> allEbookList(EbookReq ebookReq) {
        CommonResp<List<EbookResp>> commonResp = new CommonResp<>();

        List<EbookResp> list = ebookService.allEbookList(ebookReq);
        commonResp.setContent(list);

        return commonResp;
    }
}
