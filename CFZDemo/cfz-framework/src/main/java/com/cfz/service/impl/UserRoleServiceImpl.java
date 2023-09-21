package com.cfz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cfz.entity.UserRole;
import com.cfz.mapper.UserRoleMapper;
import com.cfz.service.UserRoleService;
import org.springframework.stereotype.Service;

/**
 * 用户和角色关联表(UserRole)表服务实现类
 *
 * @author makejava
 * @since 2023-04-21 22:09:08
 */
@Service("userRoleService")
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

}
