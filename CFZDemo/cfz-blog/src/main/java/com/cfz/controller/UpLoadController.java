package com.cfz.controller;

import com.cfz.ResponseResult;
import com.cfz.service.OSSUpLoadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class UpLoadController {

    @Autowired
    OSSUpLoadService uploadServce;

    @PostMapping("/upload")
    public ResponseResult upLoadImg(MultipartFile img) {
        try {
            return uploadServce.upLoadImg(img);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("上传图片失败");
        }
    }

}
