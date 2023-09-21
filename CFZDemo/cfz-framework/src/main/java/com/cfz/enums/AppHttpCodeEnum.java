package com.cfz.enums;

import com.baomidou.mybatisplus.annotation.IEnum;

import java.io.Serializable;

public enum AppHttpCodeEnum{
    // 成功
    SUCCESS(200,"操作成功"),
    // 登录
    NEED_LOGIN(401,"需要登录后操作"),
    NO_OPERATOR_AUTH(403,"无权限操作"),
    SYSTEM_ERROR(500,"出现错误"),
    USERNAME_EXIST(501,"用户名已存在"),
     PHONENUMBER_EXIST(502,"手机号已存在"),
    EMAIL_EXIST(503, "邮箱已存在"),
    NICKNAME_EXIST(503, "昵称已存在"),
    MENUNAME_EXIST(503, "名称已存在"),
    REQUIRE_USERNAME(504, "必需填写用户名"),
    LOGIN_ERROR(505,"用户名或密码错误"),
    CONTENT_NOT_NULL(505,"文章不能为空"),
    FILE_TYPE_NULL(505,"文件类型错误，请上传png文件"),
    USERNAME_NOT_NULL(508,"用户名不能为空"),
    PASSWORD_NOT_NULL(508,"密码不能为空"),
    EMAIL_NOT_NULL(508,"邮箱不能为空"),
    NICKNAME_NOT_NULL(508, "昵称不能为空"),
    TAGNAME_NOT_NULL(508,"标签不能为空"),
    TAGREMARK_NOT_NULL(508,"备注不能为空"),
    TAGRNAME_EXIST(508,"标签重复"),
    COMMENT_NOT_NULL(508,"内容不能为空"),
    SUMMARY_NOT_NULL(508,"摘要不能为空"),
    CATEGORY_NOT_NULL(508,"分类不能为空"),
    TITLE_NOTNULL(508,"标题不能为空"),
    ARTICLE_ISDEL(508,"文章不存在");
    int code;
    String msg;

    AppHttpCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
