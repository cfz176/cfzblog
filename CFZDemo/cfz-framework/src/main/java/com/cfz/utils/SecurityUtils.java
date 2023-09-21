package com.cfz.utils;

import com.cfz.entity.LoginUser;
import com.cfz.enums.AppHttpCodeEnum;
import com.cfz.exception.SystemException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    /**
     * 获取用户
     **/
    public static LoginUser getLoginUser() {
        try {
            LoginUser principal = (LoginUser) getAuthentication().getPrincipal();
            return principal;
        } catch (Exception e) {
            throw new SystemException(AppHttpCodeEnum.NEED_LOGIN);
        }
    }

    /**
     * 获取Authentication
     */
    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public static Boolean isAdmin() {
        Long id = getLoginUser().getUser().getId();
        return id != null && id.equals(1L);
    }

    public static Long getUserId() {
        return getLoginUser().getUser().getId();
    }
}