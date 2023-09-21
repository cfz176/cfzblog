package com.cfz.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateMenuDto {


    //  private String isCache;
    @NotNull
    private Long id;
    @NotBlank(message = "菜单名称不能为空")
    private String menuName;
    @NotBlank(message = "菜单类型不能为空")
    private String menuType;
    @NotNull
    private Long parentId;
    @NotNull
    private Integer orderNum;

    private String path;
    private String status;
    private String visible;
    private String component;
    private String perms;
    private String  icon;
}
