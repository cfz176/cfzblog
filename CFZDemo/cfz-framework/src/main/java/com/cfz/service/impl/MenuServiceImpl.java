package com.cfz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cfz.ResponseResult;
import com.cfz.constants.SystemConstants;
import com.cfz.entity.dto.UpdateMenuDto;
import com.cfz.entity.Menu;
import com.cfz.entity.dto.InsertMenuDto;
import com.cfz.entity.vo.MenuTreeVo;
import com.cfz.entity.vo.MenuVo;
import com.cfz.enums.AppHttpCodeEnum;
import com.cfz.exception.SystemException;
import com.cfz.mapper.MenuMapper;
import com.cfz.service.MenuService;
import com.cfz.utils.BeanCopyUtils;
import com.cfz.utils.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜单权限表(Menu)表服务实现类
 *
 * @author makejava
 * @since 2023-03-12 21:39:53
 */
@Service("menuService")
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {


    /**
     * 根据id查询权限信息
     * @param id
     * @return
     */
    @Override
    public List<String> selectPermaByUserId(Long id) {
        //如果是管理员，返回所有权限
        if(SecurityUtils.isAdmin()){
            LambdaQueryWrapper<Menu> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.in(Menu::getMenuType, SystemConstants.MENU, SystemConstants.BUTTON);
            lambdaQueryWrapper.eq(Menu::getStatus, SystemConstants.STATU_NORMAL);
            List<Menu> menus = list(lambdaQueryWrapper);
            List<String> permList = menus.stream()
                    .map(Menu::getPerms)
                    .collect(Collectors.toList());
            return permList;
        }
        //否则返所具有的权限
        List<String> permList = getBaseMapper().selectPermaByUserId(id);
        return permList;
    }

    /**
     * 获取菜单列表
     * @param userId
     * @return
     */
    @Override
    public List<MenuVo> selectRouterMenuTreeById(Long userId) {
        MenuMapper baseMapper = getBaseMapper();
        List<Menu> menus = null;
        //若果是否为管理员 若是则查询所有没有被删除的 C类，M类 菜单
        if (SecurityUtils.isAdmin()) {
            //是管理员 查询所有符合要求的menu
             menus = baseMapper.selectAllRouterMneu();
        } else {
            //否则 查询当前用户所拥有的menu
            menus = baseMapper.selectRouterMenuById(userId);
        }

        //转换Vo
        List<MenuVo> menuVos = BeanCopyUtils.copyBeanList(menus, MenuVo.class);
        //转为tree形式
        List<MenuVo> mentTrees = buildMenuTree(menuVos,0L);
        //否则 查询对应的菜单
        return mentTrees;
    }

    /**
     * 获取菜单列表
     *
     * @param status   菜单状态
     * @param menuName 菜单名称 （可进行模糊查询）
     * @return
     */
    @Override
    public ResponseResult<MenuVo> menulist(String status, String menuName) {
        LambdaUpdateWrapper<Menu> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.orderByAsc(Menu::getParentId);
        lambdaUpdateWrapper.orderByAsc(Menu::getOrderNum);
        //判断是否查询状态
        if (!ObjectUtils.isEmpty(status)) {
            lambdaUpdateWrapper.eq(Menu::getStatus, status);
        }
        //判断是否查询菜单名称
        if (!ObjectUtils.isEmpty(menuName)) {
            lambdaUpdateWrapper.like(Menu::getMenuName, menuName);
        }
        //数据库查询
        List<Menu> menuList = list(lambdaUpdateWrapper);
        //转换vo返回
        List<MenuVo> menuVos = BeanCopyUtils.copyBeanList(menuList, MenuVo.class);
        return ResponseResult.okResult(menuVos);
    }

    /**
     * 新增菜单
     * @param insertMenuDto
     * @return
     */
    @Override
    public ResponseResult menuInsert(InsertMenuDto insertMenuDto) {
        Menu menu = BeanCopyUtils.copyBean(insertMenuDto, Menu.class);
        //查询名称是否已存在
        if (isMenuNameExit(insertMenuDto)) {
            throw new SystemException(AppHttpCodeEnum.MENUNAME_EXIST);
        }
        save(menu);
        return ResponseResult.okResult();
    }

    /**
     * 根据id查询Menu
     * @param id
     * @return
     */
    @Override
    public ResponseResult<MenuVo> selectMenuById(Long id) {
        Menu menu = getBaseMapper().selectById(id);
        MenuVo menuVo = BeanCopyUtils.copyBean(menu, MenuVo.class);
        return ResponseResult.okResult(menuVo);
    }

    /**
     * 根据id删除menu（逻辑删除
     * @param id
     * @return
     */
    @Override
    public ResponseResult menudelById(Long id) {
        //查询数据库中是否存在
        if (isMenuIdExit(id)) {
            throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        LambdaUpdateWrapper<Menu> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(Menu::getId, id);
        lambdaUpdateWrapper.set(Menu::getDelFlag, SystemConstants.DEL_FLAG_YES);
        update(lambdaUpdateWrapper);
        return ResponseResult.okResult();
    }

    /**
     * 根据id修改
     * @param updateMenuDto
     * @return
     */
    @Override
    public ResponseResult menuUpdateById(UpdateMenuDto updateMenuDto) {
        //判断数据是否存在
        if (isExit(updateMenuDto.getId())) {
            throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        Menu menu = BeanCopyUtils.copyBean(updateMenuDto, Menu.class);
        updateById(menu);
        return ResponseResult.okResult();
    }

    /**
     * TODO 前端字段不一致！
     * 获取菜单权限列表
     * @return
     */
    @Override
    public ResponseResult<MenuVo> selectMenuTree() {
        LambdaQueryWrapper<Menu> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.orderByAsc(Menu::getOrderNum);
        //查询所有菜单
        List<Menu> menuList = list(lambdaQueryWrapper);
        //转换vo
        List<MenuVo> menuVos= BeanCopyUtils.copyBeanList(menuList, MenuVo.class);
        //生成menuTree
        List<MenuVo> menuTree = buildMenuTree(menuVos, 0L);
        List<MenuTreeVo> menuTreeVos = BeanCopyUtils.copyBeanList(menuTree, MenuTreeVo.class);
        return ResponseResult.okResult(menuTreeVos);
    }

    /**
     * 判断数据是否存在
     * @param id
     * @return
     */
    private boolean isExit(Long id) {
        LambdaQueryWrapper<Menu> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Menu::getId, id);
        return count(lambdaQueryWrapper)!=1;
    }

    /**
     * 查询数据库中是否存在
     * @param id
     * @return
     */
    private boolean isMenuIdExit(Long id) {
        LambdaQueryWrapper<Menu> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Menu::getId, id);
        return count(lambdaQueryWrapper)!=1;
    }

    /**
     * 菜单名是否存在校验
     * @param insertMenuDto
     * @return
     */
    private boolean isMenuNameExit(InsertMenuDto insertMenuDto) {
        LambdaQueryWrapper<Menu> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Menu::getMenuName, insertMenuDto.getMenuName());
        return count(lambdaQueryWrapper)>0;
    }

    /**
     * 树形结构
     * @param menuVos
     * @param parentId
     * @return
     */
    private List<MenuVo>  buildMenuTree(List<MenuVo> menuVos,Long parentId) {
        List<MenuVo> menuVoTree = menuVos.stream()
                //拿到顶级父类菜单
                .filter(menuVo -> menuVo.getParentId().equals(parentId))
                //查找父类菜单的子菜单
                .map(menuVo -> menuVo.setChildren(getChildrens(menuVo, menuVos)))
                .collect(Collectors.toList());
        return menuVoTree;
    }

    /**
     * 子菜单分配
     * @param menuVo
     * @param menuVos
     * @return
     */
    private List<MenuVo> getChildrens(MenuVo menuVo, List<MenuVo> menuVos) {
        List<MenuVo> collect = menuVos.stream()
                //过滤掉父类菜单
                .filter(m -> m.getParentId().equals(menuVo.getId()))
                .map(m -> m.setChildren(getChildrens(m, menuVos)))
                .collect(Collectors.toList());
        return collect;
    }
}
