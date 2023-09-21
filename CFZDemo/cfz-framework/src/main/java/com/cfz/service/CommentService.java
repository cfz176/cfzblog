package com.cfz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cfz.ResponseResult;
import com.cfz.entity.Comment;

/**
 * 评论表(Comment)表服务接口
 *
 * @author makejava
 * @since 2023-02-15 20:46:53
 */
public interface CommentService extends IService<Comment> {

    ResponseResult commentList(String commentType, Long articleId, Integer pageNum, Integer pageSize);

    ResponseResult addComment(Comment comment);
}

