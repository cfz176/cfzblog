package com.cfz.controller;

import com.cfz.ResponseResult;
import com.cfz.entity.User;
import com.cfz.entity.vo.AdminUserInfoVo;
import com.cfz.entity.vo.MenuVo;
import com.cfz.entity.vo.RoutersVo;
import com.cfz.enums.AppHttpCodeEnum;
import com.cfz.exception.SystemException;
import com.cfz.service.MenuService;
import com.cfz.service.SystemLoginService;
import com.cfz.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LoginController {

    @Autowired
    private SystemLoginService userService;

    @Autowired
    private MenuService menuService;

    /**
     * 登录接口
     * @param user
     * @return
     */
    @PostMapping("/user/login")
    public ResponseResult login(@RequestBody User user) {
        if (!StringUtils.hasText(user.getUserName())) {
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        return userService.login(user);
    }

    /**
     * 登出接口
     * @return
     */
    @PostMapping("/user/logout")
    public ResponseResult logout() {
        return userService.logout();
    }

    /**
     * 获取 权限列表 与 角色
     * @return
     */
    @GetMapping("/getInfo")
    public ResponseResult<AdminUserInfoVo> getInfo() {
        return userService.getInfo();
    }

    /**
     * 查询 菜单列表（返回tree形式）
     * @return
     */
    @GetMapping("/getRouters")
    public ResponseResult<RoutersVo> getRouters() {
        //查询menu 结果为tree
        List<MenuVo> menuVos = menuService.selectRouterMenuTreeById(SecurityUtils.getUserId());
        //封装数据返回
        return ResponseResult.okResult(new RoutersVo(menuVos));
    }
}
