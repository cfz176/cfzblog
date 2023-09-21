package com.cfz.controller;

import com.cfz.ResponseResult;
import com.cfz.annotation.SystemLog;
import com.cfz.constants.SystemConstants;
import com.cfz.entity.Comment;
import com.cfz.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping("/commentList")
    public ResponseResult commentList(Long articleId,Integer pageNum,Integer pageSize) {
        return commentService.commentList(SystemConstants.ARTICLE_COMMENT,articleId,pageNum,pageSize);
    }

    @PostMapping
    @SystemLog(businessName = "描述信息")
    public ResponseResult comment(@RequestBody Comment comment) {
        return commentService.addComment(comment);
    }

    @GetMapping("/linkCommentList")
    public ResponseResult linkComment(Long articleId,Integer pageNum,Integer pageSize) {
        return commentService.commentList(SystemConstants.LINK_COMMENT,null,pageNum,pageSize);
    }

}
