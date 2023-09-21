package com.cfz.service.impl;

import com.cfz.utils.SecurityUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("sp")
public class PermissionService {

    /**
     * 导出分类权限控制
     * @param permission
     * @return
     */
    public boolean hasPermission(String permission) {
        //如果是超级管理员，直接通过
        if (SecurityUtils.isAdmin()) {
            return true;
        }
        //否则，查询用户对应权限
        List<String> perList = SecurityUtils.getLoginUser().getPermission();
        //判断是否具有权限
        return perList.contains(permission);

    }
}
