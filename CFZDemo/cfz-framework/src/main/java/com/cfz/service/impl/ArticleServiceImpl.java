package com.cfz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cfz.ResponseResult;
import com.cfz.constants.SystemConstants;
import com.cfz.entity.dto.ArticleDto;
import com.cfz.entity.dto.InsertArticleDto;
import com.cfz.entity.Article;
import com.cfz.entity.ArticleTag;
import com.cfz.entity.Category;
import com.cfz.entity.Tag;
import com.cfz.entity.vo.*;
import com.cfz.enums.AppHttpCodeEnum;
import com.cfz.exception.SystemException;
import com.cfz.mapper.ArticleMapper;
import com.cfz.mapper.ArticleTagMapper;
import com.cfz.service.ArticleService;
import com.cfz.service.ArticleTagService;
import com.cfz.service.CategoryService;
import com.cfz.utils.BeanCopyUtils;
import com.cfz.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ArticleTagService articleTagService;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private ArticleTagMapper articleTagMapper;

    /**
     * 热门文章查询
     *
     * @return
     */
    @Override
    public ResponseResult hotArticleList() {
        //查询热门文章 封装为ResponseResult返回
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        //必须是正是文章
        queryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        //按照浏览量进行排序
        queryWrapper.orderByDesc(Article::getViewCount);
        //最多只查询10条
        Page<Article> page = new Page(SystemConstants.ARTICLE_SEL_CURRENT, SystemConstants.ARTICLE_SEL_SIZE);
        page(page, queryWrapper);

        List<Article> articles = page.getRecords();

//        //bean拷贝
//        List<HotArticleVo> articleVos = new ArrayList<>();
//        for (Article article : articles) {
//            HotArticleVo vo = new HotArticleVo();
//            BeanUtils.copyProperties(article,vo);
//            articleVos.add(vo);
//        }

        List<HotArticleVo> articleVos = BeanCopyUtils.copyBeanList(articles, HotArticleVo.class);

        return ResponseResult.okResult(articleVos);
    }

    /**
     * 根据分类查询文章
     *
     * @param pageNum
     * @param pageSize
     * @param categoryId
     * @return
     */
    @Override
    public ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId) {
        //查询条件
        LambdaQueryWrapper<Article> articleQueryWrapper = new LambdaQueryWrapper<>();
        //如果有 categoryId 就要 查询相应的类型
        articleQueryWrapper.eq(Objects.nonNull(categoryId) && categoryId > 0, Article::getCategoryId, categoryId);
        //状态是正常发布的
        articleQueryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        //对isTop进行排序
        articleQueryWrapper.orderByDesc(Article::getIsTop);

        //分页查询
        Page<Article> page = new Page<>(pageNum, pageSize);
        page(page, articleQueryWrapper);

        //查询categoryName
        List<Article> articles = page.getRecords();

        //用articleId查询CategoryName进行设置
//         for(Article article : articles) {
//            Category category = categoryService.getById(article.getCategoryId());
//            article.setCategoryName(category.getName());
//        }

         articles.stream()
                .map(article -> article.setCategoryName(categoryService.getById(article.getCategoryId()).getName()))
                .collect(Collectors.toList());

        //封装查询结果
        List<ArticleListVo> articleListVos = BeanCopyUtils.copyBeanList(page.getRecords(), ArticleListVo.class);
        PageVo pageVo = new PageVo(articleListVos, page.getTotal());

        return ResponseResult.okResult(pageVo);
    }

    /**
     * 文章详情查询
     *
     * @param id
     * @return
     */
    @Override
    public ResponseResult getArticleDetail(Long id) {
        //根据id查询文章
        Article article = getById(id);
        //从redis获取viewcount
        Integer viewCount = redisCache.getCacheMapValue(SystemConstants.ARTICLE_REDIS_KEY, id.toString());
        article.setViewCount(viewCount.longValue());
        //转化vo
        ArticleDetailVo articleDetailVo = BeanCopyUtils.copyBean(article, ArticleDetailVo.class);
        //根据分类id查询分类名
        Long categoryId = articleDetailVo.getCategoryId();
        if (categoryId != null) {
            Category category = categoryService.getById(categoryId);
            articleDetailVo.setCategoryName(category.getName());
        }
        //封装响应返回
        return ResponseResult.okResult(articleDetailVo);
    }

    /**
     * 更新文章阅读量
     *
     * @param id
     * @return
     */
    @Override
    public ResponseResult updateViewCount(Long id) {
        redisCache.incrementCacheMapValue(SystemConstants.ARTICLE_REDIS_KEY, id.toString(), 1);
        return ResponseResult.okResult();
    }

    /**
     * 新增博客
     *
     * @param articleVo
     * @return
     */
    @Override
    public ResponseResult insert(InsertArticleDto articleVo) {
        //对数据进行非空判断
        if (articleVo.getTitle().isEmpty()) {
            throw new SystemException(AppHttpCodeEnum.TITLE_NOTNULL);
        }
        if (articleVo.getSummary().isEmpty()) {
            throw new SystemException(AppHttpCodeEnum.SUMMARY_NOT_NULL);
        }
        if (articleVo.getContent().isEmpty()) {
            throw new SystemException(AppHttpCodeEnum.CONTENT_NOT_NULL);
        }
        if (ObjectUtils.isEmpty(articleVo.getCategoryId())) {
            throw new SystemException(AppHttpCodeEnum.CATEGORY_NOT_NULL);
        }
        if (articleVo.getTags().isEmpty()) {
            throw new SystemException(AppHttpCodeEnum.TAGNAME_NOT_NULL);
        }
        if (articleVo.getIsComment().isEmpty()) {
            throw new SystemException(AppHttpCodeEnum.COMMENT_NOT_NULL);
        }
        if (articleVo.getIsTop().isEmpty()) {
            throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        if (articleVo.getStatus().isEmpty()) {
            throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        //新增
        Article article = BeanCopyUtils.copyBean(articleVo, Article.class);
        save(article);

        //获取tag和article关系映射
        List<ArticleTag> articleTags = articleVo.getTags().stream()
                .map(tagId -> new ArticleTag(article.getId(), tagId))
                .collect(Collectors.toList());

        //添加 article与tag关系
        articleTagService.saveBatch(articleTags);
        return ResponseResult.okResult();

    }

    @Override
    public ResponseResult<PageVo> selectArticleByTit(Integer pageNum, Integer pageSize, ArticleDto articleDto) {
        LambdaQueryWrapper<Article> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (!ObjectUtils.isEmpty(articleDto.getTitle())) {
            lambdaQueryWrapper.like(Article::getTitle, articleDto.getTitle());
        }

        if (!ObjectUtils.isEmpty(articleDto.getSummary())) {
            lambdaQueryWrapper.like(Article::getSummary, articleDto.getSummary());
        }

        Page<Article> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        page(page, lambdaQueryWrapper);

        List<ArticleVo> articleVos = BeanCopyUtils.copyBeanList(page.getRecords(), ArticleVo.class);
        return ResponseResult.okResult(new PageVo(articleVos, page.getTotal()));
    }

    /**
     * 删除文章（逻辑删除 del_flag 0未删除 1删除）
     *
     * @param ids
     * @return
     */
    @Override
    public ResponseResult ArtDel(List<Long> ids) {
        //判断id是否为空
        if (ObjectUtils.isEmpty(ids)) {
            throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        //判断该文章是否已被删除
        delFlagExit(ids);
        LambdaUpdateWrapper<Article> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.set(Article::getDelFlag, SystemConstants.DEL_FLAG_YES);
        lambdaUpdateWrapper.in(Article::getId, ids);
        //进行逻辑删除
        update(lambdaUpdateWrapper);
        return ResponseResult.okResult();
    }

    /**
     * 根据id查询文章
     *
     * @param id
     * @return
     */
    @Override
    public ResponseResult<ArticleVo> selArtOne(Long id) {
        //查询文章
        ArticleMapper baseMapper = getBaseMapper();
        Article article = baseMapper.selectById(id);
        //查询文章标签
        List<Tag> tags = articleMapper.selectTagById(id);
        List<Long> collect = tags.stream()
                .map(tag -> tag.getId())
                .collect(Collectors.toList());
        article.setTags(collect);
        return ResponseResult.okResult(article);
    }

    @Override
    @Transactional
    public ResponseResult updateArticleByid(InsertArticleDto insertArticleDto) {
        //非空判断
        if (insertArticleDto.getTitle().isEmpty()) {
            throw new SystemException(AppHttpCodeEnum.TITLE_NOTNULL);
        }
        if (insertArticleDto.getSummary().isEmpty()) {
            throw new SystemException(AppHttpCodeEnum.SUMMARY_NOT_NULL);
        }
        if (insertArticleDto.getContent().isEmpty()) {
            throw new SystemException(AppHttpCodeEnum.CONTENT_NOT_NULL);
        }
        if (ObjectUtils.isEmpty(insertArticleDto.getCategoryId())) {
            throw new SystemException(AppHttpCodeEnum.CATEGORY_NOT_NULL);
        }
        if (insertArticleDto.getTags().isEmpty()) {
            throw new SystemException(AppHttpCodeEnum.TAGNAME_NOT_NULL);
        }
        if (insertArticleDto.getIsComment().isEmpty()) {
            throw new SystemException(AppHttpCodeEnum.COMMENT_NOT_NULL);
        }
        if (insertArticleDto.getIsTop().isEmpty()) {
            throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        if (insertArticleDto.getStatus().isEmpty()) {
            throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        //转换
        Article article = BeanCopyUtils.copyBean(insertArticleDto, Article.class);
        updateById(article);

        //获取article和tag的映射关系
        List<ArticleTag> articleTags = insertArticleDto.getTags().stream()
                .map(tagId -> new ArticleTag(insertArticleDto.getId(), tagId))
                .collect(Collectors.toList());

        LambdaUpdateWrapper<ArticleTag> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(ArticleTag::getArticleId, insertArticleDto.getId());
        articleTagService.remove(lambdaUpdateWrapper);
        articleTagService.saveBatch(articleTags);

        return ResponseResult.okResult();
    }

    private void delFlagExit(List<Long> ids) {
        LambdaQueryWrapper<Article> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(Article::getId,ids);
        lambdaQueryWrapper.eq(Article::getDelFlag, SystemConstants.DEL_FLAG_NO);
        if (count(lambdaQueryWrapper) != ids.size()) {
            throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
        }
    }

}
