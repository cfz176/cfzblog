package com.cfz.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class InsertMenuDto {


    //  private String isCache;
    @NotBlank(message = "菜单名称不能为空")
    private String menuName;
    @NotBlank(message = "菜单类型不能为空")
    private String menuType;
    @NotNull
    private Integer orderNum;
    @NotNull
    private Long parentId;

    private String  icon;
    private String path;
    private String status;
    private String visible;
}
