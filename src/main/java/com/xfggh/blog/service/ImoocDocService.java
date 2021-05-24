package com.xfggh.blog.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xfggh.blog.BlogApplication;
import com.xfggh.blog.entity.ImoocDoc;
import com.xfggh.blog.entity.ImoocDocExample;
import com.xfggh.blog.mapper.ImoocDocMapper;
import com.xfggh.blog.req.ImoocDocSaveReq;
import com.xfggh.blog.resp.CommonResp;
import com.xfggh.blog.resp.ImoocDocQueryResp;
import com.xfggh.blog.resp.PageResp;
import com.xfggh.blog.util.CopyUtil;
import com.xfggh.blog.util.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ImoocDocService {
    @Resource
    private ImoocDocMapper imoocDocMapper;

    @Resource
    private SnowFlake snowFlake;

    private static final Logger LOG = LoggerFactory.getLogger(BlogApplication.class);

    public List<ImoocDocQueryResp> list(long id){

        List<ImoocDoc> imoocDocList = new ArrayList<>();

        // 查询条件
        ImoocDocExample imoocDocExample = new ImoocDocExample();
        ImoocDocExample.Criteria criteria = imoocDocExample.createCriteria();
        criteria.andEbookIdEqualTo(id);
        imoocDocExample.setOrderByClause("sort asc");

        imoocDocList = imoocDocMapper.selectByExample(imoocDocExample);

        List<ImoocDocQueryResp> imoocDocRespList = new ArrayList<>();

        // 复制列表
        imoocDocRespList = CopyUtil.copyList(imoocDocList, ImoocDocQueryResp.class);

        return imoocDocRespList;
    }


    public CommonResp save(ImoocDocSaveReq imoocDocSaveReq){
        CommonResp commonResp = new CommonResp();
        try{
            ImoocDoc imoocDoc = CopyUtil.copy(imoocDocSaveReq, ImoocDoc.class);

            if(ObjectUtils.isEmpty(imoocDocSaveReq.getId())){
                imoocDoc.setId(snowFlake.nextId());
                imoocDocMapper.insert(imoocDoc);
            }else{
                imoocDocMapper.updateByPrimaryKey(imoocDoc);
            }
        }catch (Exception e){
            commonResp.setMessage(e.getMessage());
            commonResp.setSuccess(false);
            LOG.error(e.getMessage());
        }

        return commonResp;
    }

    public void delete(String ids){
        List idList = Arrays.asList(ids.split(","));

        ImoocDocExample imoocDocExample = new ImoocDocExample();
        ImoocDocExample.Criteria criteria = imoocDocExample.createCriteria();
        criteria.andIdIn(idList);

        imoocDocMapper.deleteByExample(imoocDocExample);
    }


}
