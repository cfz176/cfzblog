package com.cfz.service.impl;

import com.cfz.ResponseResult;
import com.cfz.constants.SystemConstants;
import com.cfz.entity.LoginUser;
import com.cfz.entity.User;
import com.cfz.entity.vo.BlogUserLoginVo;
import com.cfz.entity.vo.UserInfoVo;
import com.cfz.service.BlogLoginService;
import com.cfz.utils.BeanCopyUtils;
import com.cfz.utils.JwtUtil;
import com.cfz.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class BlogLoginServiceImpl implements BlogLoginService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    /**
     * 登录
     * @param user
     * @return
     */
    @Override
    public ResponseResult login(User user) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        //判断是否认证通过
        if (Objects.isNull(authenticate)){
            throw new RuntimeException("用户名或密码错误");
        }
        //获取 userid 生成 token
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(userId);
        //把用户信息存入 Redis
        redisCache.setCacheObject(SystemConstants.USER_BLOG_REDISKEY +userId,loginUser);
        //把 token 和 userinfo 封装返回
        //把User转换成UserInfoVo
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(loginUser.getUser(), UserInfoVo.class);
        BlogUserLoginVo blogUserLoginVo = new BlogUserLoginVo(jwt, userInfoVo);
        return ResponseResult.okResult(blogUserLoginVo);
    }

    /**
     * 登出
     * @return
     */
    @Override
    public ResponseResult logout() {
        //获取token
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        //解析获取userid
        String userId = loginUser.getUser().getId().toString();
        //删除用户redis
        redisCache.deleteObject(SystemConstants.USER_BLOG_REDISKEY + userId);
        return ResponseResult.okResult();
    }

}
