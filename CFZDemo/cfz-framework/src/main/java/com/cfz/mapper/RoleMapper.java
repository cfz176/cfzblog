package com.cfz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cfz.entity.Role;

import java.util.List;


/**
 * 角色信息表(Role)表数据库访问层
 *
 * @author makejava
 * @since 2023-03-12 21:50:23
 */
public interface RoleMapper extends BaseMapper<Role> {

    List<String> selectRoleKeyByUserId(Long userId);
}
