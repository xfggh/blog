package com.xfggh.blog.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xfggh.blog.BlogApplication;
import com.xfggh.blog.entity.User;
import com.xfggh.blog.entity.UserExample;
import com.xfggh.blog.mapper.UserMapper;
import com.xfggh.blog.req.UserQueryReq;
import com.xfggh.blog.req.UserSaveReq;
import com.xfggh.blog.resp.CommonResp;
import com.xfggh.blog.resp.UserQueryResp;
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
public class UserService {
    @Resource
    private UserMapper userMapper;

    @Resource
    private SnowFlake snowFlake;

    private static final Logger LOG = LoggerFactory.getLogger(BlogApplication.class);

    public PageResp<UserQueryResp> list(UserQueryReq userReq){
        PageResp<UserQueryResp> pageResp = new PageResp<>();

        List<User> userList = new ArrayList<>();

        UserExample userExample = new UserExample();
        UserExample.Criteria criteria =  userExample.createCriteria();

        // criteria 相当于 where 条件
        if(!ObjectUtils.isEmpty(userReq.getLoginName())){
            criteria.andLoginNameEqualTo(userReq.getLoginName());
        }

        // 分页 | 只对遇到的第一个 select 起作用
        PageHelper.startPage(userReq.getPageNum(), userReq.getPageSize());

        userList = userMapper.selectByExample(userExample);
        // 分页信息
        PageInfo<User> pageInfo = new PageInfo<>(userList);
        pageResp.setTotal(pageInfo.getTotal());

        List<UserQueryResp> userRespList = new ArrayList<>();

        // 复制列表
        userRespList = CopyUtil.copyList(userList, UserQueryResp.class);
        pageResp.setList(userRespList);

        return pageResp;
    }

    public CommonResp save(UserSaveReq userSaveReq){
        CommonResp commonResp = new CommonResp();

        User user = CopyUtil.copy(userSaveReq, User.class);

        if(ObjectUtils.isEmpty(userSaveReq.getId())){
            user.setId(snowFlake.nextId());
            userMapper.insert(user);
        }else{
            userMapper.updateByPrimaryKey(user);
        }

        return commonResp;
    }

    public void delete(long id){
        userMapper.deleteByPrimaryKey(id);
    }


}
