package com.cfz.controller;

import com.cfz.ResponseResult;
import com.cfz.entity.dto.ArticleDto;
import com.cfz.entity.dto.InsertArticleDto;
import com.cfz.entity.vo.ArticleVo;
import com.cfz.entity.vo.PageVo;
import com.cfz.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/content/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    /**
     * 新增博客
     *
     * @param insertArticleDto
     * @return
     */
    @PreAuthorize("@sp.hasPermission('content:article:writer')")
    @PostMapping
    public ResponseResult insert(@RequestBody InsertArticleDto insertArticleDto) {
        return articleService.insert(insertArticleDto);
    }

    /**
     * 查询文章(带模糊查询)
     * @param pageNum
     * @param pageSize
     * @param articleDto
     * @return
     */
    @GetMapping("/list")
    public ResponseResult<PageVo> list(Integer pageNum, Integer pageSize, ArticleDto articleDto) {
        return articleService.selectArticleByTit(pageNum,pageSize, articleDto);
    }

    /**
     * 删除文章（逻辑删除）
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseResult ArtDel(@PathVariable("id") List<Long> id) {
        return articleService.ArtDel(id);
    }

    /**
     * 根据id查询文章
     * @param id
     * @return
     */
    @GetMapping("{id}")
    public ResponseResult<ArticleVo> selArtOne(@PathVariable("id") Long id) {
        return articleService.selArtOne(id);
    }

    /**
     * 更新文章
     * @param insertArticleDto
     * @return
     */
    @PutMapping()
    public ResponseResult updateArticleByid(@RequestBody InsertArticleDto insertArticleDto) {
        return articleService.updateArticleByid(insertArticleDto);
    }

}
