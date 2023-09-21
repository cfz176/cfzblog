package com.cfz.entity;

import java.util.Date;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 用户表(User)表实体类
 *
 * @author makejava
 * @since 2023-02-05 11:38:28
 */

@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_user")
public class User {
    //主键
    @NotNull
    private Long id;
    //用户名
    @NotBlank(message = "用户名不能为空")
    private String userName;
    //昵称
    @NotBlank(message = "昵称不能为空")
    private String nickName;
    //密码
    @NotBlank(message = "密码不能为空")
    private String password;
    private String type;
    //账号状态（0正常 1停用）
    @NotBlank(message = "账号状态不能为空")
    private String status;
    //邮箱
    @NotBlank(message = "邮箱不能为空")
    private String email;
    //手机号
    @NotBlank(message = "手机号不能为空")
    private String phonenumber;
    //用户性别（0男，1女，2未知）
    @NotBlank(message = "用户性别不能为空")
    private String sex;
    //头像
    private String avatar;
    //创建人的用户id
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;
    //创建时间
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    //更新人
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateBy;
    //更新时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
    //删除标志（0代表未删除，1代表已删除）
    private Integer delFlag;
}

