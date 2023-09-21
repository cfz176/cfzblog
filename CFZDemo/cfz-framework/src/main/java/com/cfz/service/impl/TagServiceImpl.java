package com.cfz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cfz.ResponseResult;
import com.cfz.constants.SystemConstants;
import com.cfz.entity.dto.TagDto;
import com.cfz.entity.Tag;
import com.cfz.entity.vo.PageVo;
import com.cfz.entity.vo.TagVo;
import com.cfz.enums.AppHttpCodeEnum;
import com.cfz.exception.SystemException;
import com.cfz.mapper.TagMapper;
import com.cfz.service.TagService;
import com.cfz.utils.BeanCopyUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 标签(Tag)表服务实现类
 *
 * @author makejava
 * @since 2023-03-10 17:33:47
 */
@Service("tagService")
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {


    /**
     * 分页查询所有标签
     * @param pageNum
     * @param pageSize
     * @param tagDto
     * @return
     */
    @Override
    public ResponseResult<PageVo> pageTageList(Integer pageNum, Integer pageSize, TagDto tagDto) {
        //判断tagListDto是否有数据
        LambdaQueryWrapper<Tag> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(StringUtils.hasText(tagDto.getName()), Tag::getName, tagDto.getName());
        lambdaQueryWrapper.eq(StringUtils.hasText(tagDto.getRemark()), Tag::getRemark, tagDto.getRemark());

        //分页查询
        Page<Tag> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        page(page, lambdaQueryWrapper);

        //封装Vo返回
        List<TagVo> tagVos = BeanCopyUtils.copyBeanList(page.getRecords(), TagVo.class);
        return ResponseResult.okResult(new PageVo(tagVos, page.getTotal()));
    }

    /**
     * 新增标签
     * @param tagDto
     * @return
     */
    @Override
    public ResponseResult addTag(TagDto tagDto) {
        //进行非空判断
        if (!StringUtils.hasText(tagDto.getName())) {
            throw new SystemException(AppHttpCodeEnum.TAGNAME_NOT_NULL);
        }
        if (!StringUtils.hasText(tagDto.getRemark())) {
            throw new SystemException(AppHttpCodeEnum.TAGREMARK_NOT_NULL);
        }
        //查询标签是否存在
        if (nameExist(tagDto.getName())) {
            throw new SystemException(AppHttpCodeEnum.TAGRNAME_EXIST);
        }
        Tag tag = BeanCopyUtils.copyBean(tagDto, Tag.class);
        save(tag);
        return ResponseResult.okResult();
    }

    /**
     * 标签删除（逻辑删除）
     * @param tagId
     * @return
     */
    @Override
    public ResponseResult tagDeleteById(List<Long> tagId) {
        if (ObjectUtils.isEmpty(tagId)) {
            throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        LambdaUpdateWrapper<Tag> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(Tag::getId, tagId);
        lambdaUpdateWrapper.set(Tag::getDelFlag, SystemConstants.DEL_FLAG_YES);
        update(lambdaUpdateWrapper);
        return ResponseResult.okResult();
    }

    /**
     * 删除标签
     * @param tagId
     * @return
     */
    @Override
    public ResponseResult<Tag> tagSelectById(Long tagId) {
        if (ObjectUtils.isEmpty(tagId)) {
            throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        return ResponseResult.okResult(getById(tagId));
    }

    @Override
    public ResponseResult tagUpdateById(Tag tag) {
        if (tag.getName().isEmpty()) {
            throw new SystemException(AppHttpCodeEnum.TAGNAME_NOT_NULL);
        }
        if (tag.getRemark().isEmpty()) {
            throw new SystemException(AppHttpCodeEnum.TAGREMARK_NOT_NULL);
        }
        if (ObjectUtils.isEmpty(tag.getId())) {
            throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        updateById(tag);
        return ResponseResult.okResult();
    }

    /**
     * 查询所有标签
     */
    @Override
    public List<TagVo> listAllTag() {
        LambdaQueryWrapper<Tag> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.select(Tag::getId, Tag::getName);
        List<Tag> tagList = list(lambdaQueryWrapper);
        List<TagVo> tagVos = BeanCopyUtils.copyBeanList(tagList, TagVo.class);
        return tagVos;
    }

    /**
     * 判断标签名是否已存在
     * @param name
     * @return
     */
    private boolean nameExist(String name) {
        LambdaQueryWrapper<Tag> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Tag::getName, name);
        return count(lambdaQueryWrapper) > 0;
    }
}
