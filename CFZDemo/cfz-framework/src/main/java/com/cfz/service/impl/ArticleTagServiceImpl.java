package com.cfz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cfz.entity.ArticleTag;
import com.cfz.mapper.ArticleTagMapper;
import com.cfz.service.ArticleTagService;
import org.springframework.stereotype.Service;

/**
 * 文章标签关联表(ArticleTag)表服务实现类
 *
 * @author makejava
 * @since 2023-03-28 12:49:01
 */
@Service("articleTagService")
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagMapper, ArticleTag> implements ArticleTagService {

}
