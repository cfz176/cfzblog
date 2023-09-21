package com.cfz.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryVo {

    private Long id;

    private String name;

    //描述信息
    private String description;

}
