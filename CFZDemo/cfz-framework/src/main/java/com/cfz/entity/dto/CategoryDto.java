package com.cfz.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {

    // 分类 id
    private Long id;
    // 分类名
    private String name;
    // 状态:0 正常,1 禁用
    private String status;

}
