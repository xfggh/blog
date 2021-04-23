package com.xfggh.blog.tools.controller;

import com.xfggh.blog.BlogApplication;
import com.xfggh.blog.resp.CommonResp;
import com.xfggh.blog.util.HashUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/tools")
public class MD5Controller {
    private static final Logger LOG = LoggerFactory.getLogger(BlogApplication.class);

    @PostMapping("/hash")
    public CommonResp getHash(@RequestBody Map params) {
        CommonResp commonResp = new CommonResp();

        String beforeHash = params.get("beforeHash") == null ? "" : params.get("beforeHash").toString();
        String type = params.get("type") == null ? "" : params.get("type").toString();

        if(StringUtils.hasText(beforeHash)){
            LOG.info(type);
            if("MD5".equals(type)){
                commonResp.setContent(HashUtil.getMD5Str(beforeHash));
                commonResp.setSuccess(true);
            }else if ("SHA1".equals(type)){

            }
        }else{
            commonResp.setSuccess(false);
            commonResp.setMessage("传入字符串为空");
        }
        return commonResp;
    }
}
