package com.cfz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cfz.ResponseResult;
import com.cfz.entity.Category;
import com.cfz.entity.dto.CategoryDto;
import com.cfz.entity.vo.CategoryVo;
import com.cfz.entity.vo.PageVo;

import java.util.List;

/**
 * 分类表(Category)表服务接口
 *
 * @author makejava
 * @since 2023-01-18 12:14:03
 */
public interface CategoryService extends IService<Category> {
     ResponseResult getCategoryList();

    List<CategoryVo> listAllCategory();

    ResponseResult<PageVo> listCategory(Integer pageNum, Integer pageSize, CategoryDto categoryDto);

    ResponseResult changeStatus(CategoryDto categoryDto);
}

