package com.cfz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cfz.ResponseResult;
import com.cfz.constants.SystemConstants;
import com.cfz.entity.User;
import com.cfz.entity.UserRole;
import com.cfz.entity.dto.UserDto;
import com.cfz.entity.vo.PageVo;
import com.cfz.entity.vo.RoleVo;
import com.cfz.entity.vo.UserRolesVo;
import com.cfz.entity.vo.UserVo;
import com.cfz.enums.AppHttpCodeEnum;
import com.cfz.exception.SystemException;
import com.cfz.mapper.UserMapper;
import com.cfz.service.RoleService;
import com.cfz.service.UserRoleService;
import com.cfz.service.UserService;
import com.cfz.utils.BeanCopyUtils;
import com.cfz.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户表(User)表服务实现类
 *
 * @author makejava
 * @since 2023-02-05 11:38:29
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserRoleService userRoleService;

    @Override
    public ResponseResult userInfo() {
        //获取当前用户id
        Long userId = SecurityUtils.getUserId();
        //根据用户id查询用户信息
        User userInfo = getById(userId);
        //封装给UserInfoVo
        UserVo userVo = BeanCopyUtils.copyBean(userInfo, UserVo.class);
        return ResponseResult.okResult(userVo);
    }

    @Override
    public ResponseResult userinfo(User user) {
        updateById(user);
        return ResponseResult.okResult();
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * TODO 优化：可以使用validation进行校验
     *
     * @param user
     * @return
     */
    @Override
    public ResponseResult register(User user) {
        //对数据进行非空判断
        if (!StringUtils.hasText(user.getUserName())) {
            throw new SystemException(AppHttpCodeEnum.USERNAME_NOT_NULL);
        }
        if (!StringUtils.hasText(user.getPassword())) {
            throw new SystemException(AppHttpCodeEnum.PASSWORD_NOT_NULL);
        }
        if (!StringUtils.hasText(user.getEmail())) {
            throw new SystemException(AppHttpCodeEnum.EMAIL_NOT_NULL);
        }
        if (!StringUtils.hasText(user.getNickName())) {
            throw new SystemException(AppHttpCodeEnum.NICKNAME_NOT_NULL);
        }
        //对数据进行是否存在判断
        if (userNameExit(user.getUserName())) {
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        if (emailExit(user.getEmail())) {
            throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
        }
        if (nickNameExit(user.getNickName())) {
            throw new SystemException(AppHttpCodeEnum.NICKNAME_EXIST);
        }
        //对密码进行加密
        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        //存入数据库
        save(user);
        return ResponseResult.okResult();
    }

    /**
     * 获取权限列表
     *
     * @param pageNum
     * @param pageSize
     * @param userName
     * @param phonenumber
     * @param status
     * @return
     */
    @Override
    public ResponseResult<UserVo> listUser(Integer pageNum, Integer pageSize, String userName, String phonenumber, Integer status) {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //判断 电话号 是否为空
        if (!ObjectUtils.isEmpty(phonenumber)) {
            lambdaQueryWrapper.like(User::getPhonenumber, phonenumber);
        }
        //判断 用户名 是否为空
        if (!ObjectUtils.isEmpty(userName)) {
            lambdaQueryWrapper.like(User::getUserName, userName);
        }
        //判单状态
        if (!ObjectUtils.isEmpty(status)) {
            lambdaQueryWrapper.eq(User::getStatus, status);
        }

        Page<User> page = new Page<>();
        page.setSize(pageSize);
        page.setCurrent(pageNum);
        page(page, lambdaQueryWrapper);

        List<UserVo> userVos = BeanCopyUtils.copyBeanList(page.getRecords(), UserVo.class);

        return ResponseResult.okResult(new PageVo(userVos, page.getTotal()));
    }

    /**
     * 修改用户状态
     *
     * @param
     * @param userDto
     * @return
     */
    @Override
    public ResponseResult changeStatus(UserDto userDto) {
        // id不能为空
        if (ObjectUtils.isEmpty(userDto.getId())) {
            throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        // 状态不能为空
        if (ObjectUtils.isEmpty(userDto.getId())) {
            throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        LambdaUpdateWrapper<User> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(User::getId, userDto.getId());
        lambdaUpdateWrapper.set(User::getStatus, userDto.getStatus());
        update(lambdaUpdateWrapper);
        return ResponseResult.okResult();
    }

    /**
     * 新增用户
     *
     * @param user
     * @return
     */
    @Override
    public ResponseResult insetUser(User user) {
        save(user);
        return ResponseResult.okResult();
    }

    /**
     * 删除用户（删除，批量删除）
     *
     * @param ids
     * @return
     */
    @Override
    public ResponseResult delUser(List<Long> ids) {
        LambdaUpdateWrapper<User> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.set(User::getDelFlag, SystemConstants.DEL_FLAG_YES);
        lambdaUpdateWrapper.in(User::getId, ids);
        update(lambdaUpdateWrapper);
        return ResponseResult.okResult();
    }

    /**
     * 修改用户查询
     *
     * @param id
     * @return
     */
    @Override
    public ResponseResult selOne(Long id) {
        // 查询对应id用户
        User user = baseMapper.selectById(id);

        // 查询所有角色列表
        List<RoleVo> rolesVo = (List<RoleVo>) roleService.listAllRole().getData();
        //查询用户角色id列表
        LambdaQueryWrapper<UserRole> roleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        roleLambdaQueryWrapper.select(UserRole::getRoleId).eq(UserRole::getUserId, id);
        List<UserRole> userRoles = userRoleService.list(roleLambdaQueryWrapper);
        Long[] userRolesArr = userRoles.stream()
                .map(userRole -> userRole.getRoleId())
                .toArray(Long[]::new);

        //封装Vo返回
        UserVo userVo = BeanCopyUtils.copyBean(user, UserVo.class);

        return ResponseResult.okResult(new UserRolesVo(rolesVo, userRolesArr, userVo));
    }

    /**
     * 修改用户信息
     *
     * @param userDto
     * @return
     */
    @Transactional
    @Override
    public ResponseResult updateUser(UserDto userDto) {
        //查询用户昵称
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.select(User::getNickName).eq(User::getId, userDto.getId());
        User oldUser = baseMapper.selectOne(lambdaQueryWrapper);
        //判断昵称是否被修改
        if (!oldUser.getNickName().equals(userDto.getNickName())) {
            //判断昵称是否存在
            if (nickNameExit(userDto.getNickName())) {
                //所修改的昵称已存在
                throw new SystemException(AppHttpCodeEnum.NICKNAME_EXIST);
            }
        }

        // 获取 user 和 role 的映射
        List<UserRole> userRoles = new ArrayList<>();
        UserRole userRole = new UserRole();
        // 添加用户id
        userRole.setUserId(userDto.getId());
        for (Long id : userDto.getRoleIds()) {
            //添加角色id
            userRole.setRoleId(id);
            userRoles.add(userRole);
        }

        // Dto转换
        User newUser = BeanCopyUtils.copyBean(userDto, User.class);
        // 修改用户信息
        updateById(newUser);
        //修改用户角色
        LambdaUpdateWrapper<UserRole> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(UserRole::getUserId, userRole.getUserId());
        userRoleService.remove(lambdaUpdateWrapper);
        userRoleService.saveBatch(userRoles);
        return ResponseResult.okResult();
    }

    private boolean userNameExit(String userName) {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getUserName, userName);
        return count(lambdaQueryWrapper) > 0;
    }

    private boolean emailExit(String email) {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getEmail, email);
        return count(lambdaQueryWrapper) > 0;
    }

    private boolean nickNameExit(String nickName) {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getNickName, nickName);
        return count(lambdaQueryWrapper) > 0;
    }
}

