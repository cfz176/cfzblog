package com.cfz.entity.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.cfz.entity.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleVo {

    //文章id
    private Long id;
    //标题
    private String title;
    //缩略图
    private String thumbnail;
    //是否置顶（0否，1是）
    private String isTop;
    //是否允许评论 1是，0否
    private String isComment;
    //文章内容
    private String content;
    //所属标签id
    private List<Long> tags;
    //所属分类id
    private Long categoryId;
    //文章摘要
    private String summary;
    //状态（0已发布，1草稿）
    private String status;
    //浏览量
    private Long viewCount;
    //创建时间
    private Date createTime;



}
