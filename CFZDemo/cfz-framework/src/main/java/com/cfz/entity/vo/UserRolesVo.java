package com.cfz.entity.vo;

import com.cfz.entity.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserRolesVo {

    private List<RoleVo> roles;
    private Long[] roleIds;
    private UserVo user;

}
