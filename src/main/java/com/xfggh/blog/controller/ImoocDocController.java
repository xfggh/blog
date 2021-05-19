package com.xfggh.blog.controller;

import com.xfggh.blog.req.ImoocDocSaveReq;
import com.xfggh.blog.resp.ImoocDocQueryResp;
import com.xfggh.blog.resp.CommonResp;
import com.xfggh.blog.service.ImoocDocService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/imoocDoc")
public class ImoocDocController {

    @Resource
    private ImoocDocService imoocDocService;

    @GetMapping("/list")
    public CommonResp<List<ImoocDocQueryResp>> list() {
        CommonResp<List<ImoocDocQueryResp>> commonResp = new CommonResp<>();

        List<ImoocDocQueryResp> imoocDocQueryRespList = imoocDocService.list();
        commonResp.setContent(imoocDocQueryRespList);

        return commonResp;
    }

    @PostMapping("/save")
    public CommonResp save(@Valid @RequestBody ImoocDocSaveReq imoocDocSaveReq){
        CommonResp commonResp = new CommonResp();
        commonResp = imoocDocService.save(imoocDocSaveReq);
        return commonResp;
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp delete(@PathVariable long id){
        CommonResp commonResp = new CommonResp();
        imoocDocService.delete(id);
        return commonResp;
    }
}
