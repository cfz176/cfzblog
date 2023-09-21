package com.cfz.service;

import com.cfz.ResponseResult;
import com.cfz.entity.User;
import com.cfz.entity.vo.AdminUserInfoVo;

public interface SystemLoginService {

    ResponseResult login(User user);

    ResponseResult<AdminUserInfoVo> getInfo();

    ResponseResult logout();
}
