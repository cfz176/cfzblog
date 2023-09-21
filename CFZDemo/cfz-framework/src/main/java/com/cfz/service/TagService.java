package com.cfz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cfz.ResponseResult;
import com.cfz.entity.dto.TagDto;
import com.cfz.entity.Tag;
import com.cfz.entity.vo.PageVo;
import com.cfz.entity.vo.TagVo;

import java.util.List;


/**
 * 标签(Tag)表服务接口
 *
 * @author makejava
 * @since 2023-03-10 17:33:47
 */
public interface TagService extends IService<Tag> {

    ResponseResult<PageVo> pageTageList(Integer pageNum, Integer pageSize, TagDto tagDto);

    ResponseResult addTag(TagDto tagDto);

    ResponseResult tagDeleteById(List<Long> tagId);

    ResponseResult<Tag> tagSelectById(Long tagId);

    ResponseResult tagUpdateById(Tag tag);

    List<TagVo> listAllTag();
}
