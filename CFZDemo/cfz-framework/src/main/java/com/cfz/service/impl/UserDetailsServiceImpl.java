package com.cfz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cfz.constants.SystemConstants;
import com.cfz.entity.LoginUser;
import com.cfz.entity.Menu;
import com.cfz.entity.User;
import com.cfz.mapper.MenuMapper;
import com.cfz.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MenuMapper menuMapper;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        //查询用户信息
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getUserName, s);
        User user = userMapper.selectOne(lambdaQueryWrapper);
        //判断是否查到用户 如果没查到抛出异常
        if (Objects.isNull(user)) {
            throw new RuntimeException("用户不存在");
        }
        //为管理员，则查询权限信息封装
        if (user.getType().equals(SystemConstants.ADMIN)) {
            List<String> permissions = menuMapper.selectPermaByUserId(user.getId());
            return new LoginUser(user, permissions);
        }

        //返回用户信息
        return new LoginUser(user,null);
    }

}
