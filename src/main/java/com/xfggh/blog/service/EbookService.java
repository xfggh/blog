package com.xfggh.blog.service;

import com.xfggh.blog.entity.Ebook;
import com.xfggh.blog.entity.EbookExample;
import com.xfggh.blog.mapper.EbookMapper;
import com.xfggh.blog.req.EbookReq;
import com.xfggh.blog.resp.CommonResp;
import com.xfggh.blog.resp.EbookResp;
import com.xfggh.blog.util.CopyUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

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

        List<Ebook> ebookList = new ArrayList<>();
        String name = ebookReq.getName();

        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria =  ebookExample.createCriteria();

        // criteria 相当于 where 条件
        if(!ObjectUtils.isEmpty(ebookReq.getName())){
            criteria.andNameLike("%" + ebookReq.getName() + "%");
        }

        ebookList = ebookMapper.selectByExample(ebookExample);

        List<EbookResp> ebookRespList = new ArrayList<>();
        /*for (Ebook ebook : ebookList) {
            //EbookResp ebookResp = new EbookResp();
            //BeanUtils.copyProperties(ebook, ebookResp);

            // 复制一个对象
            EbookResp ebookResp = CopyUtil.copy(ebook, EbookResp.class);
            ebookResp.setCategory1Id(123L);
            ebookRespList.add(ebookResp);
        }*/

        // 复制列表
        ebookRespList = CopyUtil.copyList(ebookList, EbookResp.class);

        resp.setContent(ebookRespList);
        return resp;
    }
}
