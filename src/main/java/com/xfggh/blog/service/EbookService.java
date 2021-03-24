package com.xfggh.blog.service;

import com.xfggh.blog.entity.Ebook;
import com.xfggh.blog.mapper.EbookMapper;
import com.xfggh.blog.util.CommonResp;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class EbookService {
    @Resource
    private EbookMapper ebookMapper;

    public CommonResp<List<Ebook>> list(){
        CommonResp resp = new CommonResp();
        resp.setMessage("查询列表成功");
        resp.setContent(ebookMapper.selectByExample(null));
        return resp;
    }
}
