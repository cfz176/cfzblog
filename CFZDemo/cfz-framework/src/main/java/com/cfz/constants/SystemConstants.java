package com.cfz.constants;

import com.cfz.enums.AppHttpCodeEnum;

public class SystemConstants {

    /**
     *  文章是草稿
     */
    public static final int ARTICLE_STATUS_DRAFT = 1;


    /**
     * 文章是正常发布状态
     */
    public static final int ARTICLE_STATUS_NORMAL = 0;

    /**
     *  查询页数
     */
    public static final int ARTICLE_SEL_CURRENT = 1;


    /**
     * 查询条数
     */
    public static final int ARTICLE_SEL_SIZE = 10;

    /**
     * 文章类型状态
     */
    public static final String CATERGORY_STATUS_NORMAL = "0";

    /**
     * 友链审核通过
     */
    public static final String LINK_STATUS_NORMAL = "0";
    public static final String ARTICLE_COMMENT = "0";
    public static final String LINK_COMMENT = "1";

    /**
     * 浏览量 rediskey
     */
    public static final String ARTICLE_REDIS_KEY = "article:viewcount";

    /**
     * 前台用户信息 rediskey
     */
    public static final String USER_BLOG_REDISKEY = "bloglogin:";

    /**
     * 菜单类型 C 菜单 F 按钮
     */
    public static final String MENU = "C";
    public static final String BUTTON = "F";

    /**
     * 状态 0 正常 1停用
     */
    public static final String STATU_NORMAL = "0";
    public static final String STATU_BLOCK = "1";

    /**
     * 后台用户信息 rediskey
     */
    public static final String USER_ADMIN_REDISKEY = "login:";

    /**
     * 管理员角色
     */
    public static final String USER_ROLEKEY_ADMIN = "admin";

    /**
     * 逻辑删除 0未删除 1 删除
     */
    public static final String DEL_FLAG_NO = "0";
    public static final String DEL_FLAG_YES = "1";
    public static final String ADMIN = "1";
}
