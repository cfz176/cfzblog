package com.cfz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cfz.ResponseResult;
import com.cfz.entity.Role;
import com.cfz.entity.dto.RoleListDto;
import com.cfz.entity.dto.UserDto;
import com.cfz.entity.vo.RoleVo;

import java.util.List;


/**
 * 角色信息表(Role)表服务接口
 *
 * @author makejava
 * @since 2023-03-12 21:50:24
 */
public interface RoleService extends IService<Role> {

    List<String> selectRoleKeyByUserId(Long id);

    ResponseResult<RoleVo> roleList(Integer pageNum, Integer pageSize, RoleListDto roleListDto);

    ResponseResult delBatchById(List<Long> id);

    ResponseResult<RoleVo> listAllRole();

    ResponseResult changeStatus(UserDto userDto);
}
