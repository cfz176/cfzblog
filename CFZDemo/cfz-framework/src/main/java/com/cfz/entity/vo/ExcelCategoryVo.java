package com.cfz.entity.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExcelCategoryVo {

    //分类名
    @ExcelProperty("分类名")
    private String name;

    //描述信息
    @ExcelProperty("描述信息")
    private String description;

    //状态
    @ExcelProperty("状态 0正常 1禁用")
    private String status;

}
