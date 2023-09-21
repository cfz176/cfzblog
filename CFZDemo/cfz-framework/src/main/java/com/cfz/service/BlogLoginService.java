package com.cfz.service;

import com.cfz.ResponseResult;
import com.cfz.entity.User;

public interface BlogLoginService {

    ResponseResult login(User user);

    ResponseResult logout();
}
