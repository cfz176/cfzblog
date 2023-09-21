package com.cfz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cfz.ResponseResult;
import com.cfz.constants.SystemConstants;
import com.cfz.entity.Article;
import com.cfz.entity.Category;
import com.cfz.entity.dto.CategoryDto;
import com.cfz.entity.vo.CategoryVo;
import com.cfz.entity.vo.PageVo;
import com.cfz.enums.AppHttpCodeEnum;
import com.cfz.exception.SystemException;
import com.cfz.mapper.CategoryMapper;
import com.cfz.service.ArticleService;
import com.cfz.service.CategoryService;
import com.cfz.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 分类表(Category)表服务实现类
 *
 * @author makejava
 * @since 2023-01-18 12:14:04
 */
@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    ArticleService articleService;

    @Override
    public ResponseResult getCategoryList() {
        //查询文章表 状态为已发布的文章
        LambdaQueryWrapper<Article> articleWrapper = new LambdaQueryWrapper<>();
        articleWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        List<Article> articles = articleService.list(articleWrapper);
        //获取文章的分类id，并且去重
        Set<Long> categoryId = articles.stream()
                .map(article -> article.getCategoryId())
                .collect(Collectors.toSet());

        //查询分类表
        List<Category> categories = listByIds(categoryId);
        categories = categories.stream()
                .filter(category -> SystemConstants.CATERGORY_STATUS_NORMAL.equals(category.getStatus()))
                .collect(Collectors.toList());

        //封装vo
        List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(categories, CategoryVo.class);

        return ResponseResult.okResult(categoryVos);
    }

    /**
     * 获取所有状态正常的分类 （statu=0)
     *
     * @return
     */
    @Override
    public List<CategoryVo> listAllCategory() {
        LambdaQueryWrapper<Category> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Category::getStatus, SystemConstants.STATU_NORMAL);
        List<Category> categoryList = list(lambdaQueryWrapper);
        List<CategoryVo> categoryVoList = BeanCopyUtils.copyBeanList(categoryList, CategoryVo.class);
        return categoryVoList;
    }

    /**
     * 获取分类列表
     *
     * @param pageNum
     * @param pageSize
     * @param categoryDto
     * @return
     */
    @Override
    public ResponseResult<PageVo> listCategory(Integer pageNum, Integer pageSize, CategoryDto categoryDto) {
        LambdaQueryWrapper<Category> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //判断 分类名 是否为空
        if (!ObjectUtils.isEmpty(categoryDto.getName())) {
            lambdaQueryWrapper.like(Category::getName, categoryDto.getName());
        }
        //判断 分类状态 是否为空
        if (!ObjectUtils.isEmpty(categoryDto.getStatus())) {
            lambdaQueryWrapper.like(Category::getStatus, categoryDto.getName());
        }

        //分页查询
        Page<Category> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        page(page, lambdaQueryWrapper);

        //转换vo返回
        List<CategoryVo> categoryVoList = BeanCopyUtils.copyBeanList(page.getRecords(), CategoryVo.class);
        return ResponseResult.okResult(new PageVo(categoryVoList, page.getTotal()));
    }

    /**
     * 修改角色状态
     * @param categoryDto
     * @return
     */
    @Override
    public ResponseResult changeStatus(CategoryDto categoryDto) {
        if (ObjectUtils.isEmpty(categoryDto.getId())) {
            throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        if (ObjectUtils.isEmpty(categoryDto.getStatus())) {
            throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        LambdaUpdateWrapper<Category> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(Category::getId, categoryDto.getId());
        lambdaUpdateWrapper.set(Category::getStatus, categoryDto.getStatus());
        update(lambdaUpdateWrapper);
        return ResponseResult.okResult();
    }

}

