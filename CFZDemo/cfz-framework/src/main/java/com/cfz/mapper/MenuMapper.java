package com.cfz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cfz.entity.Menu;

import java.util.List;


/**
 * 菜单权限表(Menu)表数据库访问层
 *
 * @author makejava
 * @since 2023-03-12 21:39:51
 */
public interface MenuMapper extends BaseMapper<Menu> {

    List<String> selectPermaByUserId(Long id);

    List<Menu> selectAllRouterMneu();

    List<Menu> selectRouterMenuById(Long userId);
}
