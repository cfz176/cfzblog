package com.cfz.controller;

import com.cfz.ResponseResult;
import com.cfz.entity.dto.TagDto;
import com.cfz.entity.Tag;
import com.cfz.entity.vo.PageVo;
import com.cfz.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/content/tag")
public class TagController {

    @Autowired
    private TagService tagService;

    /**
     * 查询所有标签
     */
    @GetMapping("/listAllTag")
    public ResponseResult<PageVo> listAllTag() {
        return ResponseResult.okResult(tagService.listAllTag());
    }

    /**
     * 查询标签列表
     */
    @GetMapping("/list")
    public ResponseResult<PageVo> Taglist(Integer pageNum, Integer pageSize, TagDto tagDto) {
        return tagService.pageTageList(pageNum,pageSize, tagDto);
    }

    /**
     * 新增标签
     * @param tagDto
     * @return
     */
    @PreAuthorize("@sp.hasPermission('content:tag:index')")
    @PostMapping
    public ResponseResult tagInsert(@RequestBody TagDto tagDto) {
        return tagService.addTag(tagDto);
    }

    /**
     * 根据id删除标签
     * @param tagId
     * @return
     */
    @PreAuthorize("@sp.hasPermission('content:tag:index')")
    @DeleteMapping("{id}")
    public ResponseResult tagDeleteById(@PathVariable("id") List<Long> tagId) {
        return tagService.tagDeleteById(tagId);
    }

    /**
     * 根据id单条查询标签
     * @param tagId
     * @return
     */
    @GetMapping("{id}")
    public ResponseResult<Tag> tagSelectById(@PathVariable("id") Long tagId) {
        return tagService.tagSelectById(tagId);
    }

    /**
     * 根据id修改标签
     * @return
     */
    @PreAuthorize("@sp.hasPermission('content:tag:index')")
    @PutMapping()
    public ResponseResult tagUpdateById(@RequestBody Tag tag) {
        return tagService.tagUpdateById(tag);
    }

}
