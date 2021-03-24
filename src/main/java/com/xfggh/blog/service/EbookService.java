package com.xfggh.blog.service;

import com.xfggh.blog.entity.Ebook;
import com.xfggh.blog.entity.EbookExample;
import com.xfggh.blog.mapper.EbookMapper;
import com.xfggh.blog.req.EbookReq;
import com.xfggh.blog.resp.CommonResp;
import com.xfggh.blog.resp.EbookResp;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class EbookService {
    @Resource
    private EbookMapper ebookMapper;

    public CommonResp<List<EbookResp>> list(EbookReq ebookReq){
        CommonResp resp = new CommonResp();
        resp.setMessage("查询列表成功");

        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria =  ebookExample.createCriteria();

        // criteria 相当于 where 条件
        criteria.andNameLike("%" + ebookReq.getName() + "%");

        List<Ebook> ebookList = ebookMapper.selectByExample(ebookExample);
        List<EbookResp> ebookRespList = new ArrayList<>();
        for (Ebook ebook : ebookList) {
            EbookResp ebookResp = new EbookResp();
            BeanUtils.copyProperties(ebook, ebookResp);

            ebookRespList.add(ebookResp);
        }


        resp.setContent(ebookRespList);
        return resp;
    }
}
