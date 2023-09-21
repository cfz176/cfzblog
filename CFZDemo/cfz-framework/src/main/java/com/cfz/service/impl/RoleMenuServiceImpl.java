package com.cfz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cfz.entity.RoleMenu;
import com.cfz.mapper.RoleMenuMapper;
import com.cfz.service.RoleMenuService;
import org.springframework.stereotype.Service;

/**
 * 角色和菜单关联表(RoleMenu)表服务实现类
 *
 * @author makejava
 * @since 2023-04-21 22:06:19
 */
@Service("roleMenuService")
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements RoleMenuService {

}
