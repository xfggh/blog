package com.xfggh.blog.controller;

import com.xfggh.blog.req.UserQueryReq;
import com.xfggh.blog.req.UserSaveReq;
import com.xfggh.blog.resp.CommonResp;
import com.xfggh.blog.resp.UserQueryResp;
import com.xfggh.blog.resp.PageResp;
import com.xfggh.blog.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @GetMapping("/list")
    public CommonResp<PageResp<UserQueryResp>> listBy(@Valid UserQueryReq userReq) {
        CommonResp<PageResp<UserQueryResp>> commonResp = new CommonResp<>();

        PageResp<UserQueryResp> pageResp = userService.list(userReq);
        commonResp.setContent(pageResp);

        return commonResp;
    }

    @PostMapping("/save")
    public CommonResp save(@Valid @RequestBody UserSaveReq userSaveReq){
        CommonResp commonResp = new CommonResp();
        commonResp = userService.save(userSaveReq);
        return commonResp;
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp delete(@PathVariable long id){
        CommonResp commonResp = new CommonResp();
        userService.delete(id);
        return commonResp;
    }
}
