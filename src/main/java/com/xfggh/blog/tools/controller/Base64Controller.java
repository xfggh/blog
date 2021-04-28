package com.xfggh.blog.tools.controller;

import com.xfggh.blog.BlogApplication;
import com.xfggh.blog.resp.CommonResp;
import com.xfggh.blog.util.HashUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/tools")
public class Base64Controller {
    private static final Logger LOG = LoggerFactory.getLogger(BlogApplication.class);

    @PostMapping("/base64")
    public CommonResp getBase64(@RequestBody Map params) {
        CommonResp commonResp = new CommonResp();

        String afterValue = params.get("afterValue") == null ? "" : params.get("afterValue").toString();
        String type = params.get("type") == null ? "" : params.get("type").toString();

        if(StringUtils.hasText(afterValue)){
            LOG.info(type);
            if("0".equals(type)){
                commonResp.setContent(HashUtil.getBase64Str(afterValue));
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
