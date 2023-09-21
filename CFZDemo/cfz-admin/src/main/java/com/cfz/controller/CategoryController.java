package com.cfz.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.cfz.ResponseResult;
import com.cfz.entity.Category;
import com.cfz.entity.dto.CategoryDto;
import com.cfz.entity.vo.CategoryVo;
import com.cfz.entity.vo.ExcelCategoryVo;
import com.cfz.entity.vo.PageVo;
import com.cfz.enums.AppHttpCodeEnum;
import com.cfz.service.CategoryService;
import com.cfz.service.impl.CategoryServiceImpl;
import com.cfz.utils.BeanCopyUtils;
import com.cfz.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/content/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 查询所有状态正常的分类 （statu=0）
     *
     * @return
     */
    @GetMapping("/listAllCategory")
    public ResponseResult<CategoryVo> listAllCategory() {
        List<CategoryVo> categoryVoList = this.categoryService.listAllCategory();
        return ResponseResult.okResult(categoryVoList);
    }

    /**
     * 导出分类到Excal
     *
     * @return
     */
    @PreAuthorize("@sp.hasPer.mission('content:category:export')")
    @GetMapping("/export")
    public void export(HttpServletResponse res) {
        try {
            //设置下载文件请求头
            WebUtils.setDownLoadHeader("分类.xlsx", res);
            //获取要导入的数据
            List<Category> categories = categoryService.list();
            List<ExcelCategoryVo> excelCategoryVos
                    = BeanCopyUtils.copyBeanList(categories, ExcelCategoryVo.class);
            //将数据导入到Excel中
            EasyExcel.write(res.getOutputStream(), ExcelCategoryVo.class)
                    .autoCloseStream(Boolean.FALSE).sheet("分类模板")
                    .doWrite(excelCategoryVos);
            //如果错误响应json格式
        } catch (Exception e) {
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
            res.reset();
            WebUtils.renderString(res, JSON.toJSONString(result));
        }
    }

    /**
     * 查询所有分类
     * @param pageNum
     * @param pageSize
     * @param categoryDto
     * @return
     */
    @GetMapping("/list")
    public ResponseResult<PageVo> list(@NotNull Integer pageNum, @NotNull Integer pageSize, CategoryDto categoryDto) {
        return categoryService.listCategory(pageNum,pageSize,categoryDto);
    }

}
