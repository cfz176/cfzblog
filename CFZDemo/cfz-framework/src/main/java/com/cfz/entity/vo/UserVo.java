package com.cfz.entity.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVo {

    @TableId(type = IdType.AUTO)
    private Long id;

    //用户名称
    private String userName;

    //用户昵称
    private String nickName;

    //邮箱
    private String email;

    //用户性别（0男，1女，2未知）
    private String sex;
    //头像
    private String avatar;
    private String phonenumber;
    //创建时间
    private Date createTime;
    //账号状态（0正常 1停用）
    private String status;

}
