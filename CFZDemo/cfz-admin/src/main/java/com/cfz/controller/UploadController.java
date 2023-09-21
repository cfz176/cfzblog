package com.cfz.controller;

import com.cfz.ResponseResult;
import com.cfz.service.OSSUpLoadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class UploadController {

    @Autowired
    OSSUpLoadService ossUpLoadService;

    @PostMapping("/upload")
    @PreAuthorize("@sp.hasPermission('content:article:writer')")
    public ResponseResult uploadImg(@RequestParam("img") MultipartFile multipartFile) {
        return ossUpLoadService.upLoadImg(multipartFile);
    }

}
