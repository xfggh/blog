package com.xfggh.blog.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xfggh.blog.BlogApplication;
import com.xfggh.blog.entity.Ebook;
import com.xfggh.blog.entity.EbookExample;
import com.xfggh.blog.mapper.EbookMapper;
import com.xfggh.blog.req.EbookReq;
import com.xfggh.blog.resp.CommonResp;
import com.xfggh.blog.resp.EbookResp;
import com.xfggh.blog.util.CopyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class EbookService {
    @Resource
    private EbookMapper ebookMapper;

    private static final Logger LOG = LoggerFactory.getLogger(BlogApplication.class);

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

        // 分页 | 只对遇到的第一个 select 起作用
        PageHelper.startPage(1, 3);

        ebookList = ebookMapper.selectByExample(ebookExample);
        PageInfo<Ebook> pageInfo = new PageInfo<>(ebookList);
        LOG.info("分页总条数 {}", pageInfo.getTotal());
        LOG.info("分页总页数 {}", pageInfo.getPages());

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
