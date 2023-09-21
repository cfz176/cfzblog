package com.cfz.entity.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDto {

    //用户id
    @NotNull
    @JSONField(alternateNames = {"userId","id"})
    private Long id;
    //状态
    @NotBlank(message = "状态不能为空")
    private String status;
    //手机号
    @NotBlank(message = "手机号不能为空")
    private String phonenumber;
    //用户名
    @NotBlank(message = "用户名不能为空")
    private String userName;
    //昵称
    @NotBlank(message = "昵称不能为空")
    private String nickName;
    //邮箱
    @NotBlank(message = "邮箱不能为空")
    private String email;
    //用户性别（0男，1女，2未知）
    @NotBlank(message = "用户性别不能为空")
    private String sex;
    @NotNull
    private Long[] roleIds;

}
