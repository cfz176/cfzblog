package com.cfz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cfz.ResponseResult;
import com.cfz.entity.User;
import com.cfz.entity.dto.UserDto;
import com.cfz.entity.vo.UserVo;

import java.util.List;

/**
 * 用户表(User)表服务接口
 *
 * @author makejava
 * @since 2023-02-05 11:38:28
 */
public interface UserService extends IService<User> {

    ResponseResult userInfo();

    ResponseResult userinfo(User user);

    ResponseResult register(User user);

    ResponseResult<UserVo> listUser(Integer pageNum, Integer pageSize, String userName, String phonenumber, Integer status);

    ResponseResult changeStatus(UserDto userDto);

    ResponseResult insetUser(User user);

    ResponseResult delUser(List<Long> ids);

    ResponseResult selOne(Long id);

    ResponseResult updateUser(UserDto userDto);
}

