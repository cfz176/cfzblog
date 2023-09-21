package com.cfz.controller;

import com.cfz.ResponseResult;
import com.cfz.entity.User;
import com.cfz.entity.dto.*;
import com.cfz.entity.vo.MenuVo;
import com.cfz.entity.vo.RoleVo;
import com.cfz.entity.vo.UserVo;
import com.cfz.service.CategoryService;
import com.cfz.service.MenuService;
import com.cfz.service.RoleService;
import com.cfz.service.UserService;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/system")
public class SystemController {

    @Autowired
    private MenuService menuService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;

    /**
     * 获取菜单列表
     *
     * @param status   菜单状态
     * @param menuName 菜单名称 （可进行模糊查询）
     * @return
     */
    @GetMapping("/menu/list")
    public ResponseResult<MenuVo> menuList(String status, String menuName) {
        return menuService.menulist(status, menuName);
    }

    /**
     * 新增菜单
     *
     * @param insertMenuDto
     * @return
     */
    @PostMapping("/menu")
    public ResponseResult menuInsert(@Validated @RequestBody InsertMenuDto insertMenuDto) {
        return menuService.menuInsert(insertMenuDto);
    }

    /**
     * 根据id查询Menu
     *
     * @param id
     * @return
     */
    @GetMapping("/menu/{id}")
    public ResponseResult<MenuVo> selectMenuById(@NotNull @PathVariable("id") Long id) {
        return menuService.selectMenuById(id);
    }

    /**
     * 根据id删除menu（逻辑删除）
     *
     * @param id
     * @return
     */
    @DeleteMapping("/menu/{id}")
    public ResponseResult deleteByid(@NotNull @PathVariable("id") Long id) {
        return menuService.menudelById(id);
    }

    /**
     * 根据id修改
     *
     * @param updateMenuDto
     * @return
     */
    @PutMapping("/menu")
    public ResponseResult updateByid(@Validated @RequestBody UpdateMenuDto updateMenuDto) {
        return menuService.menuUpdateById(updateMenuDto);
    }

    /**
     * TODO 前端字段不一致！
     * 查询菜单列表
     *
     * @return
     */
    @GetMapping("/menu/treeselect")
    public ResponseResult<MenuVo> selectMenuTree() {
        return menuService.selectMenuTree();
    }

    /**
     * 获取角色列表
     *
     * @param pageNum
     * @param pageSize
     * @param roleListDto
     * @return
     */
    @GetMapping("/role/list")
    public ResponseResult<RoleVo> roleList(Integer pageNum, Integer pageSize, RoleListDto roleListDto) {
        return roleService.roleList(pageNum, pageSize, roleListDto);
    }

    /**
     * 删除角色（批量）（逻辑删除）
     *
     * @param id
     * @return
     */
    @DeleteMapping("/role/{id}")
    public ResponseResult<MenuVo> del(@PathVariable("id") List<Long> id) {
        return roleService.delBatchById(id);
    }

    /**
     * 获取角色列表
     * @return
     */
    @GetMapping("/role/listAllRole")
    public ResponseResult<RoleVo> listAllRole() {
        return roleService.listAllRole();
    }

    /**
     * 修改角色状态
     * @return
     */
    @PutMapping("/role/changeStatus")
    public ResponseResult roleChangeStatus(@RequestBody UserDto userDto){
        return roleService.changeStatus(userDto);
    }

    /**
     * 获取用户列表
     *
     * @param pageNum
     * @param pageSize
     * @param phonenumber
     * @return
     */
    @GetMapping("/user/list")
    public ResponseResult<UserVo> listUser(@NotNull Integer pageNum,
                                           @NotNull Integer pageSize, String userName, String phonenumber,Integer status) {
        return userService.listUser(pageNum, pageSize, userName, phonenumber, status);
    }

    /**
     * 修改用户状态
     * @param
     * @return
     */
    @PutMapping("/user/changeStatus")
    public ResponseResult userChangeStatus(@RequestBody UserDto userDto) {
        return userService.changeStatus(userDto);
    }

    /**
     * 新增用户
     * @return
     */
    @PostMapping("/user")
    public ResponseResult insetUser(@RequestBody User user) {
        return userService.insetUser(user);
    }

    /**
     * 删除用户（删除，批量删除）
     * @param ids
     * @return
     */
    @DeleteMapping("/user/{id}")
    public ResponseResult delUser(@PathVariable("id") List<Long> ids) {
        return userService.delUser(ids);
    }

    /**
     * 修改用户查询
     * @param id
     * @return
     */
    @GetMapping("/user/{id}")
    public ResponseResult selOne(@PathVariable("id") Long id) {
        return userService.selOne(id);
    }

    /**
     * 修改用户
     * @param userDto
     * @return
     */
    @PutMapping("/user")
    public ResponseResult selOne(@Validated @RequestBody UserDto userDto) {
        return userService.updateUser(userDto);
    }

    /**
     * 修改角色状态
     * @param categoryDto
     * @return
     */
    @PutMapping("/category/changeStatus")
    public ResponseResult categoryChangeStatus(@RequestBody CategoryDto categoryDto) {
        return categoryService.changeStatus(categoryDto);
    }
}
