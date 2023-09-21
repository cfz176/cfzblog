package com.cfz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cfz.ResponseResult;
import com.cfz.entity.dto.UpdateMenuDto;
import com.cfz.entity.Menu;
import com.cfz.entity.dto.InsertMenuDto;
import com.cfz.entity.vo.MenuVo;

import java.util.List;


/**
 * 菜单权限表(Menu)表服务接口
 *
 * @author makejava
 * @since 2023-03-12 21:39:53
 */
public interface MenuService extends IService<Menu> {

    List<String> selectPermaByUserId(Long id);

    List<MenuVo> selectRouterMenuTreeById(Long userId);

    ResponseResult<MenuVo> menulist(String status, String menuName);

    ResponseResult menuInsert(InsertMenuDto insertMenuDto);

    ResponseResult<MenuVo> selectMenuById(Long id);

    ResponseResult menudelById(Long id);

    ResponseResult menuUpdateById(UpdateMenuDto updateMenuDto);

    ResponseResult<MenuVo> selectMenuTree();

}
