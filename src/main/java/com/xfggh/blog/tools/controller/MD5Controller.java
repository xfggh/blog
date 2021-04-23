package com.xfggh.blog.tools.controller;

import com.xfggh.blog.BlogApplication;
import com.xfggh.blog.entity.Demo;
import com.xfggh.blog.resp.CommonResp;
import com.xfggh.blog.util.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tools")
public class MD5Controller {
    private static final Logger LOG = LoggerFactory.getLogger(BlogApplication.class);

    @PostMapping("/md5")
    public CommonResp getMD5(@RequestBody Map params) {
        CommonResp commonResp = new CommonResp();
        String beforeMD5 = params.get("beforeMD5") == null ? "" : params.get("beforeMD5").toString();
        if(StringUtils.hasText(beforeMD5)){
            commonResp.setContent(CommonUtil.getMD5Str(beforeMD5));
            commonResp.setSuccess(true);
        }else{
            commonResp.setSuccess(false);
            commonResp.setMessage("传入字符串为空");
        }
        return commonResp;
    }
}
