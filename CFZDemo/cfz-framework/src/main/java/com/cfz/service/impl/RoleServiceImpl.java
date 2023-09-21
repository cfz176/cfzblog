package com.cfz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cfz.ResponseResult;
import com.cfz.constants.SystemConstants;
import com.cfz.entity.Role;
import com.cfz.entity.User;
import com.cfz.entity.dto.RoleListDto;
import com.cfz.entity.dto.UserDto;
import com.cfz.entity.vo.PageVo;
import com.cfz.entity.vo.RoleVo;
import com.cfz.enums.AppHttpCodeEnum;
import com.cfz.exception.SystemException;
import com.cfz.mapper.RoleMapper;
import com.cfz.service.RoleService;
import com.cfz.utils.BeanCopyUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 角色信息表(Role)表服务实现类
 *
 * @author makejava
 * @since 2023-03-12 21:50:25
 */
@Service("roleService")
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    /**
     * 根据用户id查询角色信息
     *
     * @param id
     * @return
     */
    @Override
    public List<String> selectRoleKeyByUserId(Long id) {
        //如果为管理员，返回集合中为admin
        if (id == 1) {
            ArrayList<String> roleKeys = new ArrayList<>();
            roleKeys.add(SystemConstants.USER_ROLEKEY_ADMIN);
            return roleKeys;
        }
        //否则查询对应角色
        return getBaseMapper().selectRoleKeyByUserId(id);
    }

    /**
     * 获取角色列表
     *
     * @param pageNum
     * @param pageSize
     * @param roleListDto
     * @return
     */
    @Override
    public ResponseResult<RoleVo> roleList(Integer pageNum, Integer pageSize, RoleListDto roleListDto) {
        LambdaQueryWrapper<Role> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (!ObjectUtils.isEmpty(roleListDto.getRoleName())) {
            lambdaQueryWrapper.like(Role::getRoleName, roleListDto.getRoleName());
        }
        if (!ObjectUtils.isEmpty(roleListDto.getStatus())) {
            lambdaQueryWrapper.eq(Role::getStatus, roleListDto.getStatus());
        }

        //进行分页查询
        Page<Role> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        page(page, lambdaQueryWrapper);
        List<Role> roleList = page.getRecords();
        //封装vo返回
        List<RoleVo> roleVos = BeanCopyUtils.copyBeanList(roleList, RoleVo.class);

        return ResponseResult.okResult(new PageVo(roleVos, page.getTotal()));
    }

    /**
     * （批量）删除角色 （逻辑删除）
     *
     * @param id
     * @return
     */
    @Override
    public ResponseResult delBatchById(List<Long> id) {
        if (ObjectUtils.isEmpty(id)) {
            throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        LambdaUpdateWrapper<Role> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.set(Role::getDelFlag, SystemConstants.DEL_FLAG_YES);
        lambdaUpdateWrapper.in(Role::getId, id);
        update(lambdaUpdateWrapper);
        return ResponseResult.okResult();
    }

    /**
     * 获取角色列表
     *
     * @return
     */
    @Override
    public ResponseResult<RoleVo> listAllRole() {
        LambdaQueryWrapper<Role> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.select(Role::getId, Role::getRoleName);
        List<Role> roles = list(lambdaQueryWrapper);
        List<RoleVo> roleVos = BeanCopyUtils.copyBeanList(roles, RoleVo.class);
        return ResponseResult.okResult(roleVos);
    }

    /**
     * 修改角色状态
     *
     * @param userDto
     * @return
     */
    @Override
    public ResponseResult changeStatus(UserDto userDto) {
        LambdaUpdateWrapper<Role> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(Role::getId, userDto.getId());
        lambdaUpdateWrapper.set(Role::getStatus, userDto.getStatus());
        update(lambdaUpdateWrapper);
        return ResponseResult.okResult();
    }

}
