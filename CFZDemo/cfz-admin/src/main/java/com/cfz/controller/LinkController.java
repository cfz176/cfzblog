package com.cfz.controller;

import com.cfz.ResponseResult;
import com.cfz.entity.vo.LinkVo;
import com.cfz.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("content/link")
public class LinkController {

    @Autowired
    private LinkService linkService;

    /**
     * 获取友链列表
     *
     * @param pageNum
     * @param pageSize
     * @param linkVo
     * @return
     */
    @GetMapping("/list")
    public ResponseResult<LinkVo> list(@NotNull Integer pageNum, @NotNull Integer pageSize, LinkVo linkVo) {
        return linkService.listLink(pageNum, pageSize, linkVo);
    }

}
