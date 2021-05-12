package com.xfggh.blog.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xfggh.blog.BlogApplication;
import com.xfggh.blog.entity.Ebook;
import com.xfggh.blog.entity.EbookExample;
import com.xfggh.blog.mapper.EbookMapper;
import com.xfggh.blog.req.EbookQueryReq;
import com.xfggh.blog.req.EbookSaveReq;
import com.xfggh.blog.resp.CommonResp;
import com.xfggh.blog.resp.EbookQueryResp;
import com.xfggh.blog.resp.PageResp;
import com.xfggh.blog.util.CopyUtil;
import com.xfggh.blog.util.SnowFlake;
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

    @Resource
    private SnowFlake snowFlake;

    private static final Logger LOG = LoggerFactory.getLogger(BlogApplication.class);

    public PageResp<EbookQueryResp> list(EbookQueryReq ebookReq){
        PageResp<EbookQueryResp> pageResp = new PageResp<>();

        List<Ebook> ebookList = new ArrayList<>();

        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria =  ebookExample.createCriteria();

        // criteria 相当于 where 条件
        if(!ObjectUtils.isEmpty(ebookReq.getName())){
            criteria.andNameLike("%" + ebookReq.getName() + "%");
        }

        // 分页 | 只对遇到的第一个 select 起作用
        PageHelper.startPage(ebookReq.getPageNum(), ebookReq.getPageSize());

        ebookList = ebookMapper.selectByExample(ebookExample);
        // 分页信息
        PageInfo<Ebook> pageInfo = new PageInfo<>(ebookList);
        pageResp.setTotal(pageInfo.getTotal());
        // LOG.info("分页总条数 {}", pageInfo.getTotal());
        // LOG.info("分页总页数 {}", pageInfo.getPages());

        List<EbookQueryResp> ebookRespList = new ArrayList<>();
        /*for (Ebook ebook : ebookList) {
            //EbookResp ebookResp = new EbookResp();
            //BeanUtils.copyProperties(ebook, ebookResp);

            // 复制一个对象
            EbookResp ebookResp = CopyUtil.copy(ebook, EbookResp.class);
            ebookResp.setCategory1Id(123L);
            ebookRespList.add(ebookResp);
        }*/

        // 复制列表
        ebookRespList = CopyUtil.copyList(ebookList, EbookQueryResp.class);
        pageResp.setList(ebookRespList);

        return pageResp;
    }

    public List<EbookQueryResp> allEbookList(EbookQueryReq ebookReq){

        List<Ebook> ebookList = new ArrayList<>();

        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria =  ebookExample.createCriteria();

        ebookList = ebookMapper.selectByExample(ebookExample);

        List<EbookQueryResp> ebookRespList = new ArrayList<>();

        // 复制列表
        ebookRespList = CopyUtil.copyList(ebookList, EbookQueryResp.class);

        return ebookRespList;
    }

    public CommonResp save(EbookSaveReq ebookSaveReq){
        CommonResp commonResp = new CommonResp();
        try{
            Ebook ebook = CopyUtil.copy(ebookSaveReq, Ebook.class);

            if(ObjectUtils.isEmpty(ebookSaveReq.getId())){
                ebook.setId(snowFlake.nextId());
                ebookMapper.insert(ebook);
            }else{
                ebookMapper.updateByPrimaryKey(ebook);
            }
        }catch (Exception e){
            commonResp.setMessage(e.getMessage());
            commonResp.setSuccess(false);
            LOG.error(e.getMessage());
        }

        return commonResp;
    }

    public void delete(long id){
        ebookMapper.deleteByPrimaryKey(id);
    }


}
