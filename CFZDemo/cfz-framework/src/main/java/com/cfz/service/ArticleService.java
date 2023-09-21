package com.cfz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cfz.ResponseResult;
import com.cfz.entity.dto.ArticleDto;
import com.cfz.entity.dto.InsertArticleDto;
import com.cfz.entity.Article;
import com.cfz.entity.vo.ArticleVo;
import com.cfz.entity.vo.PageVo;

import java.util.List;

public interface ArticleService extends IService<Article> {
    ResponseResult hotArticleList();

    ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId);

    ResponseResult getArticleDetail(Long id);

    ResponseResult updateViewCount(Long id);

    ResponseResult insert(InsertArticleDto articleVo);

    ResponseResult<PageVo> selectArticleByTit(Integer pageNum, Integer pageSize, ArticleDto articleDto);

    ResponseResult ArtDel(List<Long> id);

    ResponseResult<ArticleVo> selArtOne(Long id);

    ResponseResult updateArticleByid(InsertArticleDto insertArticleDto);
}
