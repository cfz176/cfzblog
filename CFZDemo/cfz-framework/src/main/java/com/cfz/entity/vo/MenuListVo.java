package com.cfz.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuListVo {

    private Long id;
    private String label;
    private Long parentId;
    private List<MenuListVo> children;

}
