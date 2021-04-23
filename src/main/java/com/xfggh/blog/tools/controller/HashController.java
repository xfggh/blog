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
public class HashController {
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
                commonResp.setContent(HashUtil.getSHA1Str(beforeHash));
                commonResp.setSuccess(true);
            }else if ("SHA256".equals(type)){
                commonResp.setContent(HashUtil.getSHA256Str(beforeHash));
                commonResp.setSuccess(true);
            }else if ("SHA512".equals(type)){
                commonResp.setContent(HashUtil.getSHA512Str(beforeHash));
                commonResp.setSuccess(true);
            }else{
                commonResp.setSuccess(false);
                commonResp.setMessage("传入类型为空");
            }
        }else{
            commonResp.setSuccess(false);
            commonResp.setMessage("传入字符串为空");
        }
        return commonResp;
    }
}
