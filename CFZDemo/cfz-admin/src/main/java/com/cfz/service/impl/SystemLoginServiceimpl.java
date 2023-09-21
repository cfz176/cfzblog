package com.cfz.service.impl;

import com.cfz.ResponseResult;
import com.cfz.constants.SystemConstants;
import com.cfz.entity.LoginUser;
import com.cfz.entity.User;
import com.cfz.entity.vo.AdminUserInfoVo;
import com.cfz.entity.vo.UserInfoVo;
import com.cfz.service.MenuService;
import com.cfz.service.RoleService;
import com.cfz.service.SystemLoginService;
import com.cfz.utils.BeanCopyUtils;
import com.cfz.utils.JwtUtil;
import com.cfz.utils.RedisCache;
import com.cfz.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Component
public class SystemLoginServiceimpl implements SystemLoginService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private MenuService menuService;

    @Autowired
    private RoleService roleService;

    /**
     * 登录接口
     * @param user
     * @return
     */
    @Override
    public ResponseResult login(User user) {
        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        //判断认证是否通过
        if (Objects.isNull(authenticate)) {
            throw new RuntimeException("用户名或密码错误");
        }
        //获取userid 生成token
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(userId);
        //把用户信息存入redis
        redisCache.setCacheObject(SystemConstants.USER_ADMIN_REDISKEY+userId,loginUser);
        //封装token 返回
        HashMap<String, String> map = new HashMap<>();
        map.put("token", jwt);
        return ResponseResult.okResult(map);
    }

    /**
     * 获取 权限列表 与 角色
     * @return
     */
    @Override
    public ResponseResult<AdminUserInfoVo> getInfo() {
        //获取当前用户信息
        LoginUser loginUser = SecurityUtils.getLoginUser();
        //根据id查询权限信息
        List<String> perms = menuService.selectPermaByUserId(loginUser.getUser().getId());
        //根据用户id查询角色信息
        List<String> roleKeyList = roleService.selectRoleKeyByUserId(loginUser.getUser().getId());
        //获取用户信息
        User user = loginUser.getUser();
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(user, UserInfoVo.class);
        AdminUserInfoVo adminUserInfoVo = new AdminUserInfoVo(perms, roleKeyList, userInfoVo);
        return ResponseResult.okResult(adminUserInfoVo);
    }

    /**
     * 登出接口
     * @return
     */
    @Override
    public ResponseResult logout() {
        //获取当前用户id
        Long userId = SecurityUtils.getUserId();
        //删除redis数据
        redisCache.deleteObject(SystemConstants.USER_ADMIN_REDISKEY + userId);
        return ResponseResult.okResult();
    }
}
