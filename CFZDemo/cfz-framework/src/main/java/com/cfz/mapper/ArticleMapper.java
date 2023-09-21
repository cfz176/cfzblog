package com.cfz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cfz.entity.Article;
import com.cfz.entity.Tag;

import java.util.List;

public interface ArticleMapper extends BaseMapper<Article>{
    List<Tag> selectTagById(Long id);
}
