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

    @GetMapping("/list/{id}")
    public CommonResp<List<ImoocDocQueryResp>> list(@PathVariable long id) {
        CommonResp<List<ImoocDocQueryResp>> commonResp = new CommonResp<>();

        List<ImoocDocQueryResp> imoocDocQueryRespList = imoocDocService.list(id);
        commonResp.setContent(imoocDocQueryRespList);

        return commonResp;
    }

    @PostMapping("/save")
    public CommonResp save(@Valid @RequestBody ImoocDocSaveReq imoocDocSaveReq){
        CommonResp commonResp = new CommonResp();
        commonResp = imoocDocService.save(imoocDocSaveReq);
        return commonResp;
    }

    @DeleteMapping("/delete/{ids}")
    public CommonResp delete(@PathVariable String ids){
        CommonResp commonResp = new CommonResp();
        imoocDocService.delete(ids);
        return commonResp;
    }

    @GetMapping("/content/{id}")
    public CommonResp getContent(@PathVariable long id){
        CommonResp commonResp = new CommonResp();

        String content = imoocDocService.getContent(id);
        commonResp.setContent(content);
        return commonResp;
    }

}
