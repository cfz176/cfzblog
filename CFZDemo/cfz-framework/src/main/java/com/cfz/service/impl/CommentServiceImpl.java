package com.cfz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cfz.ResponseResult;
import com.cfz.constants.SystemConstants;
import com.cfz.entity.Comment;
import com.cfz.entity.vo.CommentVo;
import com.cfz.entity.vo.PageVo;
import com.cfz.enums.AppHttpCodeEnum;
import com.cfz.exception.SystemException;
import com.cfz.mapper.CommentMapper;
import com.cfz.service.CommentService;
import com.cfz.service.UserService;
import com.cfz.utils.BeanCopyUtils;
import com.cfz.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 评论表(Comment)表服务实现类
 *
 * @author makejava
 * @since 2023-02-15 20:46:53
 */
@Service("commentService")
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Autowired
    private UserService userService;

    /**
     * 评论
     *
     * @param commentType
     * @param articleId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public ResponseResult commentList(String commentType, Long articleId, Integer pageNum, Integer pageSize) {
        //查询对应文章的跟评论
        LambdaQueryWrapper<Comment> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //对articleId进行判断
        lambdaQueryWrapper.eq(SystemConstants.ARTICLE_COMMENT.equals(commentType),Comment::getArticleId, articleId);
        //根评论 root -1
        lambdaQueryWrapper.eq(Comment::getRootId, -1);
        //评论类型
        lambdaQueryWrapper.eq(Comment::getType, commentType);
        //分页查询
        Page<Comment> page = new Page<>(pageNum, pageSize);
        page(page, lambdaQueryWrapper);
        //封装vo
        List<CommentVo> commentVoList = toCommentVoList(page.getRecords());
        //查询子评论
        commentVoList.stream()
                .forEach(commentVo -> commentVo.setChildren(getChildren(commentVo.getId())));
//        for (CommentVo commentVo: commentVoList) {
//            List<CommentVo> childrens = getChildren(commentVo.getId());
//            commentVo.setChildren(childrens);
//        }
        return ResponseResult.okResult(new PageVo(commentVoList, page.getTotal()));
    }

    /**
     * 发表评论
     * @param comment
     * @return
     */
    @Override
    public ResponseResult addComment(Comment comment) {
        if (!StringUtils.hasText(comment.getContent())) {
            throw new SystemException(AppHttpCodeEnum.COMMENT_NOT_NULL);
        }
        save(comment);
        return ResponseResult.okResult();
    }

    /**
     *根据根评论查id查询子评论的集合
     * @param id 根评论id
     * @return
     */
    private List<CommentVo> getChildren(Long id) {
        //查询根评论id
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getRootId,id);
        queryWrapper.orderByDesc(Comment::getCreateTime);
        List<Comment> comments = list(queryWrapper);
        List<CommentVo> commentVos = toCommentVoList(comments);
        return  commentVos;
    }

    /**
     * 查找评论名称，子评论名称
     *
     * @param list
     * @return
     */
    public List<CommentVo> toCommentVoList(List<Comment> list) {
        List<CommentVo> commentVos = BeanCopyUtils.copyBeanList(list, CommentVo.class);

//        commentVos.stream()
//                .forEach(commentVo -> commentVo.setUserName(userService.getById(commentVo.getCreateBy()).getNickName()));
//        commentVos.stream()
//                .filter(commentVo -> commentVo.getToCommentId()!=-1)
//                .forEach(commentVo ->  commentVo.setToCommentUserName(userService.getById(commentVo.getToCommentUserId()).getNickName()));

//        遍历vo集合
        for (CommentVo commentVo : commentVos) {
            //通过creatBy查询用户昵称并赋值
            String UserName = userService.getById(commentVo.getCreateBy()).getNickName();
            commentVo.setUsername(UserName);
            //toCommentUserId查询用户昵称并赋值
            //如果toCommentUserId不为-1则进行查询
            if (commentVo.getToCommentId() != -1) {
                String toCommentUserName = userService.getById(commentVo.getToCommentUserId()).getNickName();
                commentVo.setToCommentUserName(toCommentUserName);
            }
        }
        return commentVos;
    }

}
