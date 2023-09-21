package com.cfz.service;

import com.cfz.ResponseResult;
import org.springframework.web.multipart.MultipartFile;

public interface OSSUpLoadService {
    ResponseResult upLoadImg(MultipartFile img);
}
