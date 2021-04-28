package com.xfggh.blog.tools.controller;

import com.xfggh.blog.BlogApplication;
import com.xfggh.blog.resp.CommonResp;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/tools")
public class UploadFile {
    private static final Logger LOG = LoggerFactory.getLogger(BlogApplication.class);

    @PostMapping("/upload")
    public CommonResp upload(@RequestParam("file") MultipartFile file) {
        CommonResp commonResp = new CommonResp();

        if(file.isEmpty()){
            commonResp.setSuccess(false);
            commonResp.setMessage("上传失败，文件为空");

            return commonResp;
        }
        LOG.info(String.valueOf(file.getSize()));
        if(file.getSize() > 1048576){

        }
        LOG.info(System.getProperty("user.dir"));
        String filePath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\assets\\upload\\";
        String fileName = file.getOriginalFilename();
        File dest = new File(filePath + fileName);
        try {
            file.transferTo(dest);
            commonResp.setSuccess(true);
        } catch (IOException e) {
            commonResp.setSuccess(false);
            commonResp.setMessage(e.getMessage());
            e.printStackTrace();
        }

        return commonResp;
    }

}


